/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.qtitools.player.client.module.text;

import java.io.Serializable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IResponse;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.util.XMLUtils;

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
