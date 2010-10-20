package com.qtitools.player.client.components.htmldnd;

public enum DragMode {
	NONE, FREE, VERTICAL, HORIZONTAL;
	
	public static boolean isSlotManagerEnabled(DragMode dsm){
		return (dsm == VERTICAL  ||  dsm == HORIZONTAL);
	}
}
