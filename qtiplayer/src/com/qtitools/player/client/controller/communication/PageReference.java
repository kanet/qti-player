package com.qtitools.player.client.controller.communication;

public class PageReference {

	public PageReference(PageType t, int[] pInd, DisplayOptions o){
		type = t;
		pageIndices = pInd;
		displayOptions = o;
	}
	
	public PageType type;
	public int[] pageIndices;
	public DisplayOptions displayOptions;
}
