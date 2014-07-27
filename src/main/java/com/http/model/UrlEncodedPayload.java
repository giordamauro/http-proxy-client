package com.http.model;

import javax.ws.rs.core.MediaType;

public class UrlEncodedPayload implements FormPayload {

	private final RequestParams formParams;

	public UrlEncodedPayload(RequestParams formParams) {
		this.formParams = formParams;
	}

	@Override
	public String getContentType() {
		return MediaType.APPLICATION_FORM_URLENCODED;
	}

	@Override
	public PayloadType getPayloadType() {
		return PayloadType.URL_ENCODED;
	}

	public RequestParams getFormParams() {
		return formParams;
	}
}
