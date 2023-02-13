package io.github.abhishekghoshh.core.configuration;

import java.util.Map;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class ReactorInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(ReactorInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		if (!CollectionUtils.isEmpty(MDC.getCopyOfContextMap())) {
			Map<String, String> context = MDC.getCopyOfContextMap();
			long responseTime = System.currentTimeMillis() - Long.valueOf(context.get("startTime"));
			String uuid = context.get("uuid");
			log.debug("transaction completed for uuid {}", uuid);
			log.info("ResponseCode={}|ResponseTime={}ms", response.getStatus(), responseTime);
			ThreadContext.clearMap();
			MDC.clear();
		}
	}

}
