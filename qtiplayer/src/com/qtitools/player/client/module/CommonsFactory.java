package com.qtitools.player.client.module;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

public class CommonsFactory {


	/**
	 * Get prompt for all modules
	 * @return
	 */
	public static Widget getPromptView(Element prompt){
		
		HTML	promptHTML = new HTML();
		promptHTML.setStyleName("qp-prompt");
		
		if(prompt != null){
			promptHTML.setHTML(prompt.getFirstChild().getNodeValue());
		}
		
		return promptHTML;
		
	}
}
