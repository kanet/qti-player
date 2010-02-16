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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.util.RandomizedSet;
import com.qtitools.player.client.util.xml.XMLUtils;

public class ChoiceModule extends Composite implements IInteractionModule {

	/** response processing interface */
	private Response response;

	/** response id */
	private String responseIdentifier;
	/** Work mode single or multiple choice */
	private boolean multi = false;
	/** Shuffle? */
	private boolean shuffle = false;
	/** option widgets */
	private Vector<SimpleChoice> interactionElements;
	
	
	public ChoiceModule(Element element, IModuleSocket moduleSocket){
		
		multi = (XMLUtils.getAttributeAsInt(element, "maxChoices") != 1);
		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		
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
			  String currId = Document.get().createUniqueId();

			  if(shuffle && !XMLUtils.getAttributeAsBoolean(option, "fixed") ){
				  option = randomizedNodes.pull();
			  }

			  currInteractionElement = new SimpleChoice(option, currId);
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
	public void reset() {
		for (SimpleChoice currSC:interactionElements){
			currSC.reset();
		}
		
	}

	@Override
	public void showCorrectAnswers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Serializable getState() {
	    HashMap<String, Serializable>  state = new HashMap<String, Serializable>();

		for (SimpleChoice currSC:interactionElements){
			boolean b1 = currSC.isSelected();
			Boolean b2 = new Boolean(b1);
			state.put(currSC.getIdentifier(), b2);
		}
		
		return state;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setState(Serializable newState) {
		
		 if (newState instanceof HashMap){

			 HashMap<String, Serializable> state = (HashMap<String, Serializable>)newState;
			 Boolean currSelected;
		 
			 for(SimpleChoice currSC:interactionElements){
				 currSelected = (Boolean)state.get(currSC.getIdentifier());
				 currSC.setSelected(currSelected);
			 }
		 }
		 
		 onChange(null);
	}

	@Override
	public Vector<String> getInputsId() {
		Vector<String> ids = new Vector<String>();
		for (SimpleChoice currSC:interactionElements)
			ids.add(currSC.getInputId());
		return ids;
	}

	@Override
	public void onChange(Event event) {
		
		// check if multi selection mode
				
		if (event != null){
			String lastSelectedId = com.google.gwt.dom.client.Element.as(event.getEventTarget()).getId();
		
			for (SimpleChoice currSC:interactionElements){
				if (currSC.getInputId().compareTo(lastSelectedId) == 0){
					continue;
				}
				if (!multi){
					currSC.setSelected(false);
				}
			}
		}
		
		// pass response
		
		Vector<String> currResponseValues = new Vector<String>();
		
		for (SimpleChoice currSC:interactionElements){
			if (currSC.isSelected()){
				currResponseValues.add(currSC.getIdentifier());
			}
			currSC.showFeedback(currSC.isSelected());
		}
		
		response.set(currResponseValues);
	}

}
