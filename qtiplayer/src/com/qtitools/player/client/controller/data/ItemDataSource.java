package com.qtitools.player.client.controller.data;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.controller.style.StyleLinkDeclaration;
import com.qtitools.player.client.util.xml.document.XMLData;

public class ItemDataSource {

	public ItemDataSource(XMLData d){
		data = d;
		styleDeclaration = new StyleLinkDeclaration(data.getDocument().getElementsByTagName("styleDeclaration"), data.getBaseURL());
		Node rootNode = data.getDocument().getElementsByTagName("assessmentItem").item(0);
		title = ((Element)rootNode).getAttribute("title");
		errorMessage = "";
	}
	
	public ItemDataSource(String err){
		errorMessage = err;
	}
	
	private XMLData data;
	private StyleLinkDeclaration styleDeclaration;
	private String title;
	private String errorMessage;

	public XMLData getItemData(){
		return data;
	}
	
	public Vector<String> getStyleLinksForUserAgent(String userAgent){
		return styleDeclaration.getStyleLinksForUserAgent(userAgent);
	}
	
	public boolean isError(){
		return errorMessage.length() > 0;
	}
	
	public String getTitle(){
		if (title != null)
			return title;
		else
			return "";
	}

}

