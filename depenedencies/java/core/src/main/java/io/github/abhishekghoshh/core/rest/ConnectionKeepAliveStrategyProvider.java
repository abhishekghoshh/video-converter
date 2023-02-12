package io.github.abhishekghoshh.core.rest;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ConnectionKeepAliveStrategyProvider {

	@Bean
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return new DefaultConnectionKeepAliveStrategy();
	}
}
