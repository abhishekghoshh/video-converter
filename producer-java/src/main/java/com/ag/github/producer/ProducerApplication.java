package com.ag.github.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ag.github.core.configuration.Reactor;
import com.ag.github.core.configuration.ReactorDefination;

@SpringBootApplication
@EnableScheduling
@Reactor
public class ProducerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Autowired
	ReactorDefination reactorDefination;

	@Override
	public void run(String... args) throws Exception {
//		System.out.println(reactorDefination.getApidef());
	}
}
