package com.klangner.qtiplayer.client.modules;

public interface IResponse {

	/** Set key value */
	void set(String key, String value);
	
	/** reset key */
	void unset(String key);
}
