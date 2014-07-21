package com.http.impl.httpclient;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.http.model.FormRequest;
import com.http.model.HttpFactory;
import com.http.model.HttpMethod;
import com.http.model.HttpRequest;
import com.http.model.QueryRequest;

public class HttpClientFactory implements HttpFactory {

	private final HttpRequester requester;

	private String hostPath;

	private final Map<String, String> headers = new HashMap<String, String>();

	private final Map<String, String> pathParams = new HashMap<String, String>();

	public HttpClientFactory(HttpClient httpClient, String host) {
		if (httpClient == null) {
			throw new IllegalArgumentException("HttpClient cannot be null");
		}
		this.requester = new HttpRequester(httpClient);
		this.hostPath = host;
	}

	public HttpClientFactory(HttpClient httpClient) {
		this(httpClient, null);
	}

	public <T extends HttpRequest> T newRequest(HttpMethod method, String path) {
		if (method == null || path == null) {
			throw new IllegalArgumentException("Method and path cannot be null");
		}

		String completePath = getCompletePath(path);

		HttpRequest request = null;

		if (method == HttpMethod.OPTIONS) {
			request = new HttpRequestImpl(method, completePath, requester);

		} else if (method == HttpMethod.GET || method == HttpMethod.DELETE) {

			request = new QueryRequestImpl(method, completePath, requester);
		} else {

			request = new FormRequestImpl(method, completePath, requester);
		}
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		@SuppressWarnings("unchecked")
		T castedRequest = (T) request;

		return castedRequest;
	}

	public HttpClient getHttpClient() {
		return requester.getHttpClient();
	}

	public HttpRequest newOptions(String path) {
		HttpRequest request = new HttpRequestImpl(HttpMethod.OPTIONS, getCompletePath(path), requester);
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		return request;
	}

	public QueryRequest newGet(String path) {
		QueryRequest request = new QueryRequestImpl(HttpMethod.GET, getCompletePath(path), requester);
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		return request;
	}

	public QueryRequest newDelete(String path) {
		QueryRequest request = new QueryRequestImpl(HttpMethod.DELETE, getCompletePath(path), requester);
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		return request;
	}

	public FormRequest newPost(String path) {
		FormRequest request = new FormRequestImpl(HttpMethod.POST, getCompletePath(path), requester);
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		return request;
	}

	public FormRequest newPut(String path) {
		FormRequest request = new FormRequestImpl(HttpMethod.PUT, getCompletePath(path), requester);
		request.setHeaders(headers);
		request.setPathParams(pathParams);

		return request;
	}

	public void setHost(String host) {
		this.hostPath = host;
	}

	public void setPathParam(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		this.pathParams.put(name, value);
	}

	public void setHeader(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		this.headers.put(name, value);
	}

	private String getCompletePath(String requestPath) {
		String completePath = "";
		if (hostPath != null) {
			completePath += hostPath;
		}
		completePath += requestPath;
		return completePath;
	}

}
