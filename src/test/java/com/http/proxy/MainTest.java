package com.http.proxy;

import java.util.List;

import com.util.ProxyFactoryUtil;

public class MainTest {

	public static void main(String[] args) throws Exception {

		String host = "http://localhost:8080/fuchibol-server";
		ApiProxyFactory apiProxyFactory = ProxyFactoryUtil.createDefault(host);

		Apis apisAPI = apiProxyFactory.getProxy(Apis.class);

		List<String> apis = apisAPI.getApis();

		ApiRevisions apiRevisions;
		apiRevisions = apisAPI.getApiRevisions(apis.get(0));
		int integer = apiRevisions.getRevision().get(0);

		System.out.println("Revision: " + integer);

	}

}
