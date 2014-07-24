package com.fuchibol.client;

import com.fuchibol.api.UserApi;
import com.fuchibol.api.exception.UserNotFoundException;
import com.fuchibol.api.model.User;
import com.http.proxy.ApiProxyFactory;
import com.util.ProxyFactoryUtil;

public class MainTest {

    public static void main(String[] args) {

        String host = "http://localhost:8080/fuchibol-server/v1/rest";

        ClientExceptionDeserializer clientExceptionDeserializer = new ClientExceptionDeserializer();
        ApiProxyFactory apiProxyFactory = ProxyFactoryUtil.getDefaultProxyFactory(host, clientExceptionDeserializer);

        UserApi userApi = apiProxyFactory.getProxy(UserApi.class);

        try {
            User user = userApi.getUser("mgiordas");

            System.out.println("Revision: " + user.getEmail());

        } catch (UserNotFoundException e) {
            throw new IllegalStateException(e.getUsernameOrEmail(), e);
        }
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
