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
package com.qtitools.player.client.model.variables.response;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.variables.BaseType;
import com.qtitools.player.client.model.variables.Cardinality;
import com.qtitools.player.client.model.variables.Variable;


public class Response extends Variable {

	/** List of correct ids */
	public Vector<String> 	correctAnswers;
	/** userAnswers set by IResponse interface */
	//private Set<String>	userAnswers;
	// userAnswers cannot be HashSet because it does not cover cardinality::ordered functionality
	
	public Mapping mapping;
	
	/**
	 * constructor
	 * @param item associated with this processing
	 */
	public Response(Node responseDeclarationNode){
    
		NodeList nodes = ((Element)responseDeclarationNode).getElementsByTagName("value");

		correctAnswers = new Vector<String>();
		
		values = new Vector<String>();

		identifier = ((Element)responseDeclarationNode).getAttribute("identifier");

		cardinality = Cardinality.fromString( ((Element)responseDeclarationNode).getAttribute("cardinality") );
		
		baseType = BaseType.fromString( ((Element)responseDeclarationNode).getAttribute("baseType") );
		
		for(int i = 0; i < nodes.getLength(); i++){
			correctAnswers.add( nodes.item(i).getFirstChild().getNodeValue() );
		}
		
		mapping = new Mapping(((Element)responseDeclarationNode).getElementsByTagName("mapping").item(0));
	}
	
	/**
	 * @return id
	 */
	public String getID(){
		return identifier;
	}
	
	/**
	 * @see IResponse#isCorrect(String)
	 */
	public boolean isCorrectAnswer(String key) {

		return correctAnswers.contains(key);
	}

	/**
	 * implementation of IResponse interface
	 * @param key
	 */
	public void add(String key) {
		for (String currValue:values)
			if (currValue == key)
				return;
		
		if (cardinality == Cardinality.SINGLE)
			values.clear();
		
		values.add(key);
	}

	/**
	 * implementation of IResponse interface
	 * @param key
	 */
	public void remove(String key) {
		
		for (int i = 0 ; i < values.size() ; i ++){
			if (values.get(i) == key){
				values.remove(i);
				return;
			}
		}
				
	}
	
	public void set(Vector<String> keys){
		values = keys;
	}
	
	public boolean compare(Vector<String> test){
		if (values.size() != test.size())
			return false;
		
		for (int i = 0 ; i < values.size() ; i ++){
			if (values.get(i).compareTo(test.get(i)) != 0)
				return false;
		}
		
		return true;
			
	}

	/**
	 * Reset results
	 */
	public void reset() {
		values.clear();
	}
	
	public String toString(){
		
		return "Id: " + identifier + "\n" + correctAnswers; 
	}
}
