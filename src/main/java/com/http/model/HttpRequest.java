package com.http.model;

import java.util.Map;

public interface HttpRequest {

	HttpMethod getMethod();

	String getPath();

	Map<String, String> getPathParams();

	void setHeader(String name, String value);

	void setHeaders(Map<String, String> headers);

	String getHeader(String name);

	Map<String, String> getHeaders();

	void setPathParam(String name, String value);

	void setPathParams(Map<String, String> pathParams);

	HttpResponse send();

	HttpResponse send(Map<String, String> pathParams);
}
