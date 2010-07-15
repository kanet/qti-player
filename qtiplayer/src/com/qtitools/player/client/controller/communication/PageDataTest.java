package com.qtitools.player.client.controller.communication;

public class PageDataTest extends PageData {

	public PageDataTest(ItemData[] ids, ItemActivityOptions o){
		super(PageType.TEST);
		datas = ids;
		activityOptions = o;
	}
	
	public ItemData[] datas;
	public ItemActivityOptions activityOptions;
}
