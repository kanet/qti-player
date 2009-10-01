package com.klangner.qtiplayer.client.util;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class XmlElement {

	/** Wrapped element */
	private Element element;

	public XmlElement(Element element){
		this.element = element;
	}
	
	/**
	 * Helper function for getting element attribute as string
	 * 
	 * @param name Attribute name
	 * @return attribute text or empty string if not found
	 */
	public String getAttributeAsString(String name){
		String attribute;
		
		attribute = element.getAttribute(name);
		if(attribute == null)
			return "";
		else
			return attribute;
	}
	
	/**
	 * Helper function for getting element attribute as boolean
	 * 
	 * @param name Attribute name
	 * @return attribute value or false if not found
	 */
	public boolean getAttributeAsBoolean(String name){
		String attribute;
		
		attribute = element.getAttribute(name);
		if(attribute == null)
			return false;
		else
			return (attribute.compareTo("true") == 0);
	}
	
	/**
	 * Helper function for getting element attribute as int
	 * 
	 * @param name Attribute name
	 * @return attribute value or 0 if not found
	 */
	public int getAttributeAsInt(String name){
		String attribute;
		
		attribute = element.getAttribute(name);
		if(attribute == null)
			return 0;
		else
			return Integer.parseInt(attribute);
	}
	
	/**
	 * Get first element with given tag name
	 * @param tagName
	 * @return first element or null if not found
	 */
	public Element getElement(String tagName){
		
		Element	node = null;
		NodeList nodeList = element.getElementsByTagName(tagName);
		
		if(nodeList.getLength() > 0){
			node = (Element)nodeList.item(0);
		}
		
		return node;
		
	}
	
	
	/**
	 * Wraper for getElementsByTagName
	 * @param tagName
	 * @return
	 */
	public NodeList getElementsByTagName(String tagName){
		return element.getElementsByTagName(tagName);
	}
	
	/**
	 * get children as HTML
	 * @return contents of tag as html
	 */
	public String getTextAsHtml(){
		String		htmlText = new String();
		NodeList	nodes = element.getChildNodes();
		
		for(int i = 0; i < nodes.getLength(); i ++){
			htmlText = htmlText + nodes.item(i).toString();
		}
		
		return htmlText;
	}
	
}
