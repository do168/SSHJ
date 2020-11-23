package sshj.sshj.configuration.common.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import sshj.sshj.component.ThreadRepository;
import sshj.sshj.logback.AgentUtils;
import sshj.sshj.logback.MultiReadableHttpServletRequest;
import sshj.sshj.model.RequestThreadData;

/**
 * 로깅 데이터 필터
 * @author bumworld
 *
 */
@Slf4j
public class LoggingDataFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			RequestThreadData requestThreadData = ThreadRepository.getThreadData();
			requestThreadData.setStartThreadId(Thread.currentThread().getId());

			HttpServletRequest servletRequest = (HttpServletRequest)request;

			// Set Http Header
			requestThreadData.setHeaderMap(headerMap(servletRequest));

			// Set Http Body
			requestThreadData.setParamMap(parameterMap(servletRequest));

			// Set Agent Detail
			requestThreadData.setUserAgentMap(AgentUtils.getAgentDetail(servletRequest));
			requestThreadData.setUserAgent(AgentUtils.getUserAgentString(servletRequest));

			// Set Http Request URI
			requestThreadData.setRequestURI(getRequestUri(servletRequest));

			requestThreadData.setRequestIpAddress(requestIpAddress(servletRequest));

			MultiReadableHttpServletRequest multiRequest = (MultiReadableHttpServletRequest)request;
			String body = IOUtils.toString(multiRequest.getInputStream(), "utf-8");
			Map bodyMap = (new Gson()).fromJson(body, Map.class);
			requestThreadData.setBodyMap(bodyMap);
		} catch(Exception e) {
			log.error("", e);
		}

		try {
			chain.doFilter(request, response);
		} finally {
			/**
			 * thread 데이터 삭제
			 */
			ThreadRepository.remove();
		}
	}

	@Override
	public void destroy() {

	}

	/**
	 * 요청 ip 주소
	 * @param request
	 * @return
	 */
	private String requestIpAddress(HttpServletRequest request) {
		/*	사용자 리얼 아이피(엣지 서버 타고 들어와서 그렇다.)	*/
		String userIpAddress = "";
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if( StringUtils.isNotBlank(xForwardedFor) ) {
			userIpAddress = xForwardedFor.split(",")[0];
		}
		if( StringUtils.isBlank(userIpAddress) ) {
			userIpAddress = request.getRemoteAddr();
		}

		return userIpAddress;
	}

	/**
	 * 요청 헤더 데이터
	 * @param request
	 * @return
	 */
	private Map<String, String> headerMap(HttpServletRequest request) {
		Map<String, String> convertedHeaderMap = new HashMap<>();

		Enumeration<String> headerMap = request.getHeaderNames();

		while (headerMap.hasMoreElements()) {
			String name = headerMap.nextElement();
			String value = request.getHeader(name);

			convertedHeaderMap.put(name, value);
		}
		return convertedHeaderMap;
	}

	/**
	 * 요청 파라미터 데이터
	 * @param request
	 * @return
	 */
	private Map<String, Object> parameterMap(HttpServletRequest request) {
		Map<String, Object> convertedParameterMap = new HashMap<>();
		Map<String, String[]> parameterMap = request.getParameterMap();

		for (String key : parameterMap.keySet()) {
			String[] values = parameterMap.get(key);
			convertedParameterMap.put(key, values);
		}
		return convertedParameterMap;
	}

	/**
	 * 요청 주소
	 * @param request
	 * @return
	 */
	private String getRequestUri(HttpServletRequest request) {
		return request.getRequestURI();
	}
}