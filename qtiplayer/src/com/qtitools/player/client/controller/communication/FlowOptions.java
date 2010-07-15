package com.qtitools.player.client.controller.communication;

public class FlowOptions {

	public FlowOptions(){
		showToC = true;
		showSummary = true;
		itemsDisplayMode = PageItemsDisplayMode.ONE;
	}

	public FlowOptions(boolean toc, boolean summary, PageItemsDisplayMode pidm){
		showToC = toc;
		showSummary = summary;
		itemsDisplayMode = pidm;
	}

	public boolean showToC;
	public boolean showSummary;
	public PageItemsDisplayMode itemsDisplayMode;
}
