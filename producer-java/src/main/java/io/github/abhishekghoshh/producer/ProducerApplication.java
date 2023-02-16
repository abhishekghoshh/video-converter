package io.github.abhishekghoshh.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.abhishekghoshh.core.configuration.Reactor;
import io.github.abhishekghoshh.mongo.configuration.EnableMongo;
import io.github.abhishekghoshh.redis.configuration.EnableRedis;

@SpringBootApplication
@EnableScheduling
@Reactor
@EnableRedis
@EnableMongo
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}
}
