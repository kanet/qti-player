package com.qtitools.player.client.model.responseprocessing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.Cardinality;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;

public final class ResponseProcessor {

	public ResponseProcessor(NodeList responseProcessingNode){
		Node templateNode = null;
		try {
			templateNode = responseProcessingNode.item(0).getAttributes().getNamedItem("template");
		} catch (Exception e) {}
		
		//template = ResponseProcessorTemplate.MATCH_CORRECT;
		
		if (templateNode != null)
			template = ResponseProcessorTemplate.fromURI(templateNode.getNodeValue());
		else 
			template = ResponseProcessorTemplate.MATCH_CORRECT_MULTIPLE;
			
	}
	
	private ResponseProcessorTemplate template = ResponseProcessorTemplate.NONE;
	
	public void process (HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		if (outcomes.size() == 0 ||  responses.size() == 0)
			return;
		
		try {
			if (template == ResponseProcessorTemplate.MATCH_CORRECT)
				processTemplateMatchCorrect(responses, outcomes);
			else if (template == ResponseProcessorTemplate.MATCH_CORRECT_MULTIPLE)
				processTemplateMatchCorrectMultiple(responses, outcomes);
		} catch (Exception e) {
			
		}
	}
	
	private void processTemplateMatchCorrect(HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		
		boolean result = processMatchCorrect(responses.get("RESPONSE"));
		
		outcomes.get("SCORE").values.clear();
		
		if (result)
			outcomes.get("SCORE").values.add("1");
		else
			outcomes.get("SCORE").values.add("0");
		
	}
	
	private void processTemplateMatchCorrectMultiple(HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		
		Integer points = 0;
		String currKey;

		Iterator<String> iter = responses.keySet().iterator();
		boolean passed;
		while (iter.hasNext()){
			currKey = iter.next();
			passed = processMatchCorrect(responses.get(currKey));
				
			if (passed)
				points++;
				
			// MAKRO PROCESSING
			if (outcomes.containsKey(currKey+"-SCORE")){
				outcomes.get(currKey+"-SCORE").values.clear();
				if (passed)
					outcomes.get(currKey+"-SCORE").values.add("1");
				else
					outcomes.get(currKey+"-SCORE").values.add("0");
			}
			if (outcomes.containsKey(currKey+"-SCOREHISTORY")){
				if (passed)
					outcomes.get(currKey+"-SCOREHISTORY").values.add("1");
				else
					outcomes.get(currKey+"-SCOREHISTORY").values.add("0");
			}
			if (outcomes.containsKey(currKey+"-SCORECHANGES")  &&  outcomes.containsKey(currKey+"-SCOREHISTORY")){
				if (outcomes.get(currKey+"-SCOREHISTORY").values.size() == 1) {
					outcomes.get(currKey+"-SCORECHANGES").values.add(outcomes.get(currKey+"-SCOREHISTORY").values.get(0));
				} else {
					int currModuleScore = Integer.parseInt( outcomes.get(currKey+"-SCOREHISTORY").values.get(outcomes.get(currKey+"-SCOREHISTORY").values.size()-1) );
					int prevModuleScore = Integer.parseInt( outcomes.get(currKey+"-SCOREHISTORY").values.get(outcomes.get(currKey+"-SCOREHISTORY").values.size()-2) );
					outcomes.get(currKey+"-SCORECHANGES").values.add( String.valueOf(currModuleScore - prevModuleScore) );
				}
			}
			if (outcomes.containsKey(currKey+"-PREVIOUS")  &&  outcomes.containsKey(currKey+"-LASTCHANGE")){
				outcomes.get(currKey+"-LASTCHANGE").values = TemplateMatchCorrectMultiplePerformer.getDifference(responses.get(currKey).values, outcomes.get(currKey+"-PREVIOUS").values);
			}
			if (outcomes.containsKey(currKey+"-PREVIOUS")){
				outcomes.get(currKey+"-PREVIOUS").values.clear();
				for (int a = 0 ; a < responses.get(currKey).values.size() ; a ++ ){
					outcomes.get(currKey+"-PREVIOUS").values.add(responses.get(currKey).values.get(a));
				}
			}
		}

		outcomes.get("SCORE").values.clear();
		
		outcomes.get("SCORE").values.add(points.toString());
		
		if (outcomes.containsKey("SCOREHISTORY")){
			outcomes.get("SCOREHISTORY").values.add(points.toString());
		}
		if (outcomes.containsKey("SCOREHISTORY")  &&  outcomes.containsKey("SCORECHANGES")){
			if (outcomes.get("SCOREHISTORY").values.size() == 1) {
				outcomes.get("SCORECHANGES").values.add(outcomes.get("SCOREHISTORY").values.get(0));
			} else {
				int currModuleScore = Integer.parseInt( outcomes.get("SCOREHISTORY").values.get(outcomes.get("SCOREHISTORY").values.size()-1) );
				int prevModuleScore = Integer.parseInt( outcomes.get("SCOREHISTORY").values.get(outcomes.get("SCOREHISTORY").values.size()-2) );
				outcomes.get("SCORECHANGES").values.add( String.valueOf(currModuleScore - prevModuleScore) );
			}
		}
		
		// MACRO PROCESSING
		
		iter = responses.keySet().iterator();
		while (iter.hasNext()){
			currKey = iter.next();
			
		}
		
	}
	
	private boolean processMatchCorrect(Response response ){
		
		Vector<String> correctAnswers = response.correctAnswers;
		
		Vector<String> userAnswers = response.values;
		
		boolean answerFound;
		boolean passed = true;
		
		if (response.cardinality == Cardinality.ORDERED){
			if (correctAnswers.size() != userAnswers.size()) {
				passed = false;
			} else{
				for (int correct = 0 ; correct < correctAnswers.size() ; correct ++){
					if (correctAnswers.get(correct).compareTo(userAnswers.get(correct)) != 0){
						passed = false;
						break;
					}
				}
			}
			
		} else {
			if (correctAnswers.size() != userAnswers.size()){
				passed = false;
			} else {
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
			}
		}
		
		return passed;
		
	}
}
