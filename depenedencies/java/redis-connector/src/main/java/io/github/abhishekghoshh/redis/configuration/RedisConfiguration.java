package io.github.abhishekghoshh.redis.configuration;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.abhishekghoshh.redis.entity.RedisEntity;

@Configuration
@ComponentScan(basePackages = { "io.github.abhishekghoshh.redis" })
public class RedisConfiguration implements ImportAware {

	private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class.getName());

	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> values = importMetadata.getAnnotationAttributes(EnableRedis.class.getName());
		if (log.isDebugEnabled()) {
			log.debug("values are {}", values);
		}
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory(@Autowired RedisConnectionProperties redisConnectionProperties) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(redisConnectionProperties.getHost());
		redisStandaloneConfiguration.setPort(redisConnectionProperties.getPort());
		redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConnectionProperties.getPassword()));
		JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
				.connectTimeout(Duration.ofMillis(1000)).usePooling().build();
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
				jedisClientConfiguration);
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate<String, RedisEntity> redisTemplate(@Autowired JedisConnectionFactory jedisConnectionFactory) {
		RedisTemplate<String, RedisEntity> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new JdkSerializationRedisSerializer());
		template.setValueSerializer(new JdkSerializationRedisSerializer());
		template.setEnableTransactionSupport(true);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	@Qualifier("redisObjectMapper")
	public ObjectMapper redisObjectMapper() {
		return new ObjectMapper();
	}
}
