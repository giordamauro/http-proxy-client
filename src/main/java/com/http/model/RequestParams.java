package com.http.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestParams {

	private final Map<String, List<String>> params = new HashMap<String, List<String>>();

	public RequestParams() {
	}

	public void addParameter(String name, String value) {
		if (name == null || value == null) {
			throw new IllegalArgumentException("Name and value cannot be null");
		}
		if (params.containsKey(name)) {
			List<String> values = params.get(name);
			values.add(value);
		} else {
			List<String> values = new ArrayList<String>();
			values.add(value);
			params.put(name, values);
		}
	}

	public void addParameterValues(String name, List<String> values) {
		if (name == null || values == null) {
			throw new IllegalArgumentException("Name and values cannot be null");
		}
		if (params.containsKey(name)) {
			List<String> paramValues = params.get(name);
			paramValues.addAll(values);
		} else {
			List<String> paramValues = new ArrayList<String>();
			paramValues.addAll(values);
			params.put(name, paramValues);
		}
	}

	public List<String> getParameterValues(String name) {
		if (!containsParameter(name)) {
			throw new IllegalStateException(String.format("Parameter named '%s' doesn't exist", name));
		}
		return Collections.unmodifiableList(params.get(name));
	}

	public String getParameterValue(String name) {
		List<String> values = getParameterValues(name);
		if (values.size() != 1) {
			throw new IllegalStateException(String.format("Parameter name '%s' has more than one value", name));
		}
		return values.get(0);
	}

	public boolean containsParameter(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		return params.containsKey(name);
	}

	public List<String> getParameterKeys() {
		ArrayList<String> paramters = new ArrayList<String>(params.keySet());
		return paramters;
	}

	public void addAll(RequestParams requestParams) {
		List<String> keys = requestParams.getParameterKeys();
		for (String paramKey : keys) {
			List<String> values = requestParams.getParameterValues(paramKey);
			addParameterValues(paramKey, values);
		}
	}

	public boolean isEmpty() {
		return params.isEmpty();
	}
}
