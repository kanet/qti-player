package com.qtitools.player.client.model.responseprocessing;

public enum ResponseProcessorTemplate {
	NONE, MATCH_CORRECT, MATCH_CORRECT_MULTIPLE;
	
	public static ResponseProcessorTemplate fromURI(String uri){
		if (uri.toLowerCase().contains("www.imsglobal.org/question/qti_v2p0/rptemplates/match_correct"))
			return MATCH_CORRECT;
		else if (uri.toLowerCase().contains("www.ydp.eu/qti/rptemplates/match_correct_multiple"))
			return MATCH_CORRECT_MULTIPLE;
		
		
		return MATCH_CORRECT_MULTIPLE;
	}
}
