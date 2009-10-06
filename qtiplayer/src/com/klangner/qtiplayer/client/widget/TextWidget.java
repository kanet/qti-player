package com.klangner.qtiplayer.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

public class TextWidget extends Composite{

	/**
	 * constructor
	 * @param node
	 */
	public TextWidget(Element node){
	
		Widget view;
		
		if(node.getElementsByTagName("inlineChoiceInteraction").getLength() > 0){
			view = new Label("inline choice");
		}
		else{
			view = staticTextView(node);
		}

		initWidget(view);
	}
	
	/**
	 * Create widget with static text
	 */
	private Widget staticTextView(Element node){
		
		HTML htmlLabel;
		
		htmlLabel = new HTML(node.toString());
		htmlLabel.setStyleName("qp-text-module");
		return htmlLabel;
	}
	
}
