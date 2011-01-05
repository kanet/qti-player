package com.qtitools.player.client.controller.communication;

public class FlowOptions {

	public FlowOptions(){
		showToC = true;
		showSummary = true;
		itemsDisplayMode = PageItemsDisplayMode.ONE;
		showCheck = true;
	}

	public FlowOptions(boolean toc, boolean summary, PageItemsDisplayMode pidm, boolean showCheck){
		showToC = toc;
		showSummary = summary;
		itemsDisplayMode = pidm;
		this.showCheck = showCheck;
	}

	public boolean showToC;
	public boolean showSummary;
	public PageItemsDisplayMode itemsDisplayMode;
	public boolean showCheck;
}
