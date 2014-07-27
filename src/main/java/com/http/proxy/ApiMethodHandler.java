package com.http.proxy;

import java.lang.reflect.Method;

public interface ApiMethodHandler {

	Object handleMethodCall(Method method, Object[] args) throws Exception;
}
