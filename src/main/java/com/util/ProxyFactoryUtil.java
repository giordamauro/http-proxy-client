package com.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.GsonBuilder;
import com.http.impl.httpclient.HttpClientFactory;
import com.http.model.HttpFactory;
import com.http.proxy.ApiMetadataHandler;
import com.http.proxy.ApiMethodHandler;
import com.http.proxy.ApiProxyFactory;
import com.http.proxy.ApiResultHandler;
import com.http.proxy.impl.ApiMetadataHandlerImpl;
import com.http.proxy.impl.ApiMethodHandlerImpl;
import com.http.proxy.impl.ApiResultHandlerImpl;

public final class ProxyFactoryUtil {

	private ProxyFactoryUtil() {

	}

	public static ApiProxyFactory getDefaultProxyFactory(String host) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpFactory httpFactory = new HttpClientFactory(client, host);

			GsonBuilder gsonBuilder = new GsonBuilder();
			GsonFactoryBean gsonFactoryBean = new GsonFactoryBean(gsonBuilder);

			ApiMetadataHandler metadataHandler = new ApiMetadataHandlerImpl();
			ApiResultHandler resultHandler = new ApiResultHandlerImpl(gsonFactoryBean.getObject());

			ApiMethodHandler methodHandler = new ApiMethodHandlerImpl(metadataHandler, httpFactory, resultHandler);

			ApiProxyFactory apiProxyFactory = new ApiProxyFactory(methodHandler);

			return apiProxyFactory;

		} catch (Exception e) {
			throw new IllegalStateException();
		}
	}
}
