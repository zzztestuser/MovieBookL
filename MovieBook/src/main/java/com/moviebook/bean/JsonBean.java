package com.moviebook.bean;

import com.google.gson.Gson;

public abstract class JsonBean {

	public String toJson() {
		return (new Gson()).toJson(this);
	}
	
}
