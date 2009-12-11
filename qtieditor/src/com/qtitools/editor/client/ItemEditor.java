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
package com.qtitools.editor.client;

import com.google.gwt.xml.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.model.AssessmentItem;

public class ItemEditor extends Composite{

	/** Item editor panel */
	private Panel 					bodyPanel;
	/** page title box */
	private TextBox         itemTitleLabel;


	/**
	 * Constructor
	 */

	public ItemEditor(){

		initWidget(createView());
	}


	/**
	 * @return view with player
	 */
	private Widget createView(){

		Panel mainPanel;
		HorizontalPanel panel;
		Label label;

		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("qe-item-editor");
		
		panel = new HorizontalPanel();
		panel.setWidth("100%");
		panel.setStyleName("qe-a-item");
		label = new Label("Title:");
		label.setStyleName("qe-a-item-title");
		panel.add(label);
		panel.setCellWidth(label,"60px");
		itemTitleLabel = new TextBox();
		itemTitleLabel.setStyleName("qe-a-item-textbox");
		panel.add(itemTitleLabel);
		mainPanel.add(panel);
		HTML hr = new HTML();
		hr.setWidth("100%");
		hr.setStyleName("line");
		mainPanel.add(hr);

		bodyPanel = new VerticalPanel();
		bodyPanel.setStyleName("qe-page-body");
		mainPanel.add(bodyPanel);

		return mainPanel;
	}

	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showPage(AssessmentItem assessmentItem){

		bodyPanel.clear();
		itemTitleLabel.setText(assessmentItem.getTitle());

		for(int i = 0; i < assessmentItem.getModuleCount(); i++){
			bodyPanel.add(assessmentItem.getModule(i));
		}

	}

	/**
	 * Adding new module p to existing modules
	 */
	public void addP(AssessmentItem assessmentItem){
		newModule("p");
		showPage(assessmentItem);
	}

	/**
	 * Adding new module choice to existing modules
	 */
	public void addChoice(AssessmentItem assessmentItem){
		Element newElement = newModule("choiceInteraction");
		newElement.setAttribute("responseIdentifier", "RESPONSE");
		newElement.setAttribute("shuffle", "false");
		newElement.setAttribute("maxChoices", "1");
		showPage(assessmentItem);
	}
	
	 private Element newModule(String tagName){
	   /*
	       Widget module;
	       Element newElement = getDom().createElement(tagName);
	       Node itemBody = getDom().getElementsByTagName("itemBody").item(0);
	       itemBody.appendChild(newElement);
	       module = moduleFactory.createModule(newElement, moduleSocket);
	       modules.addElement(module);
	       return newElement;
	   */
	       return null; //TODO: implement and/or change way of adding modules
	     }

}
