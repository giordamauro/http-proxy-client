package com.http.model;

public class RawPayload implements FormPayload {

	private static final String JSON_CONTENT_TYPE = "application/json";

	private static final String XML_CONTENT_TYPE = "text/xml";

	private final String payload;

	private final String contentType;

	private RawPayload(String payload, String contentType) {
		this.payload = payload;
		this.contentType = contentType;
	}

	public static RawPayload JSON(String json) {
		return new RawPayload(json, JSON_CONTENT_TYPE);
	}

	public static RawPayload XML(String xml) {
		return new RawPayload(xml, XML_CONTENT_TYPE);
	}

	public String getContentType() {
		return contentType;
	}

	public PayloadType getPayloadType() {
		return PayloadType.RAW;
	}

	public String getRawPayload() {
		return payload;
	}
}
