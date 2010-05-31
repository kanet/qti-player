package com.qtitools.player.client.model.feedback;

import java.util.HashMap;
import java.util.Vector;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.Variable;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;

public class ModalFeedbackManager {

	public ModalFeedbackManager(NodeList feedbackNodes, String baseUrl){

		modals = new Vector<ModalFeedback>();
		ModalFeedback currModal;
		for (int n = 0 ; n < feedbackNodes.getLength() ; n ++){
			try {
				currModal = new ModalFeedback( feedbackNodes.item(n) , baseUrl );
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
	
	public void process (HashMap<String, Response> responses, HashMap<String, Outcome> outcomes, String senderIdentifier){
		
		Variable currVar;
		
		for (ModalFeedback currModal : modals){
			
			if (responses.containsKey(currModal.getVariableIdentifier()))
				currVar = responses.get(currModal.getVariableIdentifier());
			else if (outcomes.containsKey(currModal.getVariableIdentifier()))
				currVar = outcomes.get(currModal.getVariableIdentifier());
			else
				continue;
						
			boolean condition = false;
			boolean validated = (currModal.getValue().compareTo("") != 0);
			
			if (currModal.getValue().startsWith(">=")  ||  
				currModal.getValue().startsWith("<=")  ||  
				currModal.getValue().startsWith("==")){
				
				try {
					
					Integer referenceValue = Integer.valueOf(currModal.getValue().substring(2));
					Integer testValue = Integer.valueOf(currVar.getValuesShort());
					
					if (currModal.getValue().startsWith(">="))
						condition = (testValue >= referenceValue);
					else if (currModal.getValue().startsWith("<="))
						condition = (testValue <= referenceValue);
					else if (currModal.getValue().startsWith("=="))
						condition = (testValue == referenceValue);
					
				} catch (Exception e) {	}
				
			} else if ( currModal.getValue().contains(".")  || 
						currModal.getValue().contains("*")  ||
						currModal.getValue().contains("[")  ||
						currModal.getValue().contains("(")  ||
						currModal.getValue().contains("\\")  ||
						currModal.getValue().contains("^")  ||
						currModal.getValue().contains("$")  ||
						currModal.getValue().contains("]")  ||
						currModal.getValue().contains(")")){
				String currVarValues = currVar.getValuesShort();
				try {
					condition = currVarValues.matches(currModal.getValue());
					//alert("regexp: " + currVarValues + " vs " + currModal.getValue());
				} catch (Exception e) {
					//alert("regexp: " + currVarValues + " vs " + currModal.getValue());
				}
			} else {
				condition = currVar.compareValues(currModal.getValue().split(";"));
			}
			
			if (currModal.getSenderIdentifier().length() > 0){
				condition = (condition  && senderIdentifier.matches(currModal.getSenderIdentifier()));  
			}
			
			if (!validated)
				continue;
			
			if (currModal.hasHTMLContent()){
				
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

			if (currModal.hasSoundContent()){
				
				if (condition && currModal.showOnMatch())
					currModal.processSound();
				else if (!condition && !currModal.showOnMatch())
					currModal.processSound();
				
			}
			
		}
		
	}

	private native void alert(String s)/*-{
		alert(s);
	}-*/; 
}
