package io.github.abhishekghoshh.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextManager {
	@Autowired
	private ApplicationContext applicationContext;

	public Object get(String className) throws ClassNotFoundException {
		if (null == className || 0 != className.length())
			throw new RuntimeException("class name is empty");
		Class<?> classType = Class.forName(className);
		String[] beans = applicationContext.getBeanNamesForType(classType);
		if (beans.length == 0)
			throw new RuntimeException("no beans found for class name " + className);
		return applicationContext.getBean(beans[0]);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> class_) {
		String[] beans = applicationContext.getBeanNamesForType(class_);
		if (beans.length == 0)
			throw new RuntimeException("no beans found for class name " + class_.getCanonicalName());
		return (T) applicationContext.getBean(beans[0]);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> class_) {
		String[] beans = applicationContext.getBeanNamesForType(class_);
		if (beans.length == 0)
			throw new RuntimeException("no beans found for class name " + class_.getCanonicalName());
		List<T> list = new ArrayList<>();
		for (String bean : beans)
			list.add((T) applicationContext.getBean(bean));
		return list;
	}
}
