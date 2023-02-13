package io.github.abhishekghoshh.redis.configuration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@ComponentScan(basePackages = { "io.github.abhishekghoshh.redis" })
public class RedisConfiguration implements ImportAware {

	private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class.getName());

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> values = importMetadata.getAnnotationAttributes(Reactor.class.getName());
		if (log.isDebugEnabled()) {
			log.debug("values are {}", values);
		}
	}

}
