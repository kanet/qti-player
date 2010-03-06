package com.qtitools.player.client.model.responseprocessing;

import java.util.HashMap;
import java.util.Vector;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;

public final class ResponseProcessor {

	public ResponseProcessor(NodeList responseProcessingNode){
		@SuppressWarnings("unused")
		Node templateNode = responseProcessingNode.item(0).getAttributes().getNamedItem("template");
		
		template = ResponseProcessorTemplate.MATCH_CORRECT;
		/*
		if (templateNode != null)
			template = ResponseProcessorTemplate.fromURI(templateNode.getNodeValue());
		else 
			template = ResponseProcessorTemplate.MATCH_CORRECT;
			*/
	}
	
	private ResponseProcessorTemplate template = ResponseProcessorTemplate.NONE;
	
	public void process (HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		if (outcomes.size() == 0 ||  responses.size() == 0)
			return;
		
		try {
			if (template == ResponseProcessorTemplate.MATCH_CORRECT)
				processTemplateMatchCorrect(responses, outcomes);
		} catch (Exception e) {
			
		}
	}
	
	private void processTemplateMatchCorrect(HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		
		Vector<String> correctAnswers = responses.get("RESPONSE").correctAnswers;
		
		Vector<String> userAnswers = responses.get("RESPONSE").values;
		
		boolean answerFound;
		boolean passed = true;
		
		for (int correct = 0 ; correct < correctAnswers.size() ; correct ++){
			
			answerFound = false;
			
			for (int user = 0 ; user < userAnswers.size() ; user ++){
				if (correctAnswers.get(correct).compareTo(userAnswers.get(user)) == 0){
					answerFound = true;
					break;
				}
			}
			
			if (!answerFound){
				passed = false;
			}
		}
		
		outcomes.get("SCORE").values.clear();
		
		if (passed)
			outcomes.get("SCORE").values.add("1");
		else
			outcomes.get("SCORE").values.add("0");
		
		
	}
	
	/*
	public Response getResponse(String responseID){
		return responses.get(responseID);
	}
	
	public Result getResult(){

		Result result = new Result();
		
		for(Response response: responses.values()){
			result.merge(response.getResult());
		}
		
		return result;
	}
	
	public void reset(){

		for(Response response: responses.values())
			response.reset();
	}
	*/
}
