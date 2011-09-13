package com.qtitools.player.client.module.prompt;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.module.CommonsFactory;

public class PromptModule extends Composite{

	public PromptModule(Element node){
		Widget w = CommonsFactory.getPromptView(node);
		initWidget(w);
	}
}
