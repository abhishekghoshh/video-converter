package io.github.abhishekghoshh.mongo.configuration;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

import io.github.abhishekghoshh.mongo.entity.MongoFileEntity;

@Service
public class MongoFileService {
	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Autowired
	private GridFsOperations gridFsOperations;

	public String addFile(MultipartFile upload) throws IOException {
		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", upload.getSize());
		Object fileID = gridFsTemplate.store(upload.getInputStream(), upload.getOriginalFilename(),
				upload.getContentType(), metadata);
		return fileID.toString();
	}


	public MongoFileEntity downloadFile(String id) throws IOException {
		GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
		MongoFileEntity mongoFileEntity = new MongoFileEntity();
		if (gridFSFile != null && gridFSFile.getMetadata() != null) {
			mongoFileEntity.setFilename(gridFSFile.getFilename());
			mongoFileEntity.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
			mongoFileEntity.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
			mongoFileEntity.setFile(IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));
		}
		return mongoFileEntity;
	}
}
