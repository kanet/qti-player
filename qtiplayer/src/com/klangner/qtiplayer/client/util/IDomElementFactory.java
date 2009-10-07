package com.klangner.qtiplayer.client.util;

import com.google.gwt.xml.client.Element;


public interface IDomElementFactory {

	/**
	 * @param tagName
	 * @return return true if this tag should be processed via this interface
	 */
	boolean isSupportedElement(String tagName);
	
	/**
	 * Create dom element from given xml element
	 * @param tagName
	 * @return new element or null if not supported
	 */
	com.google.gwt.dom.client.Element	createDomElement(Element xmlElement);
}
