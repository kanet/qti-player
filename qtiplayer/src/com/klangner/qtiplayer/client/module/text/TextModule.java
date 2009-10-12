package com.klangner.qtiplayer.client.module.text;

import java.util.HashMap;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IModule;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.IDomElementFactory;
import com.klangner.qtiplayer.client.util.XmlElement;

/**
 * Create Text which can contain gap and inline choice interactions.
 * 
 * @author klangner
 */
public class TextModule extends Widget implements IModule{
	
	/** response processing interface */
	private IResponse 	response;
	/** XML root */
	private XmlElement xmlRoot;
	/** All sub widgets */
	private HashMap<String, ITextControl>	controls = new HashMap<String, ITextControl>();

	
	/**
	 * constructor 
	 * @param node
	 */
	public TextModule(Element node, IResponse 	response){

		this.xmlRoot = new XmlElement(node);
		this.response = response;
		
		// Convert into text
		xmlRoot.setDomElementFactory(new IDomElementFactory(){
			public com.google.gwt.dom.client.Element createDomElement(Element xmlElement) {
				return createInlineWidget( xmlElement );
			}
			public boolean isSupportedElement(String tagName) {
				return (tagName.compareTo("inlineChoiceInteraction") == 0 ||
						tagName.compareTo("textEntryInteraction") == 0);
			}
		});
		
		setElement(xmlRoot.convertToHtml());
		setStyleName("qp-text-module");
		this.sinkEvents(Event.ONCHANGE);

	}

	/**
	 * @see IModule#markErrors()
	 */
	public void markErrors() {
		
		for(ITextControl control : controls.values()){
			control.setEnabled(false);
		}
	}

	/**
	 * @see IModule#reset()
	 */
	public void reset() {
	}

	/**
	 * @see IModule#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	}
	
	/**
	 * Catch inner controls events.
	 * Since events are not fired for internal Widgets. All events should be handled in this function
	 */
	public void onBrowserEvent(Event event){
		super.onBrowserEvent(event);
		
		if(event.getTypeInt() == Event.ONCHANGE){
			com.google.gwt.dom.client.Element element = 
				com.google.gwt.dom.client.Element.as(event.getEventTarget());
			
			ITextControl	handler = controls.get(element.getId());
			if(handler != null){
				handler.onChange();
			}
		}
	}

	/**
	 * Create inline choice 
	 * @param srcElement XML with content
	 * @param dstElement DOM element where this choice should be added
	 * @return Selection element
	 */
	private com.google.gwt.user.client.Element createInlineWidget(Element element){
		Widget	widget = null;
		String	id = Document.get().createUniqueId();

		if(element.getNodeName().compareTo("inlineChoiceInteraction") == 0){
			widget = new SelectionWidget(element, response);	
		}
		else if(element.getNodeName().compareTo("textEntryInteraction") == 0){
			widget = new TextEntryWidget(element, response);
		}
		
		if(widget != null){
			widget.getElement().setId(id);
			controls.put(id, (ITextControl)widget);
			
			return widget.getElement();
		}

		return null;
	}

}
