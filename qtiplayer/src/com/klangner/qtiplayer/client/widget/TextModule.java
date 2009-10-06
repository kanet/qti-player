package com.klangner.qtiplayer.client.widget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

public class TextModule implements IModule {

	private String text;
	
	public TextModule(Element node){
		this.text = node.toString();
	}
	
	public Widget getView() {
		HTML htmlLabel;
		
		htmlLabel = new HTML(text);
		htmlLabel.setStyleName("qp-text-module");
		return htmlLabel;
	}

}
