package com.qtitools.player.client.controller.communication;

import com.qtitools.player.client.util.xml.document.XMLData;

public class ItemData {

	public ItemData(int index, XMLData d){
		itemIndex = index;
		data = d;
	}
	
	public int itemIndex;
	public XMLData data;
	public ItemActivityOptions activityOptions;

}
