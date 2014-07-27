package com.http.proxy.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.http.model.HttpMethod;
import com.http.model.RequestParams;
import com.http.proxy.ApiMetadataHandler;
import com.http.proxy.BaseServiceMetadata;
import com.http.proxy.ValueProvider;

public class ApiMetadataHandlerImpl implements ApiMetadataHandler {

	@Override
	public BaseServiceMetadata getBaseServiceMetadata(Method method) {
		if (method == null) {
			throw new IllegalArgumentException("Method cannot be null");
		}
		HttpMethod verb = getServiceMethod(method);
		String baseUrl = getBaseUrl(method);
		Type resultType = method.getGenericReturnType();
		List<String> consumes = getConsumes(method);
		String produces = getProduces(method);

		Map<PathParam, Class<?>> pathParams = getAnnotationParams(method, PathParam.class);
		List<String> pathParamsList = getPathParameters(pathParams);

		BaseServiceMetadata baseServiceMetadata = new BaseServiceMetadata();
		baseServiceMetadata.setMethod(verb);
		baseServiceMetadata.setUrl(baseUrl);
		baseServiceMetadata.setResultType(resultType);
		baseServiceMetadata.setConsumes(consumes);
		baseServiceMetadata.setProduces(produces);
		baseServiceMetadata.setPathParams(pathParamsList);

		return baseServiceMetadata;
	}

	@Override
	public Map<String, String> getPathParams(Method method, Object[] args) {

		Map<String, String> map = new HashMap<String, String>();
		if (args != null) {
			Annotation[][] annot = method.getParameterAnnotations();

			for (int i = 0; i < args.length; i++) {

				PathParam paramAnn = getAnnotation(annot[i], PathParam.class);
				if (paramAnn != null) {
					String key = paramAnn.value();
					Object value = args[i];
					map.put(key, getParameterValue(value));
				}
			}
		}
		return map;
	}

	@Override
	public RequestParams getQueryParams(Method method, Object[] args) {

		RequestParams params = new RequestParams();

		if (args != null) {
			Annotation[][] annot = method.getParameterAnnotations();

			for (int i = 0; i < args.length; i++) {

				QueryParam paramAnn = getAnnotation(annot[i], QueryParam.class);
				Object objValue = args[i];

				if (paramAnn != null) {
					String key = paramAnn.value();
					params.addParameter(key, getParameterValue(objValue));
				}
			}
		}
		return params;
	}

	@Override
	public RequestParams getFormParams(Method method, Object[] args) {

		RequestParams params = new RequestParams();
		if (args != null) {

			Annotation[][] annot = method.getParameterAnnotations();

			for (int i = 0; i < args.length; i++) {

				FormParam paramAnn = getAnnotation(annot[i], FormParam.class);
				Object objValue = args[i];

				if (paramAnn != null && objValue != null) {

					String key = paramAnn.value();
					params.addParameter(key, getParameterValue(objValue));
				}
			}
		}
		return params;

	}

	private HttpMethod getServiceMethod(Method apiMethod) {

		HttpMethod method = null;

		if (apiMethod.isAnnotationPresent(GET.class)) {
			method = HttpMethod.GET;
		} else if (apiMethod.isAnnotationPresent(POST.class)) {
			method = HttpMethod.POST;
		} else if (apiMethod.isAnnotationPresent(DELETE.class)) {
			method = HttpMethod.DELETE;
		} else if (apiMethod.isAnnotationPresent(PUT.class)) {
			method = HttpMethod.PUT;
		} else if (apiMethod.isAnnotationPresent(OPTIONS.class)) {
			method = HttpMethod.OPTIONS;
		}
		if (method != null) {
			return method;
		} else {
			throw new IllegalStateException(String.format("Method '%s' isn´t annotated with GET, POST or DELETE", apiMethod));
		}
	}

	private String getBaseUrl(Method apiMethod) {

		Class<?> apiClass = apiMethod.getDeclaringClass();

		if (!apiClass.isAnnotationPresent(Path.class)) {
			throw new IllegalStateException(String.format("Class '%s' isn't annotated with Path", apiClass));
		}
		Path path = apiClass.getAnnotation(Path.class);
		String baseUrl = path.value();

		if (apiMethod.isAnnotationPresent(Path.class)) {
			Path methodPath = apiMethod.getAnnotation(Path.class);
			baseUrl += methodPath.value();
		}
		return baseUrl;
	}

	private String getProduces(Method method) {

		Produces producesAnn = method.getAnnotation(Produces.class);

		String produces = null;

		if (producesAnn != null) {

			String[] array = producesAnn.value();
			if (array.length == 1) {
				produces = array[0];
			} else {
				throw new IllegalStateException(String.format("Multiple mime types produced not supported: '%s'", Arrays.asList(array)));
			}
		} else {
			produces = "text/plain";
		}

		return produces;
	}

	private List<String> getConsumes(Method method) {

		Consumes consumes = method.getAnnotation(Consumes.class);

		List<String> consumesValue = Collections.emptyList();

		if (consumes != null) {
			String[] array = consumes.value();
			consumesValue = Arrays.asList(array);
		}

		return consumesValue;
	}

	private List<String> getPathParameters(Map<PathParam, Class<?>> pathParams) {

		List<String> list = new ArrayList<String>();

		for (PathParam pathAnn : pathParams.keySet()) {

			list.add(pathAnn.value());
		}
		return list;
	}

	private <T extends Annotation> Map<T, Class<?>> getAnnotationParams(Method method, Class<T> paramAnnClass) {

		Map<T, Class<?>> params = new HashMap<T, Class<?>>();

		Annotation[][] annot = method.getParameterAnnotations();
		Class<?>[] parameters = method.getParameterTypes();

		for (int i = 0; i < parameters.length; i++) {

			T paramAnn = getAnnotation(annot[i], paramAnnClass);
			if (paramAnn != null) {
				params.put(paramAnn, parameters[i]);
			}
		}
		return params;
	}

	private <T extends Annotation> T getAnnotation(Annotation[] array, Class<T> annClass) {

		T annotation = null;
		int i = 0;
		while (i < array.length && annotation == null) {

			Annotation ann = array[i];
			if (annClass.isAssignableFrom(ann.getClass())) {
				@SuppressWarnings("unchecked")
				T annot = (T) ann;

				annotation = annot;
			} else {
				i++;
			}
		}
		return annotation;
	}

	private String getParameterValue(Object source) {
		Object sourceObject = source;
		if (source instanceof ValueProvider) {
			ValueProvider valueProvider = (ValueProvider) source;
			sourceObject = valueProvider.getValue();
		}
		String value = String.valueOf(sourceObject);

		return value;
	}
}
