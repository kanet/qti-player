package com.qtitools.player.client.model.responseprocessing;

public enum ResponseProcessorTemplate {
	NONE, MATCH_CORRECT;
	
	public static ResponseProcessorTemplate fromURI(String uri){
		if (uri.toLowerCase().endsWith("match_correct"))
			return MATCH_CORRECT;
		
		return MATCH_CORRECT;
	}
}
