package io.github.abhishekghoshh.producer.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.abhishekghoshh.core.model.DomainModel;
import io.github.abhishekghoshh.core.rule.Rule;
import jakarta.servlet.http.HttpServletRequest;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

@Service
public class ConverterRule implements Rule {
	private static final Logger log = LoggerFactory.getLogger(ConverterRule.class);

	@Override
	public void process(DomainModel domainModel) throws Exception {
		log.info("http servelet request class {}", domainModel.getHttpServletRequest().getClass());
		HttpServletRequest httpServletRequest = domainModel.getHttpServletRequest();
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
		ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok().headers(header)
				.contentLength(target.length()).contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(resource);
		domainModel.setResponseEntity(responseEntity);
	}

}