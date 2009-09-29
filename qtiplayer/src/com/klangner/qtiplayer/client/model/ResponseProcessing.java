package com.klangner.qtiplayer.client.model;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.modules.IResponse;


public class ResponseProcessing implements IResponse{

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

	/**
	 * implementation of IResponse interface
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * implementation of IResponse interface
	 * @param key
	 */
	public void unset(String key) {
		// TODO Auto-generated method stub
		
	}
}
