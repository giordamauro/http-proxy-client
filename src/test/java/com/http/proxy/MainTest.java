package com.http.proxy;

import java.util.List;

import com.util.ProxyFactoryUtil;

public class MainTest {

	public static void main(String[] args) throws Exception {

		String host = "http://localhost:808/fuchibol-server";
		ApiProxyFactory apiProxyFactory = ProxyFactoryUtil.getDefaultProxyFactory(host);

		Apis apisAPI = apiProxyFactory.getProxy(Apis.class);

		List<String> apis = apisAPI.getApis();

		ApiRevisions apiRevisions;
		apiRevisions = apisAPI.getApiRevisions(apis.get(0));
		int integer = apiRevisions.getRevision().get(0);

		System.out.println("Revision: " + integer);

	}

	// <bean id="exceptionDeserializer"
	// class="com.apigee.proxy.ExceptionJsonDeserializer">
	// <constructor-arg>
	// <util:map>
	// <entry key="messaging.config.beans.ApplicationDoesNotExist"
	// value="com.apigee.model.exceptions.apis.ApiDoesNotExistException"/>
	// </util:map>
	// </constructor-arg>
	// </bean>

}
