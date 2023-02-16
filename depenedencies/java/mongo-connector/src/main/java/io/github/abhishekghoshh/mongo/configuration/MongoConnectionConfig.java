package io.github.abhishekghoshh.mongo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;


@Configuration
public class MongoConnectionConfig extends AbstractMongoClientConfiguration {
	@Value("${spring.data.mongodb.host}")
	private String host;
	@Value("${spring.data.mongodb.database}")
	private String database;
	@Value("${spring.data.mongodb.port}")
	private String port;

	@Override
	protected String getDatabaseName() {
		return database;
	}
	@Bean
	public GridFsTemplate gridFsTemplate(@Autowired MongoTemplate mongoTemplate) throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mongoTemplate.getConverter());
	}
}