package com.klangner.qtiplayer.client.module.debug;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.xml.client.Element;

public class DebugWidget extends Composite{

	/**
	 * constructor
	 * @param node
	 */
	public DebugWidget(Element node){
		HTML htmlLabel;
		
		htmlLabel = new HTML(node.toString());
		htmlLabel.setStyleName("qp-text-module");
		initWidget(htmlLabel);
	}
	
}
