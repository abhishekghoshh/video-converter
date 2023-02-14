package io.github.abhishekghoshh.redis.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("redis.connection")
public class RedisConnectionProperties {
	private String host;
	private int port;
	private String password;
	private long connectTimeout;

	public RedisConnectionProperties() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RedisConnectionProperties(String host, int port, String password, long connectTimeout) {
		super();
		this.host = host;
		this.port = port;
		this.password = password;
		this.connectTimeout = connectTimeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

}
