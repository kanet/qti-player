package com.klangner.qtiplayer.client.util;

import com.google.gwt.dom.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * Convert XML into DOM
 * 
 * @author Krzysztof Langner
 */
public class XmlConverter {

	/** Wrapped element */
	private Element element;
	/** XML element factory */
	private IDomElementFactory	domElementFactory = null;

	public XmlConverter(Element element){
		this.element = element;
	}
	
  /**
   * Set factory for XML to DOM conversion
   * @param domElementFactory
   */
  public void setDomElementFactory(IDomElementFactory domElementFactory){
    this.domElementFactory = domElementFactory;
  }
  
	/**
	 * get children as HTML
	 * @return contents of tag as html
	 */
	public String getTextAsHtml(){
		return getAsDOM().getInnerHTML();
	}
	
	/**
	 * Convert XML element into DOM (html) element
	 * @return contents of tag as html
	 */
	public com.google.gwt.dom.client.Element getAsDOM(){
		com.google.gwt.dom.client.Element domElement = 
			Document.get().createElement(element.getNodeName());
		copyChildren(element, domElement);
		
		return domElement;
	}
	
  /**
	 * copy content from XML into DOM
	 * @param node
	 * @param element 
	 */
	private void copyChildren(Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NodeList	nodes = srcElement.getChildNodes();
		Document	doc = Document.get();
		com.google.gwt.dom.client.Element domElement;
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				dstElement.appendChild(doc.createTextNode(node.getNodeValue()));
			}
			else if(domElementFactory != null && domElementFactory.isSupportedElement(node.getNodeName())){
			  domElement = domElementFactory.createDomElement( (Element)node );
			  if( domElement != null )
			    dstElement.appendChild( domElement );
			}
			else{
				Element xmlElement = (Element)node;
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
