package com.github.ag.core.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Configuration
public class ReactorFiler implements Filter {
	private static final Logger log = LoggerFactory.getLogger(ReactorFiler.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String uuid = httpServletRequest.getHeader("uuid");
		String sid = httpServletRequest.getHeader("sid");
		if (null == uuid) {
			uuid = UUID.randomUUID().toString();
		}
		if (null == sid) {
			sid = "ANONYMOUS";
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("uuid", uuid);
		headers.put("sid", sid);
		final HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(httpServletRequest) {
			@Override
			public String getHeader(String name) {
				if (headers.containsKey(name)) {
					return headers.get(name);
				}
				return super.getHeader(name);
			}
		};
		// setting mdc context map
		Map<String, String> context = new HashMap<>();
		context.put("sid", sid);
		context.put("uuid", uuid);
		context.put("method", httpServletRequest.getMethod());
		context.put("uri", httpServletRequest.getRequestURI());
		context.put("startTime", String.valueOf(System.currentTimeMillis()));
		ThreadContext.putAll(context);
		MDC.setContextMap(context);
		log.debug("transaction started for uuid {}", uuid);
		chain.doFilter(httpServletRequestWrapper, response);
	}

}
