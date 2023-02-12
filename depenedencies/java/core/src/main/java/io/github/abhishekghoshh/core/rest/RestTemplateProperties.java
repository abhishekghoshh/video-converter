package io.github.abhishekghoshh.core.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rest-template")
@Configuration
public class RestTemplateProperties {
	private int connectionRequestTimeout = 1000;
	private int connectTimeout = 1000;
	private int readTimeout = 1000;

	public RestTemplateProperties() {
		super();
	}

	public RestTemplateProperties(int connectionRequestTimeout, int connectTimeout, int readTimeout) {
		super();
		this.connectionRequestTimeout = connectionRequestTimeout;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	@Override
	public String toString() {
		return "RestTemplateProperties [connectionRequestTimeout=" + connectionRequestTimeout + ", connectTimeout="
				+ connectTimeout + ", readTimeout=" + readTimeout + "]";
	}

}
