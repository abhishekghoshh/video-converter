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
	private int id;
	private Object payload;
	private String className;
	private Date creationTimestamp;

	public RedisEntity() {
		super();
	}

	public RedisEntity(int id, Object payload, String className, Date creationTimestamp) {
		super();
		this.id = id;
		this.payload = payload;
		this.className = className;
		this.creationTimestamp = creationTimestamp;
	}

	public RedisEntity(int id, Object payload) {
		super();
		if (null != payload) {
			this.id = id;
			this.payload = payload;
			this.className = payload.getClass().getCanonicalName();
		}
		this.creationTimestamp = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

}