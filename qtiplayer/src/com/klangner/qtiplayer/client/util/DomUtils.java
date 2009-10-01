package com.klangner.qtiplayer.client.util;

import com.google.gwt.xml.client.Element;

public class DomUtils {

	/**
	 * Helper function for getting element attribute
	 * @param node
	 * @param name
	 * @return
	 */
	public static String getAttribute(Element node, String name){
		String attribute;
		
		attribute = node.getAttribute(name);
		if(attribute == null)
			return "";
		else
			return attribute;
	}
}
