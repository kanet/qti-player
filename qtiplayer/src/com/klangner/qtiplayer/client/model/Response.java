package com.klangner.qtiplayer.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IResponse;


public class Response implements IResponse{

	/** Response id */
	private String 					id;
	/** List of correct ids */
	private Vector<String> 	correctResponses;
	/** Values set by IResponse interface */
	private Set<String>			values;
	
	/**
	 * constructor
	 * @param item associated with this processing
	 */
	public Response(Element responseDeclarationNode){
    NodeList nodes = responseDeclarationNode.getElementsByTagName("value");

		correctResponses = new Vector<String>();
		values = new HashSet<String>();

		id = responseDeclarationNode.getAttribute("identifier");
		for(int i = 0; i < nodes.getLength(); i++){
    	correctResponses.add( nodes.item(i).getFirstChild().getNodeValue() );
    }
	}
	
	/**
	 * @return id
	 */
	public String getID(){
		return id;
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
	 * @see IResponse#isCorrect(String)
	 */
	public boolean isCorrectAnswer(String key) {

		return correctResponses.contains(key);
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

	/**
	 * Reset results
	 */
	public void reset() {
		values.clear();
	}
	
}
