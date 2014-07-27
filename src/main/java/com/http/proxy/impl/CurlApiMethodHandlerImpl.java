package com.http.proxy.impl;

import java.lang.reflect.Method;
import java.util.Map;

import com.google.gson.Gson;
import com.http.model.FormRequest;
import com.http.model.HttpFactory;
import com.http.model.HttpMethod;
import com.http.model.HttpRequest;
import com.http.model.QueryRequest;
import com.http.model.RawPayload;
import com.http.model.RequestParams;
import com.http.model.curl.CurlLogger;
import com.http.proxy.ApiMetadataHandler;
import com.http.proxy.BaseServiceMetadata;

public class CurlApiMethodHandlerImpl extends AbstractMethodHandler {

	private final ApiMetadataHandler metadataHandler;

	private final HttpFactory requestFactory;

	public CurlApiMethodHandlerImpl(ApiMetadataHandler metadataHandler, HttpFactory requestFactory) {

		if (metadataHandler == null || requestFactory == null) {

			throw new IllegalArgumentException("MetadataHandler and requestFactory cannot be null");
		}
		this.metadataHandler = metadataHandler;
		this.requestFactory = requestFactory;
	}

	public Object handleCall(Method method, Object[] args) {

		BaseServiceMetadata metadata = metadataHandler.getBaseServiceMetadata(method);
		Map<String, String> pathParams = metadataHandler.getPathParams(method, args);

		HttpRequest request = requestFactory.newRequest(metadata.getMethod(), metadata.getUrl());
		request.setPathParams(pathParams);

		if (metadata.getMethod() != HttpMethod.OPTIONS) {
			RequestParams queryParams = metadataHandler.getQueryParams(method, args);
			QueryRequest queryRequest = (QueryRequest) request;
			queryRequest.addQueryParams(queryParams);
		}

		// TODO incorporar otros tipos de Payload!
		if ((metadata.getMethod() == HttpMethod.POST || metadata.getMethod() == HttpMethod.PUT) && metadata.getConsumes().contains("application/json")) {
			if (args.length == 1) {
				String json = null;
				Object argument = args[0];
				if (argument.getClass().isAssignableFrom(String.class)) {
					json = String.valueOf(argument);
				} else {
					json = new Gson().toJson(argument);
				}

				FormRequest formRequest = (FormRequest) request;
				formRequest.setPayload(RawPayload.JSON(json));
			} else {
				throw new IllegalStateException("Json payload request cannot be called with multiple parameters");
			}
		}

		CurlLogger.logRequest(request);

		return null;
	}
}
