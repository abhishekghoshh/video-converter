package io.github.abhishekghoshh.producer.rule;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.abhishekghoshh.core.model.DomainModel;
import io.github.abhishekghoshh.core.rule.Rule;
import io.github.abhishekghoshh.redis.configuration.RedisRepository;
import io.github.abhishekghoshh.redis.entity.RedisEntity;

@Service
public class RedisTest implements Rule {
	@Autowired
	RedisRepository redisRepository;
	private static final Logger log = LoggerFactory.getLogger(RedisTest.class);

	@Override
	public void process(DomainModel domainModel) throws Exception {
		try {
			String hashKey = "Student";
			Map<String, String> map = (Map<String, String>) domainModel.getRequestBody();
			redisRepository.create(hashKey, map);
			domainModel.setResponseEntity(ResponseEntity.ok(redisRepository.findAll(hashKey).stream()
					.map(RedisEntity::getPayload).collect(Collectors.toList())));
		} catch (Exception ex) {
			log.error("exception is {}", ex.getMessage());
		}

	}

}
