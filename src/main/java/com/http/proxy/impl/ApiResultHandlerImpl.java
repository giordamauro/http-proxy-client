package com.http.proxy.impl;

import java.lang.reflect.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.http.model.HttpResponse;
import com.http.proxy.ApiResultHandler;

public class ApiResultHandlerImpl implements ApiResultHandler {

	private final Log logger = LogFactory.getLog(this.getClass());

	private static final int HTTP_SUCCESS_CODE = 200;
	private static final int HTTP_CLIENT_ERROR_CODE = 400;
	private static final int HTTP_SERVER_ERRROR_CODE = 500;

	private static final String JSON_CONTENT_TYPE = "application/json";
	private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";

	private final Gson gson;

	public ApiResultHandlerImpl(Gson gson) {
		this.gson = gson;
	}

	public Object getResponseResult(HttpResponse response, String produces, Type returnType) throws Exception {

		if (response == null || produces == null || returnType == null) {
			throw new IllegalArgumentException("Response, produces and returnType cannot be null");
		}

		if (isResponseSuccessful(response.getStatus())) {

			Object result = null;

			if (produces.equals(JSON_CONTENT_TYPE)) {

				String stringResponse = response.getStringContent();
				logger.debug(String.format("Parsing json: '%s'", stringResponse));

				result = getFromJSon(returnType, stringResponse);
			} else if (produces.equals(OCTET_STREAM_CONTENT_TYPE)) {

				result = response.getInputStream();
			} else {
				result = response.getStringContent();
			}
			return result;
		} else {

			int responseStatus = response.getStatus();

			Exception exception = getFromJSon(Exception.class, response.getStringContent());

			if (isClientException(responseStatus)) {

				if (logger.isDebugEnabled()) {
					logger.warn(String.format("CLIENT EXCEPTION - Response code: %s", responseStatus), exception);
				}
			} else if (isServerException(responseStatus)) {

				logger.warn(String.format("SERVER EXCEPTION - Response code: %s", responseStatus), exception);
			}

			throw exception;
		}
	}

	private boolean isResponseSuccessful(int responseCode) {

		return responseCode >= HTTP_SUCCESS_CODE && responseCode < HTTP_SUCCESS_CODE + 100;
	}

	private boolean isClientException(int responseCode) {

		return responseCode >= HTTP_CLIENT_ERROR_CODE && responseCode < HTTP_CLIENT_ERROR_CODE + 100;
	}

	private boolean isServerException(int responseCode) {

		return responseCode >= HTTP_SERVER_ERRROR_CODE && responseCode < HTTP_SERVER_ERRROR_CODE + 100;
	}

	private <T> T getFromJSon(Type returnType, String body) {

		try {
			return gson.fromJson(body, returnType);
		} catch (Exception e) {

			throw new IllegalStateException(String.format("Couln't parse json '%s' to class '%s'", body, returnType), e);
		}
	}
}
