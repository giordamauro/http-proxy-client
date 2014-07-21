package com.util;

import org.springframework.beans.factory.FactoryBean;

import com.http.proxy.ApiProxyFactory;

public class ProxyFactoryBean<T> implements FactoryBean<T> {

	private final ApiProxyFactory proxyFactory;

	private final Class<T> factoryClass;

	public ProxyFactoryBean(ApiProxyFactory proxyFactory, Class<T> factoryClass) {
		this.proxyFactory = proxyFactory;
		this.factoryClass = factoryClass;
	}

	@Override
	public T getObject() throws Exception {
		return proxyFactory.getProxy(factoryClass);
	}

	@Override
	public Class<?> getObjectType() {
		return factoryClass;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
