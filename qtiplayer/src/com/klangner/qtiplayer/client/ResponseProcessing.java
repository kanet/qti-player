package com.klangner.qtiplayer.client;

import com.klangner.qtiplayer.client.model.AssessmentItem;

public class ResponseProcessing {

	/** Model */
	private AssessmentItem assessmentItem;
	
	/**
	 * constructor
	 * @param item associated with this processing
	 */
	public ResponseProcessing(AssessmentItem item){
		assessmentItem = item;
	}
	
	public String getFeedback(){
		return "There is no feedback yet.";
	}
}
