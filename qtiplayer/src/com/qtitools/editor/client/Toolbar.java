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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class Toolbar extends Composite {

	/**
	 * List of buttons
	 */
	private Button addButton;
	private Button removeButton;
	private Button addPButton;
	private Button addImgButton;
	private Button addChoiceIButton;

	/**
	 * Constructor
	 */
	public Toolbar(){

		initWidget(createView());
		setStyleName("qe-toolbar");
	}

	/**
	 * @return view with player
	 */
	private Widget createView(){

		FlowPanel mainPanel;

		mainPanel = new FlowPanel();
		mainPanel.setStyleName("qe-toolbar");
		addButton = new Button("Add...");
		mainPanel.add(addButton);
		removeButton = new Button("Remove Page");
		mainPanel.add(removeButton);
		addPButton = new Button("Add P");
		mainPanel.add(addPButton);
		addImgButton = new Button("Add Img");
		mainPanel.add(addImgButton);
		addChoiceIButton = new Button("Add Choice");
		mainPanel.add(addChoiceIButton);

		return mainPanel;
	}

	/**
	 * @return addPButton
	 */
	public Button getPButton()
	{
		return addPButton;
	}

	/**
	 * @return addPButton
	 */
	public Button getImgButton()
	{
		return addImgButton;
	}

	/**
	 * @return addPButton
	 */
	public Button getChoiceButton()
	{
		return addChoiceIButton;
	}
}
