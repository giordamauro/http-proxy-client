package com.http.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ParamsUtil {

	private static final String PARAMETER_PREFIX = "{";
	private static final String PARAMETER_SUFIX = "}";

	private ParamsUtil() {
	}

	public static List<String> getPathParameters(String path) {
		List<String> pathParams = new ArrayList<String>();

		String[] splittedPath = path.split("/");
		for (String splitPart : splittedPath) {
			if (splitPart.startsWith(PARAMETER_PREFIX) && splitPart.endsWith(PARAMETER_SUFIX)) {

				String parameterName = splitPart.substring(1, splitPart.length() - 1);
				pathParams.add(parameterName);
			}
		}
		return pathParams;
	}

	public static String replacePathParameters(String path, Map<String, String> params) {

		String pathResult = path;

		List<String> pathParams = getPathParameters(path);

		for (String pathParam : pathParams) {
			String urlEncodedValue = getUrlEncodedValue(pathParam, params);
			pathResult = pathResult.replaceAll("\\" + PARAMETER_PREFIX + pathParam + "\\" + PARAMETER_SUFIX, urlEncodedValue);
		}

		return pathResult;
	}

	private static String getUrlEncodedValue(String parameterName, Map<String, String> params) {
		if (!params.containsKey(parameterName)) {
			throw new IllegalStateException(String.format("Path parameter named '%s' is required", parameterName));
		}
		String value = params.get(parameterName);
		String urlEncodedValue;
		try {
			urlEncodedValue = URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(String.format("Couldn't url encode value '%s' for parameter named '%s'", value, parameterName), e);
		}
		return urlEncodedValue;
	}

	public static String getUrlParams(RequestParams queryParams) {
		try {

			String urlQueryParams = "";
			List<String> queryParameters = queryParams.getParameterKeys();
			for (String queryParam : queryParameters) {
				String urlKey = URLEncoder.encode(queryParam, "UTF-8");
				List<String> values = queryParams.getParameterValues(queryParam);
				for (String value : values) {
					String urlValue = URLEncoder.encode(value, "UTF-8");
					urlQueryParams += urlKey + "=" + urlValue + "&";
				}
			}
			String fixedQueryUrl = urlQueryParams.substring(0, urlQueryParams.length() - 1);

			return fixedQueryUrl;

		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
