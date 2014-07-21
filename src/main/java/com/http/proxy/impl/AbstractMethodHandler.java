package com.http.proxy.impl;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.http.proxy.ApiMethodHandler;

public abstract class AbstractMethodHandler implements ApiMethodHandler {

	private Log logger = LogFactory.getLog(getClass());

	protected Log getLogger() {
		return logger;
	}

	protected abstract Object handleCall(Method method, Object[] args) throws Exception;

	public Object handleMethodCall(Method method, Object[] args) throws Exception {

		if (!method.getDeclaringClass().equals(Object.class)) {

			logger.info(String.format("Invoking service method: '%s.%s(%s)'", method.getDeclaringClass().getSimpleName(), method.getName(), getMethodTypesLog(method.getParameterTypes())));

			return handleCall(method, args);
		} else {
			return method.invoke(this, args);
		}
	}

	private String getMethodTypesLog(Class<?>[] parameterTypes) {

		String types = "";

		for (Class<?> parameterClass : parameterTypes) {

			types += parameterClass.getSimpleName() + ", ";
		}
		if (parameterTypes.length > 0) {
			types = types.substring(0, types.length() - 2);
		}
		return types;
	}
}
