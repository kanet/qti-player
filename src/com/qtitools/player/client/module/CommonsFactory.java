package com.qtitools.player.client.module;

import java.util.Vector;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.components.ElementWrapperWidget;
import com.qtitools.player.client.controller.communication.DisplayContentOptions;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.module.mathexpr.MathExprInlineModule;
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
					ModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				Widget widget = ModuleFactory.createWidget(element, moduleSocket, moduleEventsListener);
				return widget.getElement();
			}
		}, new DisplayContentOptions());
			
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
					ModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				Widget widget = InlineModuleFactory.createWidget(element, null);
				
				return widget.getElement();
			}
		}, ignoredTags);
			
		ElementWrapperWidget promptWidget = new ElementWrapperWidget(promptElement);
		promptWidget.setStyleName("qp-text-inline");
		
		return promptWidget;
		
	}
	

	
	public static Widget getFeedbackView(Element contents, final Widget attachedParent, final Vector<com.google.gwt.dom.client.Element> mathElements){

		com.google.gwt.dom.client.Element promptElement = XMLConverter.getDOM(contents, null, null, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return InlineModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element,
					ModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				Widget widget = InlineModuleFactory.createWidget(element, attachedParent);
				
				if ((MathExprInlineModule)widget != null  &&  mathElements != null){
					mathElements.add(((MathExprInlineModule)widget).getContentsElement());
				}
				
				return widget.getElement();
			}
		}, new Vector<String>());
			
		ElementWrapperWidget promptWidget = new ElementWrapperWidget(promptElement);
		promptWidget.setStyleName("qp-text-inline");
		
		return promptWidget;
		
	}
}
