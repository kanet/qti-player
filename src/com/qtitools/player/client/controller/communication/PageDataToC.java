package com.qtitools.player.client.controller.communication;

public class PageDataToC extends PageData {

	public PageDataToC(String[] ts) {
		super(PageType.TOC);
		titles = ts;
	}
	
	public String[] titles;

}
