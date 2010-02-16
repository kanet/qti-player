package com.qtitools.player.client.control;

import com.google.gwt.xml.client.Document;

public class XMLData {

	public XMLData(Document doc, String url){
		document = doc;
		baseURL = url;
	}
	
	/** XML DOM document connected with this Assessment Content */
	private Document document;
	
	/** Base URL do document */
	private String baseURL;

	public Document getDocument(){
		return document;
	}

	public String getBaseURL(){
		return baseURL;
	}
	
}
