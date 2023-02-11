package com.ag.github.core.rest;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateProvider {

	Logger log = LoggerFactory.getLogger(RestTemplateProvider.class);

	@Bean
	public RestTemplate restTemplate(@Autowired RestTemplateProperties restTemplateProperties,
			@Autowired SSLContext sslContext, @Autowired HostnameVerifier hostnameVerifier,
			@Autowired ConnectionKeepAliveStrategy keepAliveStrategy) {

		SSLConnectionSocketFactory sSLConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
				hostnameVerifier);
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sSLConnectionSocketFactory).register("http", new PlainConnectionSocketFactory())
				.build();
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				socketFactoryRegistry);

		SocketConfig socketconfig = SocketConfig.custom()
				.setSoTimeout(restTemplateProperties.getReadTimeout(), TimeUnit.SECONDS).build();
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManagerBuilder = PoolingHttpClientConnectionManagerBuilder
				.create().setDefaultSocketConfig(socketconfig).setSSLSocketFactory(sSLConnectionSocketFactory)
				.setMaxConnTotal(200).setMaxConnPerRoute(20).build();

		HttpClient httpClient = HttpClients.custom().evictIdleConnections(TimeValue.of(30, TimeUnit.SECONDS))
				.setConnectionManager(poolingHttpClientConnectionManagerBuilder).disableAutomaticRetries()
				.setKeepAliveStrategy(keepAliveStrategy).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectionRequestTimeout(restTemplateProperties.getConnectionRequestTimeout());
		requestFactory.setConnectTimeout(restTemplateProperties.getConnectTimeout());

		return new RestTemplate(requestFactory);
	}

}
