package io.github.abhishekghoshh.redis.configuration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.abhishekghoshh.redis.entity.RedisEntity;

@Repository
public class RedisRepository {
	final Logger logger = LoggerFactory.getLogger(RedisRepository.class);
	HashOperations<String, Integer, RedisEntity> hashOperations;

	@Autowired
	@Qualifier("redisObjectMapper")
	ObjectMapper objectMapper;

	public RedisRepository(@Autowired RedisTemplate<String, RedisEntity> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();
	}

	public int create(String collectionName, Object payload) {
		RedisEntity redisEntity = new RedisEntity(0, payload);
		hashOperations.put(collectionName, redisEntity.getId(), redisEntity);
		return redisEntity.getId();
	}

	public List<RedisEntity> findAll(String collectionName) {
		return hashOperations.values(collectionName);
	}

	public RedisEntity findProductById(String collectionName, int id) {
		return (RedisEntity) hashOperations.get(collectionName, id);
	}
}
