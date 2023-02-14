package io.github.abhishekghoshh.producer.rule;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.abhishekghoshh.core.model.DomainModel;
import io.github.abhishekghoshh.core.rule.Rule;
import io.github.abhishekghoshh.redis.configuration.RedisRepository;

@Service
public class MongoTest implements Rule {
	@Autowired
	RedisRepository redisRepository;
	private static final Logger log = LoggerFactory.getLogger(MongoTest.class);

	@Override
	public void process(DomainModel domainModel) throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("name", "Abhishek Ghosh");
		ResponseEntity<Map<String, String>> responseEntity = ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON).body(body);
		domainModel.setResponseEntity(responseEntity);
	}

}
