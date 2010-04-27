/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.qtitools.player.client.module.choice;

import java.util.Vector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.util.RandomizedSet;
import com.qtitools.player.client.util.xml.XMLUtils;

public class ChoiceModule extends Composite implements IInteractionModule {
	/** response processing interface */
	private Response response;
	/** module state changed listener */
	private IStateChangedListener stateListener;
	/** response id */
	private String responseIdentifier;
	/** Work mode single or multiple choice */
	private boolean multi = false;
	/** Shuffle? */
	private boolean shuffle = false;
	/** option widgets */
	private Vector<SimpleChoice> interactionElements;
	
	
	public ChoiceModule(Element element, IModuleSocket moduleSocket, IStateChangedListener stateChangedListener){
		
		multi = (XMLUtils.getAttributeAsInt(element, "maxChoices") != 1);
		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		stateListener = stateChangedListener;
		
		VerticalPanel vp = new VerticalPanel();
		
		vp.setStyleName("qp-choice-module");
		vp.add(getPromptView(element));
		vp.add(getOptionsView(element));
		
		initWidget(vp);
	}
	

	// ------------------------- MODULE CERATION --------------------------------
	
	
	/**
	 * Get prompt
	 * @return
	 */
	private Widget getPromptView(Element element){
		
		HTML	promptHTML = new HTML();
		Element prompt = XMLUtils.getFirstElementWithTagName(element, "prompt");
		
		promptHTML.setStyleName("qp-choice-prompt");
		
		if(prompt != null){
			promptHTML.setHTML(prompt.getFirstChild().getNodeValue());
		}
		
		return promptHTML;
		
	}

	  /**
	   * Get options view
	   * @return
	   */
	  private Widget getOptionsView(Element element){

		  VerticalPanel panel = new VerticalPanel();
		  NodeList optionNodes = element.getElementsByTagName("simpleChoice");
		  RandomizedSet<Element> randomizedNodes = new RandomizedSet<Element>();

		  interactionElements = new Vector<SimpleChoice>();
		  
		  // Add randomized nodes to shuffle table
		  if(shuffle){
			  for(int i = 0; i < optionNodes.getLength(); i++){
				  Element	option = (Element)optionNodes.item(i);
				  if(!XMLUtils.getAttributeAsBoolean(option, "fixed"))
					  randomizedNodes.push(option);
			  }
		  }

		  // Create buttons
		  for(int i = 0; i < optionNodes.getLength(); i++){
			  Element option = (Element)optionNodes.item(i);
			  SimpleChoice currInteractionElement;
			  String currInputId = Document.get().createUniqueId();
			  String currLabelId = Document.get().createUniqueId();

			  if(shuffle && !XMLUtils.getAttributeAsBoolean(option, "fixed") ){
				  option = randomizedNodes.pull();
			  }

			  currInteractionElement = new SimpleChoice(option, currInputId, currLabelId);
			  interactionElements.add(currInteractionElement);
			  panel.add(currInteractionElement);
		  }

		  return panel;
	  }


	// ------------------------- INTERFACES --------------------------------
	
	
	@Override
	public void markAnswers() {
		
		for (SimpleChoice currSC:interactionElements){
			currSC.markAnswers( response.correctAnswers.contains(currSC.getIdentifier()) );
		}
	}

	@Override
	public void unmark() {
		for (SimpleChoice currSC:interactionElements){
			currSC.unmark();
		}
		
	}

	@Override
	public void reset() {
		for (SimpleChoice currSC:interactionElements){
			currSC.reset();
		}
		
	}

	@Override
	public void showCorrectAnswers() {
		
	}

	@Override
	public JSONArray getState() {
		JSONArray  state = new JSONArray();

		for (SimpleChoice currSC:interactionElements){
			boolean b1 = currSC.isSelected();
			state.set(state.size(), JSONBoolean.getInstance(b1));
		}
		
		return state;
	}

	@Override
	public void setState(JSONArray newState) {

		Boolean currSelected;
		 
		for (int i  = 0 ; i < newState.size() && i < interactionElements.size(); i ++ ){
			currSelected = newState.get(i).isBoolean().booleanValue();
			interactionElements.get(i).setSelected(currSelected);
			
		}
		
		updateResponse();
		stateListener.onStateChanged();
	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {

		Vector<InternalEventTrigger> ids = new Vector<InternalEventTrigger>();
		for (SimpleChoice currSC:interactionElements){
			ids.add(new InternalEventTrigger(currSC.getInputId(), Event.ONCHANGE));
			ids.add(new InternalEventTrigger(currSC.getLabelId(), Event.ONMOUSEUP));
		}
		return ids;
	}


	@Override
	public void handleEvent(String tagID, InternalEvent param) {

		// check if multi selection mode
				
		if (param != null){
			String lastSelectedId = param.getEventTargetElement().getId();
		
			boolean targertIsButton = false;

			for (SimpleChoice currSC:interactionElements){
				if (currSC.getInputId().compareTo(lastSelectedId) == 0){
					targertIsButton = true;
					break;
				}
			}
			
			if (targertIsButton){
				for (SimpleChoice currSC:interactionElements){
					if (currSC.getInputId().compareTo(lastSelectedId) == 0){
						continue;
					}
					if (!multi){
						currSC.setSelected(false);
					}
				}
				
			} else {
				for (SimpleChoice currSC:interactionElements){
					if (currSC.getLabelId().compareTo(lastSelectedId) == 0){
						currSC.setSelected(!currSC.isSelected());
						continue;
					}
					if (!multi){
						currSC.setSelected(false);
					}
				}
			}
		}
		
		// pass response
		
		updateResponse();
		
	}
	
	private void updateResponse(){
		Vector<String> currResponseValues = new Vector<String>();
		
		for (SimpleChoice currSC:interactionElements){
			if (currSC.isSelected()){
				currResponseValues.add(currSC.getIdentifier());
			}
			currSC.showFeedback(currSC.isSelected());
		}
		
		response.set(currResponseValues);
		stateListener.onStateChanged();
	}


	@Override
	public void onOwnerAttached() {
		// do nothing
		
	}

}
