package com.qtitools.player.client.model.feedback;

import java.util.HashMap;
import java.util.Vector;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.Variable;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;

public class FeedbackManager {

	public FeedbackManager(NodeList feedbackNodes){

		modals = new Vector<ModalFeedback>();
		ModalFeedback currModal;
		for (int n = 0 ; n < feedbackNodes.getLength() ; n ++){
			try {
				currModal = new ModalFeedback( feedbackNodes.item(n) );
				modals.add(currModal);
			} catch (Exception e) {	}
		}
		
		container = new FlowPanel();
		container.setStyleName("qp-feedback-modal-container");
	}
	
	public Vector<ModalFeedback> modals; 
	private FlowPanel container;
	
	public Panel getView(){
		return container;
	}
	
	public void process (HashMap<String, Response> responses, HashMap<String, Outcome> outcomes){
		
		Variable currVar;
		
		for (ModalFeedback currModal : modals){
			
			if (responses.containsKey(currModal.getVariableIdentifier()))
				currVar = responses.get(currModal.getVariableIdentifier());
			else if (outcomes.containsKey(currModal.getVariableIdentifier()))
				currVar = outcomes.get(currModal.getVariableIdentifier());
			else
				continue;
			
			String currVarValues = currVar.getValuesShort();
			
			boolean condition = false;
			boolean validated = (currModal.getValue().compareTo("") != 0);
			
			if (currModal.getValue().startsWith(">")  ||  
				currModal.getValue().startsWith(">=")  ||
				currModal.getValue().startsWith("<")  ||  
				currModal.getValue().startsWith("<=")  ||  
				currModal.getValue().startsWith("=")){
				
				Integer referenceValue = Integer.valueOf(currModal.getValue());
				Integer testValue = Integer.valueOf(currVarValues);
				
				if (currModal.getValue().startsWith(">"))
					condition = (testValue > referenceValue);
				else if (currModal.getValue().startsWith(">="))
					condition = (testValue >= referenceValue);
				else if (currModal.getValue().startsWith("<"))
					condition = (testValue < referenceValue);
				else if (currModal.getValue().startsWith("<="))
					condition = (testValue <= referenceValue);
				else if (currModal.getValue().startsWith("="))
					condition = (testValue == referenceValue);
				
			} else {
				condition = currVarValues.matches(currModal.getValue());
			}
			
			if (!validated)
				continue;
			
			if (condition){
				
				if (container.getWidgetIndex(currModal.getView()) == -1  &&  currModal.showOnMatch()){
					container.add(currModal.getView());
				} else if (container.getWidgetIndex(currModal.getView()) != -1  &&  !currModal.showOnMatch()){
					container.remove(currModal.getView());
				}
				
			} else {

				if (container.getWidgetIndex(currModal.getView()) == -1  &&  !currModal.showOnMatch()){
					container.add(currModal.getView());
				} else if (container.getWidgetIndex(currModal.getView()) != -1  &&  currModal.showOnMatch()){
					container.remove(currModal.getView());
				}
				
			}
			
		}
		
	}
}
