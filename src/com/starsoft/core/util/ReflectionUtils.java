package com.starsoft.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReflectionUtils {
	private static Logger logger = LoggerFactory
			.getLogger(ReflectionUtils.class);

	public static Object invokeGetterMethod(Object obj, String propertyName) {
		String getterMethodName = "get" + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getterMethodName, new Class[0], new Object[0]);
	}

	public static void invokeSetterMethod(Object obj, String propertyName,
			Object value) {
		invokeSetterMethod(obj, propertyName, value, null);
	}

	public static void invokeSetterMethod(Object obj, String propertyName,
			Object value, Class<?> propertyType) {
		Class type = (propertyType != null) ? propertyType : value.getClass();
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokeMethod(obj, setterMethodName, new Class[] { type },
				new Object[] { value });
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field ["
					+ fieldName + "] on target [" + obj + "]");

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	public static Field getAccessibleField(Object obj, String fieldName) {
		Assert.notNull(obj, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		Class superClass = obj.getClass();
		if (superClass != Object.class) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				superClass = superClass.getSuperclass();
			}

		}

		return null;
	}

	public static Object invokeMethod(Object obj, String methodName,
			Class<?>[] parameterTypes, Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null)
			throw new IllegalArgumentException("Could not find method ["
					+ methodName + "] on target [" + obj + "]");

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	public static Method getAccessibleMethod(Object obj, String methodName,
			Class<?>[] parameterTypes) {
		Assert.notNull(obj, "object不能为空");

		Class superClass = obj.getClass();
		if (superClass != Object.class) {
			try {
				Method method = superClass.getDeclaredMethod(methodName,
						parameterTypes);

				method.setAccessible(true);

				return method;
			} catch (NoSuchMethodException e) {
				superClass = superClass.getSuperclass();
			}

		}

		return null;
	}

	public static <T> Class<T> getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName()
					+ "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if ((index >= params.length) || (index < 0)) {
			logger.warn("Index: " + index + ", Size of "
					+ clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);

			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return ((Class) params[index]);
	}

	public static RuntimeException convertReflectionExceptionToUnchecked(
			Exception e) {
		if ((e instanceof IllegalAccessException)
				|| (e instanceof IllegalArgumentException)
				|| (e instanceof NoSuchMethodException)) {
			return new IllegalArgumentException("Reflection Exception.", e);
		}
		if (e instanceof InvocationTargetException)
			return new RuntimeException("Reflection Exception.",
					((InvocationTargetException) e).getTargetException());
		if (e instanceof RuntimeException)
			return ((RuntimeException) e);

		return new RuntimeException("Unexpected Checked Exception.", e);
	}
}