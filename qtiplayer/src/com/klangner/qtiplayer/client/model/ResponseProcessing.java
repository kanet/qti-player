package com.klangner.qtiplayer.client.model;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;


public class ResponseProcessing {

	/** List of correct ids */
	private Vector<String> correctResponses;
	
	/**
	 * constructor
	 * @param item associated with this processing
	 */
	public ResponseProcessing(Element responseDeclarationNode){
    NodeList nodes = responseDeclarationNode.getElementsByTagName("value");

		correctResponses = new Vector<String>();

		for(int i = 0; i < nodes.getLength(); i++){
    	correctResponses.add( nodes.item(i).getFirstChild().getNodeValue() );
    }
	}
	
	/**
	 * @return feedback based on score
	 */
	public String getFeedback(){
		if(correctResponses.size() > 0)
			return correctResponses.get(0);
		else
			return "There is no feedback yet.";
	}
}
