package com.qtitools.player.client.module;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.module.mathexpr.MathExprInlineModule;

public class InlineModuleFactory {

	protected static String[] SUPPORTED_MODULES ={"math"};

	public static boolean isSupported(String test){
		for (int s= 0 ; s <SUPPORTED_MODULES.length ; s++)
			if (SUPPORTED_MODULES[s].compareTo(test) == 0)
				return true;
		
		return false;
	}
	
	public static Widget createWidget(Element element, IModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener){
		Widget	widget = null;

		if(element.getNodeName().compareTo("math") == 0){
			widget = new MathExprInlineModule(element);
		}
		
		return widget;
	}
}
