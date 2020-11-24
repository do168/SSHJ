package sshj.sshj.component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Joiner;

@Component // 1
@Aspect // 2
public class AopCompo {
    private static final Logger logger = LoggerFactory.getLogger(AopCompo.class);

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    @Pointcut("within(sshj.sshj.controller.*)") // 3
    public void onRequest() {}

    @Around("sshj.sshj.component.AopCompo.onRequest()") // 4
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = // 5
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }

        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        } finally {
            long end = System.currentTimeMillis();
            logger.info("Request: {} {}{} < {} ({}ms)", request.getMethod(), request.getRequestURI(),
                    params, request.getRemoteHost(), end - start);
        }
    }

    @Pointcut("within(sshj.sshj.service.*)") // 3
    public void onRequestService() {}

    @Around("sshj.sshj.component.AopCompo.onRequestService()") // 4
    public Object doLoggingService(ProceedingJoinPoint pjp) throws Throwable {

        String parameters = Arrays.toString(pjp.getArgs());
        String methodName = pjp.getSignature().getName();
        String serviceName = pjp.getTarget().toString();
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed(pjp.getArgs()); // 6
        } finally {
            long end = System.currentTimeMillis();
            logger.info("{}, method : {}  parameters : {} ({}ms)", serviceName, methodName,
                    parameters, end - start);
        }
    }
}