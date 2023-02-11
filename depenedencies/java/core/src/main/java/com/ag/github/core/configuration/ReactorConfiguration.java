package com.ag.github.core.configuration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(basePackages = { "com.ag.github.core" })
public class ReactorConfiguration implements ImportAware, WebMvcConfigurer {

	private final Logger log = LoggerFactory.getLogger(ReactorConfiguration.class.getName());
	@Autowired
	ReactorInterceptor reactorInterceptor;

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> values = importMetadata.getAnnotationAttributes(Reactor.class.getName());
		if (log.isDebugEnabled()) {
			log.debug("values are {}", values);
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(reactorInterceptor);
	}

	@Bean
	public FilterRegistrationBean<ReactorFiler> loggingFilter(@Autowired ReactorFiler reactorFiler) {
		FilterRegistrationBean<ReactorFiler> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(reactorFiler);
		registrationBean.addUrlPatterns("*");
		return registrationBean;
	}

	@Bean("reactorThreadPool")
	public TaskExecutor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(500);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("reator-");
		return executor;
	}

	@Bean("coreObjectMapper")
	public ObjectMapper coreObjectMapper() {
		return new ObjectMapper();
	}

}
