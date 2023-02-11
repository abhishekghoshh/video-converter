package com.github.ag.core.service;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

public class ReflectionService {
	public void setField(Class<?> className, Object object, String fieldName, Object fieldValue) {
		Field field = ReflectionUtils.findField(className, fieldName);
		field.setAccessible(true);
		ReflectionUtils.setField(field, object, fieldValue);
	}
}
