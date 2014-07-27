package com.http.impl.httpclient;

import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;

import com.http.model.FormPayload;
import com.http.model.FormRequest;
import com.http.model.HttpMethod;
import com.http.model.HttpResponse;
import com.http.model.RequestParams;

public class FormRequestImpl extends QueryRequestImpl implements FormRequest {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	private FormPayload payload;

	FormRequestImpl(HttpMethod method, String path, HttpRequester requester) {
		super(method, path, requester);
	}

	public void setPayload(FormPayload payload) {
		this.payload = payload;
		this.setHeader(CONTENT_TYPE_HEADER, payload.getContentType());
	}

	public void setPayload(FormPayload payload, String contentType) {
		if (contentType == null) {
			throw new IllegalArgumentException("ContentType cannot be null");
		}
		this.payload = payload;
		this.setHeader(CONTENT_TYPE_HEADER, contentType);
	}

	public FormPayload getPayload() {
		return payload;
	}

	@Override
	public HttpResponse send() {
		return requester.sendFormRequest(this, pathParams, queryParams, payload);
	}

	@Override
	public HttpResponse send(Map<String, String> pathParams) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);

		return requester.sendFormRequest(this, pathParameters, queryParams, payload);
	}

	@Override
	public <T extends HttpRequestBase> T getHttpClientRequest() {

		@SuppressWarnings("unchecked")
		T castedRequest = (T) requester.getRequest(this, pathParams, queryParams, payload);

		return castedRequest;
	}

	@Override
	public HttpResponse send(Map<String, String> pathParams, RequestParams queryParams) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);
		RequestParams queryBasedParams = getQueryBasedParams(queryParams);

		return requester.sendFormRequest(this, pathParameters, queryBasedParams, payload);
	}

	@Override
	public HttpResponse send(RequestParams queryParams) {

		RequestParams queryBasedParams = getQueryBasedParams(queryParams);

		return requester.sendQueryRequest(this, pathParams, queryBasedParams);
	}

	public HttpResponse send(Map<String, String> pathParams, RequestParams queryParams, FormPayload formPayload) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);
		RequestParams queryBasedParams = getQueryBasedParams(queryParams);
		setPayload(formPayload);

		return requester.sendFormRequest(this, pathParameters, queryBasedParams, payload);

	}

	public HttpResponse send(RequestParams queryParams, FormPayload formPayload) {

		RequestParams queryBasedParams = getQueryBasedParams(queryParams);
		setPayload(formPayload);

		return requester.sendFormRequest(this, pathParams, queryBasedParams, payload);
	}

	public HttpResponse send(Map<String, String> pathParams, FormPayload formPayload) {

		Map<String, String> pathParameters = getPathBaseParams(pathParams);
		setPayload(formPayload);

		return requester.sendFormRequest(this, pathParameters, queryParams, payload);
	}

	public HttpResponse send(FormPayload formPayload) {

		setPayload(formPayload);

		return requester.sendFormRequest(this, pathParams, queryParams, payload);
	}

}
