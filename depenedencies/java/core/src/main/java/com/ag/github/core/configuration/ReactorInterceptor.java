package com.ag.github.core.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ReactorInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServeletRequest, HttpServletResponse httpServletResponse,
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServeletRequest, HttpServletResponse httpServletResponse,
			Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServeletRequest, HttpServletResponse httpServletResponse,
			Object handler, Exception exception) throws Exception {

	}
}
