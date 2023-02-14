package io.github.abhishekghoshh.core.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Configuration
public class ReactorFiler implements Filter {
	private static final String ANONYMOUS = "ANONYMOUS";
	private static final Logger log = LoggerFactory.getLogger(ReactorFiler.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String uuid = httpServletRequest.getHeader("uuid");
		String sid = httpServletRequest.getHeader("sid");
		if (null == uuid) {
			uuid = UUID.randomUUID().toString();
			log.debug("uuid not found in header, setting uuid to {} ", uuid);
		}
		if (null == sid) {
			sid = ANONYMOUS;
			log.debug("uuid not found in header, setting uuid to {} ", sid);
		}
		Map<String, String> headers = new HashMap<>();
		headers.put("uuid", uuid);
		headers.put("sid", sid);
		HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(httpServletRequest) {
			private byte[] cachedBody;

			@Override
			public String getHeader(String name) {
				if (headers.containsKey(name)) {
					return headers.get(name);
				}
				return super.getHeader(name);
			}

			@Override
			public ServletInputStream getInputStream() throws IOException {
				initCachedBody(request);
				return new CachedBodyServletInputStream(this.cachedBody);
			}

			@Override
			public BufferedReader getReader() throws IOException {
				initCachedBody(request);
				return new BufferedReader(new InputStreamReader(this.getInputStream()));
			}

			private void initCachedBody(ServletRequest request) throws IOException {
				if (null == this.cachedBody) {
					InputStream requestInputStream = request.getInputStream();
					this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
				}
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
		log.debug("Incoming headers for uuid {}", uuid);
		Collections.list(httpServletRequest.getHeaderNames()).stream()
				.forEach(headerName -> log.debug("{} -> {}", headerName, httpServletRequest.getHeader(headerName)));
		chain.doFilter(request, response);
	}

}
