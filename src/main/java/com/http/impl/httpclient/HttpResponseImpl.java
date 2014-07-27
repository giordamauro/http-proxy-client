package com.http.impl.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.http.model.HttpResponse;

public class HttpResponseImpl implements HttpResponse {

	final org.apache.http.HttpResponse response;

	HttpResponseImpl(org.apache.http.HttpResponse response) {
		this.response = response;
	}

	public Map<String, String> getHeaders() {
		Map<String, String> map = new HashMap<String, String>();

		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			map.put(header.getName(), header.getValue());
		}
		return map;
	}

	public boolean isSuccessful() {
		int code = getStatus();
		return (code >= 200 && code < 300);
	}

	public int getStatus() {
		return response.getStatusLine().getStatusCode();
	}

	public InputStream getInputStream() {
		InputStream inputStream;
		try {
			inputStream = response.getEntity().getContent();
		} catch (Exception e) {
			throw new IllegalStateException("Couldn't get entity from response", e);
		}

		return inputStream;
	}

	public String getStringContent() {
		try {
			String content = "";
			if (response.getEntity() != null) {
				content = EntityUtils.toString(response.getEntity());
			}
			if (!isSuccessful()) {
				if (content == null || content.equals("")) {
					content = response.getStatusLine().getReasonPhrase();
				}
			}
			return content;
		} catch (ParseException e) {
			throw new IllegalStateException("Couldn't parse HttpResponse to String", e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String toString() {
		return getStringContent();
	}

	public org.apache.http.HttpResponse getClientResponse() {
		return response;
	}
}
