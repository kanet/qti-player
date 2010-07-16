package com.qtitools.player.client.util.xml;

import java.util.Vector;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;

public abstract class XMLConverter {

	public static Element getDOM(com.google.gwt.xml.client.Element element){
		Element dom = Document.get().createElement(translateNodeName(element.getNodeName()));
		parseXMLElement(element, dom, null, null, null, null);
		return dom;
	}
	
	public static Element getDOM(com.google.gwt.xml.client.Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener, IModuleCreator moduleCreator){
		Element dom = Document.get().createElement(translateNodeName(element.getNodeName()));
		parseXMLElement(element, dom, moduleSocket, moduleEventsListener, moduleCreator, null);
		return dom;
	}

	public static Element getDOM(com.google.gwt.xml.client.Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener, IModuleCreator moduleCreator, Vector<String> ignoredTags){
		Element dom = Document.get().createElement(translateNodeName(element.getNodeName()));
		parseXMLElement(element, dom, moduleSocket, moduleEventsListener, moduleCreator, ignoredTags);
		return dom;
	}

	public static Element getDOM(com.google.gwt.xml.client.Element element, Vector<String> ignoredTags){
		Element dom = Document.get().createElement(translateNodeName(element.getNodeName()));
		parseXMLElement(element, dom, null, null, null, ignoredTags);
		return dom;
	}
	
	private static void parseXMLElement(com.google.gwt.xml.client.Element srcElement, com.google.gwt.dom.client.Element dstElement, 
			ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener, IModuleCreator moduleCreator, Vector<String> ignoredTags){
		NodeList	nodes = srcElement.getChildNodes();
		Document	doc = Document.get();
		com.google.gwt.dom.client.Element domElement;
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				dstElement.appendChild(doc.createTextNode(node.getNodeValue()));
			} else  if (node.getNodeType() == Node.COMMENT_NODE){
			}else if(moduleCreator != null && moduleCreator.isSupported(node.getNodeName()))
			{
				domElement = moduleCreator.createModule((com.google.gwt.xml.client.Element)node, moduleSocket, moduleEventsListener);
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
				parseXMLElement(xmlElement, domElement, moduleSocket, moduleEventsListener, moduleCreator, ignoredTags);
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
	
	private static String translateNodeName(String nodeName){
		if (nodeName.toLowerCase().compareTo("itembody") == 0)
			return "div";
		if (nodeName.toLowerCase().compareTo("prompt") == 0)
			return "div";
		if (nodeName.toLowerCase().compareTo("simplechoice") == 0)
			return "span";
		
		return nodeName;
	}
	
}
