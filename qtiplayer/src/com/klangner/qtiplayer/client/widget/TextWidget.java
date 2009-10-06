package com.klangner.qtiplayer.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.xml.client.Element;

public class TextWidget extends Composite{

	/**
	 * constructor
	 * @param node
	 */
	public TextWidget(Element node){
		HTML htmlLabel;
		
		htmlLabel = new HTML(node.toString());
		htmlLabel.setStyleName("qp-text-module");
		initWidget(htmlLabel);
	}
	
}
