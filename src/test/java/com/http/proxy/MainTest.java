package com.http.proxy;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    private static final ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

    private static final ApiProxyFactory apiProxyFactory = context.getBean("apiProxyFactory", ApiProxyFactory.class);

    public static void main(String[] args) {

        Apis apisAPI = apiProxyFactory.getProxy(Apis.class);

        List<String> apis = apisAPI.getApis();

        ApiRevisions apiRevisions;
        apiRevisions = apisAPI.getApiRevisions(apis.get(0));
        int integer = apiRevisions.getRevision().get(0);

        System.out.println("Revision: " + integer);

    }
}
