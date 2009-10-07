package com.klangner.qtiplayer.client.widget.inline;

import com.google.gwt.user.client.ui.ListBox;
import com.klangner.qtiplayer.client.widget.IResponse;

public class SelectionWidget extends ListBox implements IOnChangeHandler{

	/** response processing interface */
	private IResponse 	response;
	/** Last selected value */
	private String	lastValue = null;

	/**
	 * constructor
	 * @param response
	 */
	public SelectionWidget(IResponse 	response){
		
		this.response = response;
	}
	
	/**
	 * Process on change event 
	 */
	public void onChange(){
		
		if(lastValue != null)
			response.unset(lastValue);
		
		lastValue = getValue(getSelectedIndex());
		response.set(lastValue);
	}
	
}
