package io.github.abhishekghoshh.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.abhishekghoshh.core.configuration.Reactor;

@SpringBootApplication
@EnableScheduling
@Reactor
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}
}
