package com.qtitools.player.client.model.feedback;

import java.util.HashMap;
import java.util.Vector;

import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;

public class InlineFeedbackManager implements InlineFeedbackSocket {

	public InlineFeedbackManager(){
		
	}
	
	public Vector<InlineFeedback> feedbacks;

	@Override
	public void add(InlineFeedback inlineFeedback) {
		feedbacks.add(inlineFeedback);
		
	}
	
	public void removeAllFeedbacks(){
		
	}
	

	public void process (HashMap<String, Response> responses, HashMap<String, Outcome> outcomes, String senderIdentifier){
		
	}
}
