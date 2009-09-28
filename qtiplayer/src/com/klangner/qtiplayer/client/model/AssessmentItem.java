package com.klangner.qtiplayer.client.model;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;


public class AssessmentItem extends AbstractXMLDocument{

	/**
	 * @return item title
	 */
	public String getTitle(){

    Node rootNode = getDom().getElementsByTagName("assessmentItem").item(0);
    String title = ((Element)rootNode).getAttribute("title");
    
    return title;
	}
	
}
