package com.klangner.qtiplayer.client.widget.inline;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.XmlElement;

public class TextEntryWidget extends TextBox implements IOnChangeHandler{

	/** response processing interface */
	private IResponse 	response;
	/** Last selected value */
	private String	lastValue = null;

	/**
	 * constructor
	 * @param response
	 */
	public TextEntryWidget(Element element, IResponse 	response){
		
		XmlElement xmlElement = new XmlElement(element);
		this.response = response;
		setMaxLength(xmlElement.getAttributeAsInt("expectedLength"));
	}
	
	/**
	 * Process on change event 
	 */
	public void onChange(){
		
		if(lastValue != null)
			response.unset(lastValue);
		
		lastValue = getText();
		response.set(lastValue);
	}

}
