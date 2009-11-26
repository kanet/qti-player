package com.qtitools.editor.client.widget;

import com.google.gwt.user.client.ui.Label;

public class EditableLabel extends Label {

	/**
	 * Constructor
	 */
	public EditableLabel(){
		getElement().setAttribute("contentEditable", "true");
		setStyleName("qe-editable-label");
	}
}
