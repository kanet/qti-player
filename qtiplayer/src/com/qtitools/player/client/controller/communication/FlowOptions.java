package com.qtitools.player.client.controller.communication;

public class FlowOptions {
	
	public FlowOptions(){
		showToC = true;
		showSummary = true;
		itemsDisplayMode = PageItemsDisplayMode.ONE;
	}

	public boolean showToC;
	public boolean showSummary;
	public PageItemsDisplayMode itemsDisplayMode;
}
