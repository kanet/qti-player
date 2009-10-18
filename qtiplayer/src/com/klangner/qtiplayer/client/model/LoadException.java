package com.klangner.qtiplayer.client.model;

public class LoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoadException(String reason){
		super(reason);
	}
}
