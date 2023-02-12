package io.github.abhishekghoshh.core.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.abhishekghoshh.core.rule.RuleRuntimeException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerHandlerAdvisor extends ResponseEntityExceptionHandler {
	@ExceptionHandler(RuleRuntimeException.class)
	public ResponseEntity<?> handleRestCallException(RuleRuntimeException ex, HttpServletRequest request) {
		return ResponseEntity.status(ex.getStatus()).body(build(ex));
	}

	private Map<String, String> build(RuleRuntimeException ex) {
		Map<String, String> map = new HashMap<>();
		map.put("exception", ex.getMessage());
		return map;
	}

}
