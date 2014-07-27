package com.http.model;

import java.io.File;

public class FilePayload implements FormPayload {

	private final File payload;

	public FilePayload(File payload) {
		this.payload = payload;
	}

	@Override
	public String getContentType() {
		return "application/octet-stream";
	}

	@Override
	public PayloadType getPayloadType() {
		return PayloadType.INPUT;
	}

	public File getPayload() {
		return payload;
	}
}
