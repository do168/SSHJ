package sshj.sshj.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;
import sshj.sshj.configuration.properties.InterceptorProps;

@Configuration
@ComponentScan("sshj.sshj")
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorProps interceptorProps;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("addInterceptors {}", interceptorProps.toString());

//        	TODO: 추후 로그인 인터셉터 추가 예쩡 Auth check do168
//        
//        {
//            InterceptorMappingProps interceptorMappingProps = interceptorProps.getAuthCheck();
//            InterceptorRegistration interceptorRegistration = registry.addInterceptor((HandlerInterceptor) applicationContext.getBean(interceptorMappingProps.getInterceptorName()));
//            interceptorRegistration.addPathPatterns(interceptorMappingProps.getMappings());
//            interceptorRegistration.excludePathPatterns(interceptorMappingProps.getExcludeMappings());
//            interceptorRegistration.order(Ordered.HIGHEST_PRECEDENCE);
//        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
