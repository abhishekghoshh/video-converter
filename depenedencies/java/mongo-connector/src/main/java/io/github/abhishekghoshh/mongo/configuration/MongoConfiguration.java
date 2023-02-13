package io.github.abhishekghoshh.mongo.configuration;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@ComponentScan(basePackages = { "io.github.abhishekghoshh.mongo" })
public class MongoConfiguration implements ImportAware {

	private final Logger log = LoggerFactory.getLogger(MongoConfiguration.class.getName());


	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> values = importMetadata.getAnnotationAttributes(EnableMongo.class.getName());
		if (log.isDebugEnabled()) {
			log.debug("values are {}", values);
		}
	}

}
