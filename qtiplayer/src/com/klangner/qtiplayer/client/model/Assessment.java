package com.klangner.qtiplayer.client.model;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class Assessment extends AbstractXMLDocument{

	/** Array with references to items */
	private String[]	itemRefs;

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
