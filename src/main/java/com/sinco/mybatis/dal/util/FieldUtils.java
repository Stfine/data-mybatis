package com.sinco.mybatis.dal.util;

import java.lang.reflect.Field;

//import org.springframework.util.ReflectionUtils;


public abstract class FieldUtils {
	
	public static boolean hasField(Object target, String fieldName) {
		return org.apache.commons.lang3.reflect.FieldUtils.getField(target.getClass(), fieldName) == null;
	}

	public static Object getFieldValue(Object target, String fieldName) throws IllegalAccessException {
		Field field =org.apache.commons.lang3.reflect.FieldUtils.getField(target.getClass(), fieldName,true);
		return org.apache.commons.lang3.reflect.FieldUtils.readField(field, target);
	}
	
	public static void setFieldValue(Object target, String fieldName, Object value) throws IllegalAccessException {
		Field field =org.apache.commons.lang3.reflect.FieldUtils.getField(target.getClass(), fieldName,true);
		org.apache.commons.lang3.reflect.FieldUtils.writeField(field, target, value);
	}
}
