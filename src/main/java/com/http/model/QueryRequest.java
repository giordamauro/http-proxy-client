package com.http.model;

import java.util.Map;

public interface QueryRequest extends HttpRequest {

	void addQueryParam(String name, String value);

	void addQueryParams(RequestParams queryParams);

	RequestParams getQueryParams();

	HttpResponse send(Map<String, String> pathParams, RequestParams queryParams);

	HttpResponse send(RequestParams queryParams);
}
