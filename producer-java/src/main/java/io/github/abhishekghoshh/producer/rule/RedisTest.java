package io.github.abhishekghoshh.producer.rule;

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
public class RedisTest implements Rule {
	@Autowired
	RedisRepository redisRepository;
	private static final Logger log = LoggerFactory.getLogger(RedisTest.class);

	@Override
	public void process(DomainModel domainModel) throws Exception {
		try {
			String hashKey = "Student";
			Map<String, String> map = (Map<String, String>) domainModel.getRequestBody();
			redisRepository.save(hashKey, map);
			domainModel.setResponseEntity(
					ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(redisRepository.findAll(hashKey)));
		} catch (Exception ex) {
			log.error("exception is {}", ex.getMessage());
			StackTraceElement[] traces = ex.getStackTrace();
			for (StackTraceElement trace : traces)
				log.error("{}", trace.getClassName() + "." + trace.getMethodName() + ":" + trace.getLineNumber());
		}

	}

}
