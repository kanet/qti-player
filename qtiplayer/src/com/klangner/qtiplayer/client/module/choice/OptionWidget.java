package com.klangner.qtiplayer.client.module.choice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.ModuleImageBundle;
import com.klangner.qtiplayer.client.util.XmlElement;

public class OptionWidget extends Composite implements IActivity{

	/** Option panel */
	private HorizontalPanel	panel;
	/** option button */
	private CheckBox	button;
	/** Response processing */
	private IResponse response;
	
	public OptionWidget(XmlElement element, IResponse response, boolean multi){
		
		this.response = response;
		
		panel = new HorizontalPanel();
		panel.setStyleName("qp-choice-option");
		
		if(multi)
			button = new CheckBox();
		else
			button = new RadioButton(Document.get().createUniqueId());
		button.setHTML(element.getTextAsHtml());
		button.setName(element.getAttributeAsString("identifier"));
		button.addValueChangeHandler(new OptionHandler());

		panel.add(button);
		initWidget(panel);
	}
	
	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
		ModuleImageBundle images = (ModuleImageBundle) GWT.create(ModuleImageBundle.class);

		panel.clear();
		if(button.getValue()){
			if( response.isCorrectAnswer(button.getName()) )
				panel.add(images.correct_icon().createImage());
			else
				panel.add(images.wrong_icon().createImage());
		}
//		else{
//			if( response.isCorrectAnswer(button.getName()) )
//				panel.add(images.wrong_icon().createImage());
//		}
		
		panel.add(button);
		setEnabled(false);
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {

		panel.clear();
		panel.add(button);
		setEnabled(true);
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
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
