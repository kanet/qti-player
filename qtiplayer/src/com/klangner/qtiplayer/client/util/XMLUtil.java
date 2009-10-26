package com.klangner.qtiplayer.client.util;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class XMLUtil {

  /**
   * Helper function for getting element attribute as string
   * 
   * @param name Attribute name
   * @return attribute text or empty string if not found
   */
  public static String getAttributeAsString(Element element, String name){
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
  public static boolean getAttributeAsBoolean(Element element, String name){
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
  public static int getAttributeAsInt(Element element, String name){
    String attribute;
    
    attribute = element.getAttribute(name);
    if(attribute == null)
      return 0;
    else
      return Integer.parseInt(attribute);
  }
  
  
	/**
	 * get all TEXT nodes
	 * @return contents of tag
	 */
	public static String getText(Element element){
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
   * Get first element with given tag name
   * @param tagName
   * @return first element or null if not found
   */
  public static Element getFirstElementWithTagName(Element element, String tagName){
    
    Element node = null;
    NodeList nodeList = element.getElementsByTagName(tagName);
    
    if(nodeList.getLength() > 0){
      node = (Element)nodeList.item(0);
    }
    
    return node;
    
  }
  
  
	
	
}
