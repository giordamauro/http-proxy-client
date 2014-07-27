package com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ExceptionDeserializer implements JsonDeserializer<Exception> {

	@Override
	public Exception deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {

		ExceptionData exceptionData = context.deserialize(json, ExceptionData.class);

		Class<? extends Exception> exceptionClass = getExceptionClass(exceptionData.getClassName());
		Exception obj = SilentObjectCreator.create(exceptionClass);

		Map<String, Object> attributes = exceptionData.getAttributes();
		attributes.put("detailMessage", exceptionData.getMessage());

		setObjectAttributes(obj, attributes);

		return obj;
	}

	private void setObjectAttributes(Object obj, Map<String, Object> attributes) {

		for (Map.Entry<String, Object> attribute : attributes.entrySet()) {

			Class<?> objClass = obj.getClass();

			try {

				Field field = null;

				while (objClass != null && field == null) {

					try {
						field = objClass.getDeclaredField(attribute.getKey());

					} catch (NoSuchFieldException e) {

					}
					objClass = objClass.getSuperclass();
				}

				if (field != null) {

					boolean isAccessible = field.isAccessible();
					field.setAccessible(true);

					field.set(obj, attribute.getValue());

					field.setAccessible(isAccessible);
				}

			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private Class<? extends Exception> getExceptionClass(String className) {

		try {
			Class<?> theClass = Class.forName(className);

			if (!Exception.class.isAssignableFrom(theClass)) {
				throw new IllegalStateException("Exception class expected - Got " + className);
			}

			@SuppressWarnings("unchecked")
			Class<? extends Exception> exceptionClass = (Class<? extends Exception>) theClass;

			return exceptionClass;

		} catch (ClassNotFoundException e) {

			throw new IllegalStateException(e);
		}
	}
}