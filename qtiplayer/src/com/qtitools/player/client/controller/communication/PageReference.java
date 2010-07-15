package com.qtitools.player.client.controller.communication;

public class PageReference {

	public PageReference(PageType t, int[] pInd, ItemActivityOptions o){
		type = t;
		pageIndices = pInd;
		activityOptions = o;
	}
	
	public PageType type;
	public int[] pageIndices;
	public ItemActivityOptions activityOptions;
}
