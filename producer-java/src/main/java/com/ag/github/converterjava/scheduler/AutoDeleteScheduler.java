package com.ag.github.converterjava.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class AutoDeleteScheduler {
    @Scheduled(fixedRate = 3000)
    public void autoDelete() {
        File file = new File("media");
        if (file.isDirectory()) {
            String[] names = file.list();
            for (String name : names) {
                File innerFile = new File("media/" + name);
                if ((innerFile.getName().endsWith("mp3") || innerFile.getName().endsWith("mp4")) && innerFile.delete())
                    System.out.println(innerFile.getName() + " is deleted successfully");
            }
        }
    }
}