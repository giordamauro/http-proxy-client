package com.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.http.impl.httpclient.HttpClientFactory;
import com.http.model.HttpFactory;
import com.http.proxy.ApiMetadataHandler;
import com.http.proxy.ApiMethodHandler;
import com.http.proxy.ApiProxyFactory;
import com.http.proxy.ApiResultHandler;
import com.http.proxy.ValidationHandler;
import com.http.proxy.impl.ApiMetadataHandlerImpl;
import com.http.proxy.impl.ApiMethodHandlerImpl;
import com.http.proxy.impl.ApiResultHandlerImpl;
import com.http.proxy.impl.ValidationHandlerImpl;

public final class ProxyFactoryUtil {

    private ProxyFactoryUtil() {

    }

    public static ApiProxyFactory createDefault(String host) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpFactory httpFactory = new HttpClientFactory(client, host);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            gsonBuilder.disableHtmlEscaping();
            gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
            gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

            ExceptionDeserializer exceptionDeserializer = new ExceptionDeserializer();
            gsonBuilder.registerTypeAdapter(Exception.class, exceptionDeserializer);

            ApiMetadataHandler metadataHandler = new ApiMetadataHandlerImpl();
            ApiResultHandler resultHandler = new ApiResultHandlerImpl(gsonBuilder.create());

            ApiMethodHandler methodHandler = new ApiMethodHandlerImpl(metadataHandler, httpFactory, resultHandler);
            ValidationHandler validationHandler = new ValidationHandlerImpl();

            ApiProxyFactory apiProxyFactory = new ApiProxyFactory(methodHandler, validationHandler);

            return apiProxyFactory;

        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

}
