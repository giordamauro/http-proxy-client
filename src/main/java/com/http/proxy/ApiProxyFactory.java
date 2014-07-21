package com.http.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ApiProxyFactory {

	private final Map<Class<?>, Object> proxyObjects = new HashMap<Class<?>, Object>();
	
	private final InvocationHandler invocationHandler;
	
	public ApiProxyFactory(final ApiMethodHandler apiHandler) {

		if (apiHandler == null) {

			throw new IllegalArgumentException("Api handler cannot be null");
		}

		this.invocationHandler = new InvocationHandler() {

			public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

				return apiHandler.handleMethodCall(method, args);
			}
		};
	}

	public <T> T getProxy(Class<T> interfaceClass) {

		Object proxyObject = proxyObjects.get(interfaceClass);
		
		if(proxyObject == null){
		
			ClassLoader classLoader = interfaceClass.getClassLoader();
	
			Class<?>[] implClass = new Class<?>[] { interfaceClass };
	
			proxyObject = Proxy.newProxyInstance(classLoader, implClass, invocationHandler);
		
			proxyObjects.put(interfaceClass, proxyObject); 
		}
		
		@SuppressWarnings("unchecked")
		T proxy = (T) proxyObject;
		
		return proxy;
	}
}