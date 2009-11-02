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
package com.klangner.qtiplayer.client.module.choice;

import java.io.Serializable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.IDomElementFactory;
import com.klangner.qtiplayer.client.util.XMLUtils;
import com.klangner.qtiplayer.client.util.XmlConverter;

public class OptionWidget extends Composite implements IActivity, IStateful{

	/** socket */
//	private IModuleSocket moduleSocket;
	/** response processing interface */
	private IResponse 	response;
	/** Option panel */
	private Panel	panel;
	/** option button */
	private CheckBox	 button;
	/** feedback label */
	private Label      feedbackLabel = null;
	/** option identifier */
	private String		 identifier;
	/** feedback */
	private String     feedback = null;
	/** Button group interface */
	private IButtonGroup buttonGroup;
	
	
	public OptionWidget(Element element, IModuleSocket moduleSocket, IButtonGroup buttonGroup){
		
//		this.moduleSocket = moduleSocket;
	  this.buttonGroup = buttonGroup;
		this.response = moduleSocket.getResponse(buttonGroup.getId()); 
		this.identifier = XMLUtils.getAttributeAsString(element, "identifier");
		
		panel = new VerticalPanel();
		panel.setStyleName("qp-choice-option");

		createButton(element);

		initWidget(panel);
	}

  /**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {

		if(button.getValue()){
			if( response.isCorrectAnswer(identifier) )
				button.setStyleName("qp-choice-selected-correct");
			else
				button.setStyleName("qp-choice-selected-wrong");
		}
		else{
			if( response.isCorrectAnswer(identifier) )
				button.setStyleName("qp-choice-notselected-wrong");
			else
				button.setStyleName("qp-choice-notselected-correct");
		}
		
		setEnabled(false);
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {

		button.setStyleName("");
		button.setValue(false);
		setEnabled(true);
		
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	}
	
  /**
   * @see IStateful#getState()
   */
  public Serializable getState() {
    return new Boolean(button.getValue());
  }

  /**
   * @see IStateful#setState(Serializable)
   */
  public void setState(Serializable newState) {
    Boolean state = (Boolean)newState;
    button.setValue(state);
  }
  

  /**
   * Return module id
   * @return
   */
  public String getId(){
    return identifier;
  }
  
  
	/**
	 * Make this widget read only
	 */
	public void setEnabled(boolean mode){
		button.setEnabled(mode);
	}
	
	
	/**
	 * Uncheck this option
	 */
	public void uncheck(){
	  button.setValue(false);
	  response.unset(identifier);
    showFeedback(false);
	}
	
  /** 
   * Create button
   * @param element XML element
   * @param multi check box or radio button?
   */
  private void createButton(Element element) {

    XmlConverter  converter = new XmlConverter(element);
    
    // Do not import feedbackInline tag
    converter.setDomElementFactory(new IDomElementFactory(){

      public com.google.gwt.dom.client.Element createDomElement(Element xmlElement) {
        // just skip this tag
        return null;
      }

      public boolean isSupportedElement(String tagName) {
        return (tagName.compareTo("feedbackInline") == 0);
      }
      
    });
    
    button = new CheckBox();
    button.setHTML(converter.getTextAsHtml());
    button.addValueChangeHandler(new OptionHandler());

    panel.add(button);

    Element feedbackInline = XMLUtils.getFirstElementWithTagName(element, "feedbackInline");
    
    if(feedbackInline != null){
      feedback = XMLUtils.getText(feedbackInline);
      feedbackLabel = new Label();
      panel.add(feedbackLabel);
    }
    
  }

  /**
   * Show or hide feedback
   * @param b
   */
  private void showFeedback(boolean show) {

    if( feedbackLabel != null ){
      if(show){
        feedbackLabel.setText(feedback);
      }
      else{
        feedbackLabel.setText("");
      }
    }
  }
	
	/**
	 * Handler for chech option.
	 * @author klangner
	 *
	 */
	private class OptionHandler implements ValueChangeHandler<Boolean>{

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			CheckBox button = (CheckBox)event.getSource();
			
			if(button.getValue()){
				response.set(identifier);
				showFeedback(true);
				buttonGroup.buttonSelected(OptionWidget.this);
			}
			else{
				response.unset(identifier);
				showFeedback(false);
			}
		}

	}

}
