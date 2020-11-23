package sshj.sshj.configuration.common.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import sshj.sshj.logback.MultiReadableHttpServletRequest;

/**
 * request body multi readable
 * @author bumworld
 *
 */
public class MultiReadableHttpServletRequestFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		MultiReadableHttpServletRequest multiReadRequest = new MultiReadableHttpServletRequest((HttpServletRequest) req);
		chain.doFilter(multiReadRequest, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}