package com.http.impl.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;

import com.http.model.FilePayload;
import com.http.model.FormPayload;
import com.http.model.HttpMethod;
import com.http.model.HttpRequest;
import com.http.model.HttpResponse;
import com.http.model.ParamsUtil;
import com.http.model.PayloadType;
import com.http.model.RawPayload;
import com.http.model.RequestParams;

public class HttpRequester {

	private final HttpClient httpClient;

	HttpRequester(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public HttpRequestBase getRequest(HttpRequest request, Map<String, String> pathParams, RequestParams queryParams, FormPayload payload) {

		HttpMethod method = request.getMethod();
		String completePath = ParamsUtil.replacePathParameters(request.getPath(), pathParams);

		if (queryParams != null && !queryParams.isEmpty()) {
			completePath += "?" + ParamsUtil.getUrlParams(queryParams);
		}

		HttpRequestBase httpRequest = getBaseRequestFromMethod(method, completePath);

		Map<String, String> headers = request.getHeaders();
		for (Entry<String, String> header : headers.entrySet()) {
			httpRequest.setHeader(header.getKey(), header.getValue());
		}

		if (payload != null) {

			setRequestPayload(method, httpRequest, payload);
		}

		return httpRequest;
	}

	public HttpRequestBase getRequest(HttpRequest request, Map<String, String> pathParams, RequestParams queryParams) {
		return getRequest(request, pathParams, queryParams, null);
	}

	public HttpRequestBase getRequest(HttpRequest request, Map<String, String> pathParams) {

		return getRequest(request, pathParams, null);
	}

	public HttpResponse sendFormRequest(HttpRequest request, Map<String, String> pathParams, RequestParams queryParams, FormPayload payload) {
		HttpRequestBase httpRequest = getRequest(request, pathParams, queryParams, payload);

		org.apache.http.HttpResponse response = executeHttpRequest(httpRequest);
		HttpResponse httpResponse = new HttpResponseImpl(response);

		return httpResponse;
	}

	public HttpResponse sendQueryRequest(HttpRequest request, Map<String, String> pathParams, RequestParams queryParams) {
		return sendFormRequest(request, pathParams, queryParams, null);
	}

	public HttpResponse sendHttpRequest(HttpRequest request, Map<String, String> pathParams) {
		return sendQueryRequest(request, pathParams, null);
	}

	private void setRequestPayload(HttpMethod method, HttpRequestBase httpRequest, FormPayload payload) {

		if (method != HttpMethod.POST && method != HttpMethod.PUT) {
			throw new IllegalStateException(String.format("Method '%s' shouldn't have payload", method));
		}

		HttpEntity entity = null;

		HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) httpRequest;

		if (payload.getPayloadType() == PayloadType.RAW) {

			RawPayload rawPayload = (RawPayload) payload;
			String strPayload = rawPayload.getRawPayload();
			entity = getStringEntity(strPayload);

		} else if (payload.getPayloadType() == PayloadType.INPUT) {

			FilePayload inputPayload = (FilePayload) payload;
			File file = inputPayload.getPayload();
			String contentType = inputPayload.getContentType();
			entity = new FileEntity(file, contentType);

		} else {
			throw new UnsupportedOperationException("Not done yet");
		}

		entityRequest.setEntity(entity);

	}

	private StringEntity getStringEntity(String payload) {
		StringEntity params;
		try {
			params = new StringEntity(payload, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
		return params;
	}

	private HttpRequestBase getBaseRequestFromMethod(HttpMethod method, String basePath) {
		HttpRequestBase httpRequest = null;

		URI uri = URI.create(basePath);

		switch (method) {
		case OPTIONS:
			httpRequest = new HttpOptions(uri);
			break;

		case GET:
			httpRequest = new HttpGet(uri);
			break;

		case DELETE:
			httpRequest = new HttpDelete(uri);
			break;

		case POST:
			httpRequest = new HttpPost(uri);
			break;

		case PUT:
			httpRequest = new HttpPut(uri);
			break;

		default:
			throw new IllegalStateException(String.format("Couldn't create a BaseHttpRequest for method '%s'", method));
		}

		return httpRequest;
	}

	private org.apache.http.HttpResponse executeHttpRequest(HttpUriRequest httpRequest) {
		try {
			org.apache.http.HttpResponse response = httpClient.execute(httpRequest);
			return response;
		} catch (ClientProtocolException e) {

			throw new IllegalStateException(e);
		} catch (IOException e) {

			throw new IllegalStateException(e);
		}
	}

}
