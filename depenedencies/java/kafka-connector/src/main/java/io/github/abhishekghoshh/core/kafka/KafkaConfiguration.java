package io.github.abhishekghoshh.core.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@ComponentScan(basePackages = { "io.github.abhishekghoshh.kafka" })
public class KafkaConfiguration implements ImportAware {

	private final Logger log = LoggerFactory.getLogger(KafkaConfiguration.class.getName());

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> values = importMetadata.getAnnotationAttributes(KafkaConfiguration.class.getName());
		if (log.isDebugEnabled()) {
			log.debug("values are {}", values);
		}
	}

}
