package com.qtitools.player.client.module;

import java.util.Vector;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.components.ElementWrapperWidget;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.util.xml.XMLConverter;

public class CommonsFactory {


	/**
	 * Get prompt for all modules
	 * @return
	 */
	public static Widget getPromptView(Element prompt){
		
		if (prompt == null){
			Widget emptyPrompt =  new FlowPanel();
			emptyPrompt.setStyleName("qp-prompt");
			return emptyPrompt;
		}
		
		com.google.gwt.dom.client.Element promptElement = XMLConverter.getDOM(prompt, null, null, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return InlineModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element,
					IModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				Widget widget = ModuleFactory.createWidget(element, moduleSocket, moduleEventsListener);
				return widget.getElement();
			}
		});
			
		ElementWrapperWidget promptWidget = new ElementWrapperWidget(promptElement);
		promptWidget.setStyleName("qp-prompt");
		
		return promptWidget;
		
	}
	
	public static Widget getInlineTextView(Element contents, Vector<String> ignoredTags){
		
		com.google.gwt.dom.client.Element promptElement = XMLConverter.getDOM(contents, null, null, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return InlineModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element,
					IModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				Widget widget = InlineModuleFactory.createWidget(element, moduleSocket, moduleEventsListener);
				return widget.getElement();
			}
		}, ignoredTags);
			
		ElementWrapperWidget promptWidget = new ElementWrapperWidget(promptElement);
		promptWidget.setStyleName("qp-text-inline");
		
		return promptWidget;
		
		
	}
}
