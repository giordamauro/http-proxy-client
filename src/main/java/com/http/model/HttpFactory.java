package com.http.model;

public interface HttpFactory {

	<T extends HttpRequest> T newRequest(HttpMethod method, String path);

	HttpRequest newOptions(String path);

	QueryRequest newGet(String path);

	QueryRequest newDelete(String path);

	FormRequest newPost(String path);

	FormRequest newPut(String path);

	void setHost(String host);

	void setPathParam(String name, String value);

	void setHeader(String name, String value);
}
