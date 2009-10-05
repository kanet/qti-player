package com.klangner.qtiplayer.client.modules;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public class OrderModule implements IModule {

	/** response processing interface */
	private IResponse 	response;
	
	private String text;

	/**
	 * Constructor
	 * @param node
	 * @param response
	 */
	public OrderModule(Node node, IResponse response){
		this.response = response;
		this.text = node.toString();
	}
	
	/**
	 * Create view
	 */
	public Widget getView() {
		return new Label(text);
	}

}
