package com.klangner.qtiplayer.client.module;

/**
 * Socket interface for modules
 * @author Krzysztof Langner
 */
public interface IModuleSocket {

	/** Get access to response */
	public IResponse getResponse(String id);
	
}
