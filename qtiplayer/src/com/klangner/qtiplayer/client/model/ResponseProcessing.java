package com.klangner.qtiplayer.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IResponse;


public class ResponseProcessing implements IResponse{

	/** List of correct ids */
	private Vector<String> 	correctResponses;
	/** Values set by set IResponse interface */
	private Set<String>			values;
	
	/**
	 * constructor
	 * @param item associated with this processing
	 */
	public ResponseProcessing(Element responseDeclarationNode){
    NodeList nodes = responseDeclarationNode.getElementsByTagName("value");

		correctResponses = new Vector<String>();
		values = new HashSet<String>();

		for(int i = 0; i < nodes.getLength(); i++){
    	correctResponses.add( nodes.item(i).getFirstChild().getNodeValue() );
    }
	}
	
	/**
	 * @return feedback based on score
	 */
	public String getFeedback(){
		Result result = getResult();
		
		return "Score: " + result.getScore() + " out of " + result.getMaxPoints() + " points";
	}

	/**
	 * @return assessment item score
	 */
	public Result getResult(){
		int maxPoints = correctResponses.size();
		int score = 0;
		
		for(int i = 0; i < correctResponses.size(); i++){
			if( values.contains(correctResponses.elementAt(i)) )
					score ++;
		}
		
		return new Result(score, maxPoints);
	}

	/**
	 * implementation of IResponse interface
	 * @param key
	 */
	public void set(String key) {
		values.add(key);
	}

	/**
	 * implementation of IResponse interface
	 * @param key
	 */
	public void unset(String key) {
		values.remove(key);
	}
}
