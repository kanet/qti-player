package com.qtitools.player.client.util.xml;

import java.util.Vector;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;

public abstract class XMLConverter {

	public static Element getDOM(com.google.gwt.xml.client.Element element){
		Element dom = Document.get().createElement(element.getNodeName());
		parseXMLElement(element, dom, null, null, null, null);
		return dom;
	}
	public static Element getDOM(com.google.gwt.xml.client.Element element, IModuleSocket moduleSocket, IStateChangedListener stateChangedListener, IModuleCreator moduleCreator){
		Element dom = Document.get().createElement(element.getNodeName());
		parseXMLElement(element, dom, moduleSocket, stateChangedListener, moduleCreator, null);
		return dom;
	}

	public static Element getDOM(com.google.gwt.xml.client.Element element, Vector<String> ignoredTags){
		Element dom = Document.get().createElement(element.getNodeName());
		parseXMLElement(element, dom, null, null, null, ignoredTags);
		return dom;
	}
	
	private static void parseXMLElement(com.google.gwt.xml.client.Element srcElement, com.google.gwt.dom.client.Element dstElement, 
			IModuleSocket moduleSocket, IStateChangedListener stateChangedListener, IModuleCreator moduleCreator, Vector<String> ignoredTags){
		NodeList	nodes = srcElement.getChildNodes();
		Document	doc = Document.get();
		com.google.gwt.dom.client.Element domElement;
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				dstElement.appendChild(doc.createTextNode(node.getNodeValue()));
			}
			else if(moduleCreator != null && moduleCreator.isSupported(node.getNodeName()))
			{
				domElement = moduleCreator.createModule((com.google.gwt.xml.client.Element)node, moduleSocket, stateChangedListener);
				if( domElement != null )
					dstElement.appendChild( domElement );
			}
			else if (ignoredTags != null  &&  ignoredTags.contains(node.getNodeName())){
				
			}else
			{
				com.google.gwt.xml.client.Element xmlElement = (com.google.gwt.xml.client.Element)node;
				domElement = doc.createElement(node.getNodeName());
				dstElement.appendChild(domElement);
				// Copy attributes
				parseXMLAttributes(xmlElement, domElement);
				// Add children
				parseXMLElement(xmlElement, domElement, moduleSocket, stateChangedListener, moduleCreator, ignoredTags);
			}
		}
	}

	/**
	 * copy html attributes from XML into DOM
	 * @param srcElement
	 * @param dstElement
	 */
	private static void parseXMLAttributes(com.google.gwt.xml.client.Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NamedNodeMap attributes = srcElement.getAttributes();
		
		for(int i = 0; i < attributes.getLength(); i++){
			Node attribute = attributes.item(i);
			dstElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
		}
	}
	
}
