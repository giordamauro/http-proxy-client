package com.http.proxy;

import java.lang.reflect.Type;

import com.http.model.HttpResponse;

public interface ApiResultHandler {

	Object getResponseResult(HttpResponse response, String produces, Type returnType) throws Exception;
}
