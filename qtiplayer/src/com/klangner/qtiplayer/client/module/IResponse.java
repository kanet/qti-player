package com.klangner.qtiplayer.client.module;

public interface IResponse {

	/** Set key value */
	void set(String key);
	
	/** reset key */
	void unset(String key);
	
	/**
	 * check if given key should be set
	 * @param key
	 * @return true if key is correct answer
	 */
	public boolean isCorrectAnswer(String key);
}
