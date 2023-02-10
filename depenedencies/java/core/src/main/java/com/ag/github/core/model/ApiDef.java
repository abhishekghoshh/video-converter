package com.ag.github.core.model;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMethod;

public class ApiDef {
	private String key;
	private String path;
	private RequestMethod method;
	private String requestBodyClass;
	private List<ServiceDef> servicedef;

	public ApiDef(String path, RequestMethod method, String requestBodyClass, List<ServiceDef> servicedef) {
		super();
		this.path = path;
		this.method = method;
		this.requestBodyClass = requestBodyClass;
		this.servicedef = servicedef;
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

	@Override
	public String toString() {
		return "ApiDef [path=" + path + ", method=" + method + ", requestBodyClass=" + requestBodyClass
				+ ", servicedef=" + servicedef + ", key=" + key + "]";
	}

}
