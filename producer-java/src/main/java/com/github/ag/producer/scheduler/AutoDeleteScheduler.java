package com.github.ag.producer.scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoDeleteScheduler {
	private static final Logger log = LoggerFactory.getLogger(AutoDeleteScheduler.class);

	@Scheduled(fixedRate = 3000)
	public void autoDelete() throws IOException {
		File file = new File("media");
		if (file.isDirectory()) {
			String[] names = file.list();
			for (String name : names) {
				File innerFile = new File("media/" + name);
				BasicFileAttributes attributes = Files.readAttributes(innerFile.toPath(), BasicFileAttributes.class);
				long creationTimestamp = attributes.creationTime().to(TimeUnit.MILLISECONDS);
				if ((innerFile.getName().endsWith("mp3") || innerFile.getName().endsWith("mp4"))
						&& (System.currentTimeMillis() - creationTimestamp) > 60000 && innerFile.delete())
					log.debug(innerFile.getName() + " is deleted successfully");
			}
		}
	}

}