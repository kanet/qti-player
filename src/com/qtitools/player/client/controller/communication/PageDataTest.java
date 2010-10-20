package com.qtitools.player.client.controller.communication;

public class PageDataTest extends PageData {

	public PageDataTest(ItemData[] ids, DisplayOptions o){
		super(PageType.TEST);
		datas = ids;
		displayOptions = o;
	}
	
	public ItemData[] datas;
	public DisplayOptions displayOptions;
}
