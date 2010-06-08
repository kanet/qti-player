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
package com.qtitools.player.client.module.text;

import java.io.Serializable;
import java.util.Vector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.util.RandomizedSet;
import com.qtitools.player.client.util.xml.XMLUtils;

public class SelectionWidget extends InlineHTML implements IInteractionModule{

	/** response processing interface */
	private Response response;
	private String responseIdentifier;
	/** module state changed listener */
	private IStateChangedListener stateListener;
	/** widget id */
	private String  id;
	/** panel widget */
	private ListBox  listBox;
	/** Shuffle? */	private boolean 		shuffle = false;
	/** Last selected value */
	private String	lastValue = null;
	

	/**
	 * constructor
	 * @param moduleSocket
	 */
	public SelectionWidget(Element element, IModuleSocket moduleSocket, IStateChangedListener stateChangedListener){
		
		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier"); 

		id = Document.get().createUniqueId();
		response = moduleSocket.getResponse(responseIdentifier);
		stateListener = stateChangedListener;
		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");
		
		listBox = new ListBox();
		if(shuffle)
			initRandom(element);
		else
			init(element);

		listBox.getElement().setId(id);
		getElement().appendChild(listBox.getElement());
		
		setStyleName("qp-text-choice");
	}

	// ------------------------ INTERFACES ------------------------ 

	@Override
	public void onOwnerAttached() {
		// do nothing
		
	}

	@Override
	public void lock(boolean l) {
		  listBox.setEnabled(!l);
		
	}

	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
	  listBox.setEnabled(false);
		if( response.isCorrectAnswer(lastValue) )
			setStyleName("qp-text-choice-correct");
		else
			setStyleName("qp-text-choice-wrong");
	}

	/**
	 * @see IActivity#unmark()
	 */
	public void unmark() {
		  listBox.setEnabled(true);
		  setStyleName("qp-text-choice");
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {
	  listBox.setEnabled(true);
	  setStyleName("qp-text-choice");
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	  listBox.setEnabled(false);
	}
	
  /**
   * @see IStateful#getState()
   */
  public JSONArray getState() {
	  
	  JSONArray jsonArr = new JSONArray();
	  
	  String stateString = "";
	  
	  if (lastValue != null)
		  stateString = lastValue;
	  
	  jsonArr.set(0, new JSONString(stateString));
	  
	  return jsonArr;
  }

  
  	/**
 	 * @see IStateful#setState(Serializable)
 	 */
  	public void setState(JSONArray newState) {
	
		String state = "";
	
		if (newState == null){
		} else if (newState.size() == 0){
		} else if (newState.get(0).isString() == null){
		} else {
			state = newState.get(0).isString().stringValue();
			lastValue = null;
		}
	
		for(int i = 0; i < listBox.getItemCount(); i++){
			if( listBox.getValue(i).compareTo(state) == 0){
				listBox.setSelectedIndex(i);
				break;
			}
		}
		updateResponse(false);
  }
  
	/**
	 * init widget view
	 * @param element
	 */
	private void init(Element inlineChoiceElement){
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				Element choiceElement = (Element)nodes.item(i);
				listBox.addItem(XMLUtils.getText(choiceElement), 
				    XMLUtils.getAttributeAsString(choiceElement, "identifier"));
			}
		}
	}
	
	/**
	 * init widget view. Randomize options
	 * @param element
	 */
	private void initRandom(Element inlineChoiceElement){
		RandomizedSet<Element>	randomizedNodes = new RandomizedSet<Element>();
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		// Add nodes to temporary list
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				randomizedNodes.push((Element)nodes.item(i));
			}
		}
		
		while(randomizedNodes.hasMore()){
			Element choiceElement = randomizedNodes.pull();
      listBox.addItem(XMLUtils.getText(choiceElement), 
          XMLUtils.getAttributeAsString(choiceElement, "identifier"));
		}
		
	}

	
	/**
	 * @see IBrowserEventListener#getInputsId()
	 */
	/*
	public Vector<String> getInputsId() {
		Vector<String> v = new Vector<String>();
		v.add(id);
		return v;
	}
	*/
	
	/**
	 * Process on change event 
	 */
	/*
	public void onChange(Event event){
		
		if(lastValue != null)
			response.remove(lastValue);
		
		lastValue = listBox.getValue(listBox.getSelectedIndex());
		response.add(lastValue);
	}
	*/
	
	@Override
	public Vector<InternalEventTrigger> getTriggers() {
		Vector<InternalEventTrigger> v = new Vector<InternalEventTrigger>();
		v.add(new InternalEventTrigger(id, Event.ONCHANGE));
		return v;
	}

	@Override
	public void handleEvent(String tagID, InternalEvent param) {
		updateResponse(true);
	}
	
	private void updateResponse(boolean markSender){

		if(lastValue != null)
			response.remove(lastValue);
		
		lastValue = listBox.getValue(listBox.getSelectedIndex());
		response.add(lastValue);
		stateListener.onStateChanged(markSender ? this : null);
	}

	@Override
	public String getIdentifier() {
		return responseIdentifier;
	}
}
