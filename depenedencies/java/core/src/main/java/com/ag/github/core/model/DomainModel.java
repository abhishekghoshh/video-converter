package com.ag.github.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

public class DomainModel {

	ObjectMapper objectMapper = new ObjectMapper();
	private HttpServletRequest httpServletRequest;
	private Map<String, String> requestHeaders;
	private Map<String, List<String>> requestParams;
	private Object requestBody;
	private ResponseEntity<?> responseEntity;

	private Map<String, Object> runtimeData;

	public DomainModel(HttpServletRequest httpServletRequest) throws IOException, ClassNotFoundException {
		init(httpServletRequest, null);
	}

	public DomainModel(HttpServletRequest httpServletRequest, String className)
			throws IOException, ClassNotFoundException {
		init(httpServletRequest, className);
	}

	private void init(HttpServletRequest httpServletRequest, String className)
			throws IOException, ClassNotFoundException {
		this.httpServletRequest = httpServletRequest;
		Iterator<String> headerNamesIterator = httpServletRequest.getHeaderNames().asIterator();
		Map<String, String> requestHeaders = new HashMap<>();
		while (headerNamesIterator.hasNext()) {
			String headerName = headerNamesIterator.next();
			requestHeaders.put(headerName, httpServletRequest.getHeader(headerName));
		}
		this.requestHeaders = Collections.unmodifiableMap(requestHeaders);

		Map<String, List<String>> requestParams = new HashMap<>();
		for (Map.Entry<String, String[]> parameterEntry : httpServletRequest.getParameterMap().entrySet()) {
			if (null == parameterEntry.getValue() || 0 == parameterEntry.getValue().length) {
				continue;
			}
			String parameterName = parameterEntry.getKey();
			List<String> parameterValues = new ArrayList<>();
			for (String parameterValue : parameterEntry.getValue())
				parameterValues.add(parameterValue);
			requestParams.put(parameterName, parameterValues);

		}
		this.requestParams = Collections.unmodifiableMap(requestParams);

		if (null != className && !"".equals(className.strip())) {
			byte[] inputStreamBytes = StreamUtils.copyToByteArray(httpServletRequest.getInputStream());
			Class<?> class_ = Class.forName(className);
			this.requestBody = new ObjectMapper().readValue(inputStreamBytes, class_);
		}
		this.runtimeData = new HashMap<>();
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public Map<String, List<String>> getRequestParams() {
		return requestParams;
	}

	public Object getRequestBody() {
		return requestBody;
	}

	public ResponseEntity<?> getResponseEntity() {
		return responseEntity;
	}

	public DomainModel setResponseEntity(ResponseEntity<?> responseEntity) {
		this.responseEntity = responseEntity;
		return this;
	}

	public Map<String, Object> getRuntimeData() {
		return runtimeData;
	}
}
