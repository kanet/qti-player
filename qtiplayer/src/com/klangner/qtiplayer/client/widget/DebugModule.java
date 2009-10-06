package com.klangner.qtiplayer.client.widget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class DebugModule implements IModule {

	private String text;
	
	public DebugModule(Node node){
		this.text = node.toString();
	}
	
	public Widget getView() {
		return new Label(text);
	}

}
