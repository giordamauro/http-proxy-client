package com.http.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import com.http.model.RequestParams;

public interface ApiMetadataHandler {

	BaseServiceMetadata getBaseServiceMetadata(Method method);

	Map<String, String> getPathParams(Method method, Object[] args);

	RequestParams getQueryParams(Method method, Object[] args);

	RequestParams getFormParams(Method method, Object[] args);
}
