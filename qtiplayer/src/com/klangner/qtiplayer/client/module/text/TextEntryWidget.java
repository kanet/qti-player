package com.klangner.qtiplayer.client.module.text;

import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.XmlElement;

public class TextEntryWidget extends TextBox implements ITextControl, IActivity{

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

	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
		
		setEnabled(false);
		if( response.isCorrectAnswer(lastValue) )
			setStyleName("qp-text-textentry-correct");
		else
			setStyleName("qp-text-textentry-wrong");
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {
		setEnabled(true);
		setStyleName("");
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
		setEnabled(false);
	}
}
