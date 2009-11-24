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
package com.qtitools.player.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.module.IResponse;


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
	
	public String toString(){
		
		return "Id: " + id + "\n" + correctResponses; 
	}
}
