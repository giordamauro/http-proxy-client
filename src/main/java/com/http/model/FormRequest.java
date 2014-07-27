package com.http.model;

import java.util.Map;

public interface FormRequest extends QueryRequest {

	void setPayload(FormPayload payload);

	void setPayload(FormPayload payload, String contentType);

	FormPayload getPayload();

	HttpResponse send(Map<String, String> pathParams, RequestParams queryParams, FormPayload payload);

	HttpResponse send(RequestParams queryParams, FormPayload payload);

	HttpResponse send(Map<String, String> pathParams, FormPayload payload);

	HttpResponse send(FormPayload payload);

}
