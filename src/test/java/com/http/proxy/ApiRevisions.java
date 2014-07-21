package com.http.proxy;

import java.util.List;

public class ApiRevisions {

	private String name;
	private List<Integer> revision;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getRevision() {
		return revision;
	}

	public void setRevision(List<Integer> revision) {
		this.revision = revision;
	}

}
