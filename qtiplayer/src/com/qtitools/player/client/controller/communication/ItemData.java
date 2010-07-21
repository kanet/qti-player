package com.qtitools.player.client.controller.communication;

import com.qtitools.player.client.util.xml.document.XMLData;

public class ItemData {

	public ItemData(int index, XMLData d){
		itemIndex = index;
		data = d;
		errorMessage = "";
	}

	public ItemData(int index, String err){
		itemIndex = index;
		errorMessage = err;
	}
	
	public int itemIndex;
	public String errorMessage;
	public XMLData data;
	public ItemActivityOptions activityOptions;

}
