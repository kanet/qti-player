package com.qtitools.player.client.components.htmldnd;

import com.google.gwt.user.client.ui.Widget;

public class DragElement {

	public DragElement(Widget w, int elIndex){
		widget = w;
		elementIndex = elIndex;
	}
	
	private Widget widget;
	private int elementIndex;

	public Widget getWidget(){
		return widget;
	}
	
	public int getElementIndex(){
		return elementIndex;
	}
	
}
