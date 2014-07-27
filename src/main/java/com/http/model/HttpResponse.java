package com.http.model;

import java.io.InputStream;
import java.util.Map;

public interface HttpResponse {

	Map<String, String> getHeaders();

	boolean isSuccessful();

	int getStatus();

	InputStream getInputStream();

	String getStringContent();
}
