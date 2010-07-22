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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.feedback.InlineFeedbackSocket;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.CommonsFactory;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.util.RandomizedSet;
import com.qtitools.player.client.util.xml.XMLUtils;

public class ChoiceModule extends Composite implements IInteractionModule {
	/** response processing interface */
	private Response response;
	/** module state changed listener */
	private ModuleStateChangedEventsListener stateListener;
	/** response id */
	private String responseIdentifier;
	/** Work mode single or multiple choice */
	private boolean multi = false;
	/** Shuffle? */
	private boolean shuffle = false;
	/** option widgets */
	private Vector<SimpleChoice> interactionElements;
	
	private boolean locked = false;
		
	
	public ChoiceModule(Element element, ModuleSocket moduleSocket, ModuleStateChangedEventsListener stateChangedListener){
		
		multi = (XMLUtils.getAttributeAsInt(element, "maxChoices") != 1);
		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		stateListener = stateChangedListener;
		
		VerticalPanel vp = new VerticalPanel();
		
		vp.setStyleName("qp-choice-module");
		vp.add(CommonsFactory.getPromptView(XMLUtils.getFirstElementWithTagName(element, "prompt")));
		vp.add(getOptionsView(element, moduleSocket));
		
		initWidget(vp);
	}
	

	// ------------------------- MODULE CERATION --------------------------------
	

	  /**
	   * Get options view
	   * @return
	   */
	  private Widget getOptionsView(Element element, InlineFeedbackSocket inlineFeedbackSocket){

		  VerticalPanel panel = new VerticalPanel();
		  NodeList optionNodes = element.getElementsByTagName("simpleChoice");
		  RandomizedSet<Element> randomizedNodes = new RandomizedSet<Element>();
		  RandomizedSet<Integer> randomizedIndices = new RandomizedSet<Integer>();

		  interactionElements = new Vector<SimpleChoice>();
		  for (int el = 0 ; el < optionNodes.getLength() ; el ++)
			  interactionElements.add(null);
		  
		  // Add randomized nodes to shuffle table
		  if(shuffle){
			  for(int i = 0; i < optionNodes.getLength(); i++){
				  Element	option = (Element)optionNodes.item(i);
				  if(!XMLUtils.getAttributeAsBoolean(option, "fixed")){
					  randomizedNodes.push(option);
					  randomizedIndices.push(i);
				  }
			  }
		  }

		  // Create buttons
		  for(int i = 0; i < optionNodes.getLength(); i++){
			  int optionIndex = i;
			  Element option = (Element)optionNodes.item(i);
			  SimpleChoice currInteractionElement;
			  String currInputId = Document.get().createUniqueId();
			  String currLabelId = Document.get().createUniqueId();

			  if(shuffle && !XMLUtils.getAttributeAsBoolean(option, "fixed") ){
				  //option = randomizedNodes.pull();
				  optionIndex = randomizedIndices.pull();
				  option = (Element)optionNodes.item(optionIndex);
			  }

			  currInteractionElement = new SimpleChoice(option, currInputId, currLabelId, multi, inlineFeedbackSocket);
			  //interactionElements.add(currInteractionElement);
			  interactionElements.set(optionIndex, currInteractionElement);
			  panel.add(currInteractionElement);
		  }

		  return panel;
	  }


	// ------------------------- INTERFACES --------------------------------


	@Override
	public void onOwnerAttached() {
		// do nothing
		
	}


	@Override
	public void lock(boolean l) {
		locked = l;
		for (SimpleChoice currSC:interactionElements){
			currSC.setEnabled(!l);
		}
		
	}
	
	@Override
	public void markAnswers(boolean mark) {
		
		for (SimpleChoice currSC:interactionElements){
			if (mark)
				currSC.markAnswers( response.correctAnswers.contains(currSC.getIdentifier()) );
			else 
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
	public void showCorrectAnswers(boolean show) {
		
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
		
		updateResponse(null, false);
		//stateListener.onStateChanged(this);
	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {

		Vector<InternalEventTrigger> ids = new Vector<InternalEventTrigger>();
		for (SimpleChoice currSC:interactionElements){
			ids.add(new InternalEventTrigger(currSC.getInputId(), Event.ONMOUSEUP));
			ids.add(new InternalEventTrigger(currSC.getLabelId(), Event.ONMOUSEUP));
		}
		return ids;
	}


	@Override
	public void handleEvent(String tagID, InternalEvent param) {
		if (locked)
			return;

		SimpleChoice target = null;
		
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
						if (!multi  &&  !currSC.isSelected())
							currSC.setSelected(!currSC.isSelected());
						currSC.showFeedback((!multi && currSC.isSelected())  ||  (multi && !currSC.isSelected()), response.correctAnswers.contains(currSC.getIdentifier()));
						target = currSC;
						continue;
					} else if (!multi){
						currSC.setSelected(false);
					}
				}
				
			} else {
				for (SimpleChoice currSC:interactionElements){
					if (currSC.getLabelId().compareTo(lastSelectedId) == 0){
						currSC.setSelected(!currSC.isSelected());
						currSC.showFeedback(currSC.isSelected(), response.correctAnswers.contains(currSC.getIdentifier()));
						continue;
					} else if (!multi){
						currSC.setSelected(false);
					}
				}
			}
		}
		param.stopPropagation();
		// pass response
		
		updateResponse(target, true);
		
	}
	
	private void updateResponse(SimpleChoice target, boolean userInteract){
		Vector<String> currResponseValues = new Vector<String>();
		
		for (SimpleChoice currSC:interactionElements){
			if (currSC.equals(target) && multi && !currSC.isSelected()){
				currResponseValues.add(currSC.getIdentifier());
			} else if ((!currSC.equals(target) || !multi) && currSC.isSelected()){
				currResponseValues.add(currSC.getIdentifier());
			}
			//currSC.showFeedback(currSC.isSelected());
		}
		
		if (!response.compare(currResponseValues)){
			response.set(currResponseValues);
			stateListener.onStateChanged(userInteract, this);
		}
	}

	@Override
	public String getIdentifier() {
		return responseIdentifier;
	}

}
