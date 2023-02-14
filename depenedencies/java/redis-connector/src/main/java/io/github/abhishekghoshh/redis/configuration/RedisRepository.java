package io.github.abhishekghoshh.redis.configuration;

import java.util.ArrayList;
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
	HashOperations<String, String, RedisEntity> hashOperations;

	@Autowired
	@Qualifier("redisObjectMapper")
	ObjectMapper objectMapper;

	public RedisRepository(@Autowired RedisTemplate<String, RedisEntity> redisTemplate) {
		this.hashOperations = redisTemplate.opsForHash();
	}

	public String save(String collectionName, Object payload) {
		RedisEntity redisEntity = new RedisEntity(payload);
		saveOrUpdate(collectionName, redisEntity);
		return redisEntity.getId();
	}

	public String save(String id, String collectionName, Object payload) {
		RedisEntity redisEntity = new RedisEntity(id, payload);
		saveOrUpdate(collectionName, redisEntity);
		return redisEntity.getId();
	}

	private void saveOrUpdate(String collectionName, RedisEntity redisEntity) {
		hashOperations.put(collectionName, redisEntity.getId(), redisEntity);
		logger.debug("stored in redis with id {}", redisEntity.getId());
	}

	public List<RedisEntity> findAll(String collectionName) {
		return hashOperations.values(collectionName);

	}

	public <T> List<T> findAll(String collectionName, Class<T> class_) {
		List<RedisEntity> redisEntities = findAll(collectionName);
		List<T> list = new ArrayList<>();
		for (RedisEntity redisEntity : redisEntities) {
			list.add(cast(class_, redisEntity));
		}
		return list;
	}

	public RedisEntity find(String collectionName, String id) {
		return (RedisEntity) hashOperations.get(collectionName, id);
	}

	public <T> T find(String collectionName, String id, Class<T> class_) {
		return cast(class_, hashOperations.get(collectionName, id));
	}

	private <T> T cast(Class<T> class_, RedisEntity redisEntity) {
		if (null != redisEntity && null != redisEntity.getPayload()) {
			try {
				return cast(class_, redisEntity);
			} catch (Exception ex) {
				logger.error("exception is {} , Can not convert to {}", ex.getMessage(), class_.getSimpleName());
			}
		}
		return null;
	}

	public boolean delete(String collectionName) {
		List<RedisEntity> redisEntities = findAll(collectionName);
		for (RedisEntity redisEntity : redisEntities)
			if (delete(collectionName, redisEntity.getId()))
				return false;
		return true;
	}

	public boolean delete(String collectionName, String id) {
		try {
			hashOperations.delete(collectionName, id);
			return true;
		} catch (Exception ex) {
			logger.error("unable to delete redis entity for collectionName {} , id {}", collectionName, id);
			return false;
		}
	}
}
