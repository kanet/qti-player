package com.klangner.qtiplayer.client.module.choice;

import java.io.Serializable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.XmlElement;

public class OptionWidget extends Composite implements IActivity, IStateful{

	/** socket */
//	private IModuleSocket moduleSocket;
	/** response processing interface */
	private IResponse 	response;
	/** Option panel */
	private HorizontalPanel	panel;
	/** option button */
	private CheckBox	button;
	/** option identifier */
	private String		identifier;
	
	
	public OptionWidget(XmlElement element, IModuleSocket moduleSocket, 
			String responseId, boolean multi){
		
//		this.moduleSocket = moduleSocket;
		this.response = moduleSocket.getResponse(responseId); 
		this.identifier = element.getAttributeAsString("identifier");
		
		panel = new HorizontalPanel();
		panel.setStyleName("qp-choice-option");
		
		if(multi)
			button = new CheckBox();
		else
			button = new RadioButton(responseId);
		button.setHTML(element.getTextAsHtml());
		button.addValueChangeHandler(new OptionHandler());

		panel.add(button);
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
	 * Handler for chech option.
	 * @author klangner
	 *
	 */
	class OptionHandler implements ValueChangeHandler<Boolean>{

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			CheckBox button = (CheckBox)event.getSource();
			if(button.getValue())
				response.set(identifier);
			else
				response.unset(identifier);
		}
	}

}
