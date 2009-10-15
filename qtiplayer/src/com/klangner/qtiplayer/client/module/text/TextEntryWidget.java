package com.klangner.qtiplayer.client.module.text;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.XmlElement;

public class TextEntryWidget extends InlineHTML implements ITextControl, IActivity{

	/** response processing interface */
	private IResponse 	response;
  /** widget id */
  private String  id;
  /** text box control */
  private TextBox textBox;
	/** Last selected value */
	private String	lastValue = null;

	/**
	 * constructor
	 * @param response
	 */
	public TextEntryWidget(Element element, IResponse 	response){
		
		XmlElement xmlElement = new XmlElement(element);

    this.id = Document.get().createUniqueId();
		this.response = response;
		textBox = new TextBox();
		textBox.setMaxLength(xmlElement.getAttributeAsInt("expectedLength"));
		textBox.getElement().setId(id);
    getElement().appendChild(textBox.getElement());
	}
	
  /**
   * @see ITextControl#getID()
   */
  public String getID() {
    return id;
  }

	/**
	 * Process on change event 
	 */
	public void onChange(){
		
		if(lastValue != null)
			response.unset(lastValue);
		
		lastValue = textBox.getText();
		response.set(lastValue);
	}

	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
		
	  textBox.setEnabled(false);
		if( response.isCorrectAnswer(lastValue) )
			setStyleName("qp-text-textentry-correct");
		else
			setStyleName("qp-text-textentry-wrong");
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {
	  textBox.setEnabled(true);
		setStyleName("");
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	  textBox.setEnabled(false);
	}
}
