package com.github.ag.core.model;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;

public class ApiDef {
	private boolean active = true;
	private String key;
	private String path;
	private RequestMethod method;
	private String requestBodyClass;
	private List<ServiceDef> servicedef;
	private boolean hasDefaultResponse = false;
	private String defaultResponse;

	public ApiDef(boolean active, String key, String path, RequestMethod method, String requestBodyClass,
			List<ServiceDef> servicedef, boolean hasDefaultResponse, String defaultResponse) {
		super();
		this.active = active;
		this.key = key;
		this.path = path;
		this.method = method;
		this.requestBodyClass = requestBodyClass;
		this.servicedef = servicedef;
		this.hasDefaultResponse = hasDefaultResponse;
		this.defaultResponse = defaultResponse;
	}

	public ApiDef() {
		super();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public String getRequestBodyClass() {
		return requestBodyClass;
	}

	public void setRequestBodyClass(String requestBodyClass) {
		this.requestBodyClass = requestBodyClass;
	}

	public List<ServiceDef> getServicedef() {
		return servicedef;
	}

	public void setServicedef(List<ServiceDef> servicedef) {
		this.servicedef = servicedef;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isHasDefaultResponse() {
		return hasDefaultResponse;
	}

	public void setHasDefaultResponse(boolean hasDefaultResponse) {
		this.hasDefaultResponse = hasDefaultResponse;
	}

	public String getDefaultResponse() {
		return defaultResponse;
	}

	public void setDefaultResponse(String defaultResponse) {
		this.defaultResponse = defaultResponse;
	}

	@Override
	public String toString() {
		return "ApiDef [active=" + active + ", key=" + key + ", path=" + path + ", method=" + method
				+ ", requestBodyClass=" + requestBodyClass + ", servicedef=" + servicedef + ", hasDefaultResponse="
				+ hasDefaultResponse + ", defaultResponse=" + defaultResponse + "]";
	}
}
