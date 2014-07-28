package com.http.proxy;

import java.lang.reflect.Method;

import javax.validation.ValidationException;

public interface ValidationHandler {

    void validateMethodCall(Class<?> interfaceClass, Method method, Object[] args) throws ValidationException;

}
