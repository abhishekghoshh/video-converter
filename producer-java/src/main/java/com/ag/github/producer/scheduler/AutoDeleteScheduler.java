package com.ag.github.producer.scheduler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.io.*;
import java.util.UUID;

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

    public void convertToAudio() throws IOException {
        HttpServletRequest httpServletRequest = null;
        InputStream inputStream = httpServletRequest.getInputStream();
        String sourceFileName = UUID.randomUUID() + ".mp4";
        File source = new File("media/" + sourceFileName);
        FileOutputStream fileOutputStream = new FileOutputStream(source);
        byte[] bytes = inputStream.readAllBytes();
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

        String destinationFileName = UUID.randomUUID() + ".mp3";
        File target = new File("media/" + destinationFileName);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("aac");
        audio.setBitRate(64000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);

        try {
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(target));
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + target.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .headers(header)
                .contentLength(target.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}