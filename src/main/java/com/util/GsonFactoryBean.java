package com.util;

import org.springframework.beans.factory.FactoryBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public class GsonFactoryBean implements FactoryBean<Gson> {

	private final GsonBuilder gs;

	public GsonFactoryBean(GsonBuilder gs) {
		this.gs = gs;

		gs.serializeNulls();
		gs.disableHtmlEscaping();
		gs.setLongSerializationPolicy(LongSerializationPolicy.STRING);
	}

	@Override
	public Gson getObject() throws Exception {
		return gs.create();
	}

	@Override
	public Class<?> getObjectType() {
		return Gson.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
