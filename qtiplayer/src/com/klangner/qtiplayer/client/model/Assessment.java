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
package com.klangner.qtiplayer.client.model;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class Assessment extends AbstractXMLDocument{

	/** Array with references to items */
	private String[]			itemRefs;

	/**
	 * @return item ref
	 */
	public String getItemRef(int index){
		return itemRefs[index];
	}
	
	/**
	 * @return number of items in assessment
	 */
	public int getItemCount(){
		return itemRefs.length;
	}
	
	/**
	 * @return assessment title
	 */
	public String getTitle(){

    Node rootNode = getDom().getElementsByTagName("assessmentTest").item(0);
    String title = ((Element)rootNode).getAttribute("title");
    
    return title;
	}
	
	/**
	 * Load item refs into array
	 */
	protected void initData(){

		NodeList 	nodes = getDom().getElementsByTagName("assessmentItemRef");
    Node			itemRefNode;

    itemRefs = new String[nodes.getLength()];
    
    for(int i = 0; i < nodes.getLength(); i++){
    	itemRefNode = nodes.item(i);
    	itemRefs[i] = getBaseUrl() + ((Element)itemRefNode).getAttribute("href");
    }
	}
}
