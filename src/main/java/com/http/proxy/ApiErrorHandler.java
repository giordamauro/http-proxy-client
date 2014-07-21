package com.http.proxy;

public interface ApiErrorHandler<T> {

	Class<T> getErrorType();

	void throwException(Object errorObject) throws Exception;
}
