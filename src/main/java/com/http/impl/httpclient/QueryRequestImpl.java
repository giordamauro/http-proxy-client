package com.http.impl.httpclient;

import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;

import com.http.model.HttpMethod;
import com.http.model.HttpResponse;
import com.http.model.QueryRequest;
import com.http.model.RequestParams;

public class QueryRequestImpl extends HttpRequestImpl implements QueryRequest {

	protected final RequestParams queryParams = new RequestParams();

	QueryRequestImpl(HttpMethod method, String path, HttpRequester requester) {
		super(method, path, requester);
	}

	public void addQueryParam(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		queryParams.addParameter(name, value);
	}

	public void addQueryParams(RequestParams params) {
		if (params == null) {
			throw new IllegalArgumentException("Params cannot be null");
		}
		queryParams.addAll(params);
	}

	@Override
	public HttpResponse send() {
		return requester.sendQueryRequest(this, pathParams, queryParams);
	}

	@Override
	public HttpResponse send(Map<String, String> pathParams) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);

		return requester.sendQueryRequest(this, pathParameters, queryParams);
	}

	@Override
	public <T extends HttpRequestBase> T getHttpClientRequest() {

		@SuppressWarnings("unchecked")
		T castedRequest = (T) requester.getRequest(this, pathParams, queryParams);

		return castedRequest;
	}

	public HttpResponse send(Map<String, String> pathParams, RequestParams queryParams) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);
		return requester.sendQueryRequest(this, pathParameters, queryParams);
	}

	public HttpResponse send(RequestParams queryParams) {

		RequestParams queryBasedParams = getQueryBasedParams(queryParams);

		return requester.sendQueryRequest(this, pathParams, queryBasedParams);
	}

	public RequestParams getQueryBasedParams(RequestParams queryParams) {
		RequestParams queryBasedParams = new RequestParams();
		queryBasedParams.addAll(this.queryParams);
		queryBasedParams.addAll(queryParams);

		return queryBasedParams;
	}

	@Override
	public RequestParams getQueryParams() {
		RequestParams queryP = new RequestParams();
		queryP.addAll(this.queryParams);

		return queryP;
	}

}
