package com.klangner.qtieditor.client.widget;

import com.google.gwt.user.client.ui.Label;

public class EditableLabel extends Label {

	/**
	 * Constructor
	 */
	public EditableLabel(){
		getElement().setAttribute("contenteditable", "true");
		setStyleName("qe-editable-label");
	}
}
