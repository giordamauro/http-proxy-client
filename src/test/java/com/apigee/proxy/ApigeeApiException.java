package com.apigee.proxy;

import java.util.List;

public abstract class ApigeeApiException extends Exception {

	private static final long serialVersionUID = 3627368062187504082L;

	private final List<String> contexts;

	public ApigeeApiException(String message, List<String> contexts) {

		super(message);
		this.contexts = contexts;
	}

	public List<String> getContexts() {
		return contexts;
	}

	public String toString() {
		return super.toString() + " - Contexts: " + contexts;
	}
}
