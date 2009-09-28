package com.klangner.qtiplayer.client.model.modules;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class TextModule implements IModule {

	private String text;
	
	public TextModule(Node node){
		this.text = node.toString();
	}
	
	public Widget getView() {
		return new HTML(text);
	}

}
