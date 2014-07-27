package com.http.proxy;

import java.lang.reflect.Type;
import java.util.List;

import com.http.model.HttpMethod;

public class BaseServiceMetadata {

	private HttpMethod method;

	private String url;

	private String produces;

	private List<String> consumes;

	private List<String> pathParams;

	private Type resultType;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProduces() {
		return produces;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public Type getResultType() {
		return resultType;
	}

	public void setResultType(Type resultType) {
		this.resultType = resultType;
	}

	public List<String> getConsumes() {
		return consumes;
	}

	public void setConsumes(List<String> consumes) {
		this.consumes = consumes;
	}

	public List<String> getPathParams() {
		return pathParams;
	}

	public void setPathParams(List<String> pathParams) {
		this.pathParams = pathParams;
	}

}
