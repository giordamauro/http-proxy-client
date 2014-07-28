package com.http.proxy;

import java.lang.reflect.Method;

public interface ValidationHandler {

	void validateMethodCall(Class<?> interfaceClass, Method method, Object[] args) throws Exception;

}
