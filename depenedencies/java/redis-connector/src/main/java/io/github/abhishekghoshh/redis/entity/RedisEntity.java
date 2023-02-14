package io.github.abhishekghoshh.redis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("RedisEntity")
public class RedisEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5186324066701590738L;
	@Id
	private String id;
	private Object payload;
	private String className;
	private String creationTimestamp;

	public RedisEntity() {
		super();
	}

	public RedisEntity(String id, Object payload, String className, String creationTimestamp) {
		super();
		this.id = id;
		this.payload = payload;
		this.className = className;
		this.creationTimestamp = creationTimestamp;
	}

	public RedisEntity(String id, Object payload) {
		super();
		if (null != payload) {
			this.id = id;
			this.payload = payload;
			this.className = payload.getClass().getCanonicalName();
		}
		this.creationTimestamp = new Date().toString();
	}

	public RedisEntity(Object payload) {
		this(RedisEntity.uniqueId(), payload);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static String uniqueId() {
		return UUID.randomUUID().toString();
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(String creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

}