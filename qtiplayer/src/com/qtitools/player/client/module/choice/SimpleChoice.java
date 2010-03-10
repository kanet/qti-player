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
package com.qtitools.player.client.module.choice;

import java.util.Vector;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.components.AccessibleCheckBox;
import com.qtitools.player.client.util.xml.XMLConverter;
import com.qtitools.player.client.util.xml.XMLUtils;

public class SimpleChoice extends Composite {

	public String identifier;
	
	public String feedback;
	public Label feedbackLabel;

	private AccessibleCheckBox button;
	
	public String inputId;
	
	public SimpleChoice(Element element, String inputId) {
		
		this.inputId = inputId;

		identifier = XMLUtils.getAttributeAsString(element, "identifier");
		
		// button

	    button = new AccessibleCheckBox();
		Vector<String> ignoredTags = new Vector<String>();
		ignoredTags.add("feedbackInline");
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM(element, ignoredTags);
	    button.setHTML(dom.getInnerHTML());
		((com.google.gwt.dom.client.Element)button.getElement().getElementsByTagName("input").getItem(0)).setId(inputId);
		
		
		// panel

		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("qp-choice-option");
		panel.add(button);
		
		initWidget(panel);
		
		// feedback

	    Element feedbackInline = XMLUtils.getFirstElementWithTagName(element, "feedbackInline");
	    if(feedbackInline != null){
	    	feedback = XMLUtils.getText(feedbackInline);
			feedbackLabel = new Label();
			panel.add(feedbackLabel);
	    }
	    
	}

	public void markAnswers(boolean correct) {

		if(button.getValue()){
			if( correct )
				button.setStyleName("qp-choice-selected-correct");
			else
				button.setStyleName("qp-choice-selected-wrong");
		}
		else{
			if( correct )
				button.setStyleName("qp-choice-notselected-wrong");
			else
				button.setStyleName("qp-choice-notselected-correct");
		}
		
		setEnabled(false);

	}
	

	public void setSelected(boolean sel){
		button.setChecked(sel);
	}

	public boolean isSelected(){
		boolean isc = button.isChecked();
		return isc;
	}

	/**
	 * Make this widget read only
	 */
	public void setEnabled(boolean mode){
		button.setEnabled(mode);
		showFeedback(false);
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	

	/**
	 * @return the input ID
	 */
	public String getInputId() {
		return inputId;
	}

	public void reset(){
		button.setStyleName("");
		button.setValue(false);
		setEnabled(true);
	}

	/**
	 * Show or hide feedback
	 * @param show 
	 */
	public void showFeedback(boolean show) {

		if( feedbackLabel != null ){
			if(show){
				feedbackLabel.setText(feedback);
			}
			else{
				feedbackLabel.setText("");
			}
		}
	}

}
