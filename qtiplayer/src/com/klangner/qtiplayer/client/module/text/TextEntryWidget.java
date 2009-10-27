package com.klangner.qtiplayer.client.module.text;

import java.io.Serializable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.XMLUtils;

public class TextEntryWidget extends InlineHTML implements ITextControl, IActivity, IStateful{

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
	 * @param moduleSocket
	 */
	public TextEntryWidget(Element element, IModuleSocket moduleSocket){

		String			responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier"); 

    this.id = Document.get().createUniqueId();
		this.response = moduleSocket.getResponse(responseIdentifier);
		textBox = new TextBox();
		textBox.setMaxLength(XMLUtils.getAttributeAsInt(element, "expectedLength"));
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
	
  /**
   * @see IStateful#getState()
   */
  public Serializable getState() {
    return textBox.getText();
  }

  /**
   * @see IStateful#setState(Serializable)
   */
  public void setState(Serializable newState) {
    String state = (String)newState;
    textBox.setText(state);
    lastValue = state;
  }
  

}
