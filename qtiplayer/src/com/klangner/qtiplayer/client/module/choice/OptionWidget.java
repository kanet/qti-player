package com.klangner.qtiplayer.client.module.choice;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.XmlElement;

public class OptionWidget extends Composite {

	/** option button */
	private CheckBox	button;
	/** Response processing */
	private IResponse response;
	
	public OptionWidget(XmlElement element, IResponse response, boolean multi){
		
		this.response = response;
		
		if(multi)
			button = new CheckBox();
		else
			button = new RadioButton(Document.get().createUniqueId());
		button.setStyleName("qp-choice-option");
		button.setHTML(element.getTextAsHtml());
		button.setName(element.getAttributeAsString("identifier"));
		button.addValueChangeHandler(new OptionHandler());

		initWidget(button);
	}
	
	/**
	 * Make this widget read only
	 */
	public void setEnabled(boolean mode){
		button.setEnabled(mode);
	}
	
	/**
	 * Handler for chech option.
	 * @author klangner
	 *
	 */
	class OptionHandler implements ValueChangeHandler<Boolean>{

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			CheckBox button = (CheckBox)event.getSource();
			if(button.getValue())
				response.set(button.getName());
			else
				response.unset(button.getName());
		}
	}

	
}
