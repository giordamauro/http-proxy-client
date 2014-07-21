package com.apigee.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class ExceptionJsonDeserializer implements JsonDeserializer<Exception> {

	private final Map<String, Class<? extends ApigeeApiException>> exceptionsMap;

	public ExceptionJsonDeserializer(Map<String, Class<? extends ApigeeApiException>> exceptionsMap) {
		this.exceptionsMap = exceptionsMap;
	}

	@Override
	public Exception deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject object = json.getAsJsonObject();

		String code = object.get("code").getAsString();
		String message = object.get("message").getAsString();
		JsonElement contextElements = object.get("contexts");
		Type listType = new TypeToken<List<String>>() {
		}.getType();
		List<String> contexts = context.deserialize(contextElements, listType);

		Class<? extends ApigeeApiException> exceptionClass = exceptionsMap.get(code);

		if (exceptionClass == null) {
			throw new IllegalStateException("There isn't an ApigeeApiException registered for code '" + code + "'");
		}

		try {
			Constructor<? extends ApigeeApiException> constructor = exceptionClass.getConstructor(String.class, List.class);
			ApigeeApiException exceptionInstance = constructor.newInstance(message, contexts);

			return exceptionInstance;
		} catch (Exception e) {
			throw new IllegalStateException(String.format("Coudn't invoke constructor class for exception '%s'", exceptionClass), e);
		}
	}
}
