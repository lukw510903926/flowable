package com.flowable.core.util.context;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class ContextFactory implements ApplicationContextAware {
	
	private static final Logger logger = Logger.getLogger(ContextFactory.class);

	private static ApplicationContext appContext = null;

	public static ApplicationContext createApplicationContext(
			String configLocation) {
		try {
			
			appContext = new ClassPathXmlApplicationContext(configLocation);
			
		} catch (Exception e) {
			logger.error("createApplicationContext error:", e);
		}
		return appContext;
	}

	public static ApplicationContext getApplicationContext() {
		return appContext;
	}

	public static void initAutowiredFields(Object obj) throws Exception {
		if (obj == null || appContext == null) {
			return;
		}

		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getAnnotation(Autowired.class) != null) {
					field.setAccessible(true);
					field.set(obj, appContext.getBean(field.getType()));
				}
			}
		}
	}
	
	public static Object getBeanByName(String beanName) {
		return appContext.getBean(beanName);
	}
	
	public static Object getBeanByType(Class<?> requiredType) {
		return appContext.getBean(requiredType);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;
	}
}
