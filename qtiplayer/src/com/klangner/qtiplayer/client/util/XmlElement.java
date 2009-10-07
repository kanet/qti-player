package com.klangner.qtiplayer.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class XmlElement {

	/** Wrapped element */
	private Element element;
	/** XML element factory */
	private IDomElementFactory	domElementFactory = null;

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
	 * get all TEXT nodes
	 * @return contents of tag as html
	 */
	public String getText(){
		String		text = new String();
		NodeList	nodes = element.getChildNodes();
		
		for(int i = 0; i < nodes.getLength(); i ++){
			Node node = nodes.item(i);
		
			if(node.getNodeType() == Node.TEXT_NODE){
				text = text + node.getNodeValue();
			}
		}
		
		return text;
	}
	
	/**
	 * get children as HTML
	 * @return contents of tag as html
	 */
	public String getTextAsHtml(){
		return convertToHtml().getInnerHTML();
	}
	
	/**
	 * Convert XML element into DOM (html) element
	 * @return contents of tag as html
	 */
	public com.google.gwt.dom.client.Element convertToHtml(){
		com.google.gwt.dom.client.Element domElement = 
			Document.get().createElement(element.getNodeName());
		copyChildren(element, domElement);
		
		return domElement;
	}
	
	/**
	 * Set factory for XML to DOM conversion
	 * @param domElementFactory
	 */
	public void setDomElementFactory(IDomElementFactory	domElementFactory){
		this.domElementFactory = domElementFactory;
	}
	
  /**
	 * copy content from XML into DOM
	 * @param node
	 * @param element 
	 */
	private void copyChildren(Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NodeList	nodes = srcElement.getChildNodes();
		Document	doc = Document.get();
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				dstElement.appendChild(doc.createTextNode(node.getNodeValue()));
			}
			else if(domElementFactory != null && domElementFactory.isSupportedElement(node.getNodeName())){
				dstElement.appendChild( domElementFactory.createDomElement( (Element)node ) );
			}
			else{
				Element xmlElement = (Element)node;
				com.google.gwt.dom.client.Element domElement;
				domElement = doc.createElement(node.getNodeName());
				dstElement.appendChild(domElement);
				// Copy attributes
				copyAttributes(xmlElement, domElement);
				// Add children
				copyChildren(xmlElement, domElement);
			}
		}
	}

	/**
	 * copy html attributes from XML into DOM
	 * @param srcElement
	 * @param dstElement
	 */
	private void copyAttributes(Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NamedNodeMap attributes = srcElement.getAttributes();
		
		for(int i = 0; i < attributes.getLength(); i++){
			Node attribute = attributes.item(i);
			dstElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
		}
	}
	
}
