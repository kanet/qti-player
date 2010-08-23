package com.qtitools.player.client.controller.communication;

public class DisplayOptions extends DisplayContentOptions {
	
	public DisplayOptions(){
		tagsIgnored = new String[0];
		previewMode = false;
	}

	public DisplayOptions(String[] tags){
		tagsIgnored = tags;
		previewMode = false;
	}

	public DisplayOptions(String[] tags, boolean preview){
		tagsIgnored = tags;
		previewMode = preview;
	}

	private boolean previewMode;
	
	public boolean isPreviewMode(){
		return previewMode;
	}
	
	public void setPreviewMode(boolean preview){
		previewMode = preview;
	}
}
