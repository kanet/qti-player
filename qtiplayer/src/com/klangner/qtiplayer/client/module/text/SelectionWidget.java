package com.klangner.qtiplayer.client.module.text;

import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.RandomizedSet;
import com.klangner.qtiplayer.client.util.XmlElement;

public class SelectionWidget extends ListBox implements IOnChangeHandler{

	/** response processing interface */
	private IResponse 	response;
	/** Shuffle? */
	private boolean 		shuffle = false;
	/** Last selected value */
	private String	lastValue = null;

	/**
	 * constructor
	 * @param response
	 */
	public SelectionWidget(Element element, IResponse 	response){
		
		XmlElement choiceElement = new XmlElement(element);
		this.response = response;
		this.shuffle = choiceElement.getAttributeAsBoolean("shuffle");
		
		if(shuffle)
			initRandom(element);
		else
			init(element);
	}
	
	/**
	 * Process on change event 
	 */
	public void onChange(){
		
		if(lastValue != null)
			response.unset(lastValue);
		
		lastValue = getValue(getSelectedIndex());
		response.set(lastValue);
	}

	/**
	 * init widget view
	 * @param element
	 */
	private void init(Element inlineChoiceElement){
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		addItem("");
		
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				XmlElement choiceElement = new XmlElement((Element)nodes.item(i));
				addItem(choiceElement.getText(), choiceElement.getAttributeAsString("identifier"));
			}
		}
	}
	
	/**
	 * init widget view. Randomize options
	 * @param element
	 */
	private void initRandom(Element inlineChoiceElement){
		RandomizedSet<XmlElement>	randomizedNodes = new RandomizedSet<XmlElement>();
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		addItem("");
		
		// Add nodes to temporary listy
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				XmlElement choiceElement = new XmlElement((Element)nodes.item(i));
				randomizedNodes.push(choiceElement);
			}
		}
		
		while(randomizedNodes.hasMore()){
			XmlElement choiceElement = randomizedNodes.pull();
			addItem(choiceElement.getText(), choiceElement.getAttributeAsString("identifier"));
		}
	}
}
