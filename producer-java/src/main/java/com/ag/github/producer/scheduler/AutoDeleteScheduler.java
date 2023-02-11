package com.ag.github.producer.scheduler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoDeleteScheduler {
	private static final Logger log = LoggerFactory.getLogger(AutoDeleteScheduler.class);

	@Scheduled(fixedRate = 3000)
	public void autoDelete() {
		File file = new File("media");
		if (file.isDirectory()) {
			String[] names = file.list();
			for (String name : names) {
				File innerFile = new File("media/" + name);
				if ((innerFile.getName().endsWith("mp3") || innerFile.getName().endsWith("mp4")) && innerFile.delete())
					log.debug(innerFile.getName() + " is deleted successfully");
			}
		}
	}

}