package com.http.impl.httpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpRequestBase;

import com.http.model.HttpMethod;
import com.http.model.HttpRequest;
import com.http.model.HttpResponse;

public class HttpRequestImpl implements HttpRequest {

	protected final HttpRequester requester;

	private final HttpMethod method;

	private final String path;

	private final Map<String, String> headers = new HashMap<String, String>();

	protected final Map<String, String> pathParams = new HashMap<String, String>();

	HttpRequestImpl(HttpMethod method, String path, HttpRequester requester) {
		this.method = method;
		this.path = path;
		this.requester = requester;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getPathParams() {
		return pathParams;
	}

	public void setHeader(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		headers.put(name, value);
	}

	public void setHeaders(Map<String, String> headers) {
		if (headers == null) {
			throw new IllegalArgumentException("Headers cannot be null");
		}
		this.headers.putAll(headers);
	}

	public String getHeader(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		if (!headers.containsKey(name)) {
			throw new IllegalStateException(String.format("Header named '%s' doesn't exist", name));
		}
		return headers.get(name);
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	public void setPathParam(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		pathParams.put(name, value);
	}

	public void setPathParams(Map<String, String> pathParams) {
		if (pathParams == null) {
			throw new IllegalArgumentException("PathParams cannot be null");
		}
		this.pathParams.putAll(pathParams);
	}

	public HttpResponse send() {
		return requester.sendHttpRequest(this, pathParams);
	}

	public HttpResponse send(Map<String, String> pathParams) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);

		return requester.sendHttpRequest(this, pathParameters);
	}

	public <T extends HttpRequestBase> T getHttpClientRequest() {

		@SuppressWarnings("unchecked")
		T castedRequest = (T) requester.getRequest(this, pathParams);

		return castedRequest;
	}

	protected Map<String, String> getPathBaseParams(Map<String, String> params) {

		Map<String, String> completeParams = new HashMap<String, String>();
		if (params != null) {
			completeParams.putAll(params);
		}
		for (Entry<String, String> baseParam : this.pathParams.entrySet()) {
			if (!completeParams.containsKey(baseParam.getKey())) {
				completeParams.put(baseParam.getKey(), baseParam.getValue());
			}
		}
		return completeParams;
	}
}
