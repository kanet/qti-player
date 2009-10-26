package com.klangner.qtiplayer.client.module.choice;

import java.io.Serializable;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Element;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.XMLUtil;

public class OptionWidget extends Composite implements IActivity, IStateful{

	/** socket */
//	private IModuleSocket moduleSocket;
	/** response processing interface */
	private IResponse 	response;
	/** Option panel */
	private Panel	panel;
	/** option button */
	private CheckBox	 button;
	/** feedback label */
	private Label      feedbackLabel = null;
	/** option identifier */
	private String		 identifier;
	/** feedback */
	private String     feedback = null;
	
	
	public OptionWidget(Element element, IModuleSocket moduleSocket, 
			String responseId, boolean multi){
		
//		this.moduleSocket = moduleSocket;
		this.response = moduleSocket.getResponse(responseId); 
		this.identifier = XMLUtil.getAttributeAsString(element, "identifier");
		
		panel = new VerticalPanel();
		panel.setStyleName("qp-choice-option");

		createButton(element, responseId, multi);

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
   * Create button
   * @param element XML element
   * @param multi check box or radio button?
   */
  private void createButton(Element element, String responseId, boolean multi) {

    if(multi)
      button = new CheckBox();
    else
      button = new RadioButton(responseId);
    button.setHTML(XMLUtil.getText(element));
    button.addValueChangeHandler(new OptionHandler());
    
    panel.add(button);

    Element feedbackInline = XMLUtil.getFirstElementWithTagName(element, "feedbackInline");
    
    if(feedbackInline != null){
      feedback = XMLUtil.getText(feedbackInline);
      feedbackLabel = new Label();
      panel.add(feedbackLabel);
    }
    
  }

  /**
   * Show or hide feedback
   * @param b
   */
  private void showFeedback(boolean show) {

    if( feedbackLabel != null ){
      if(show){
        feedbackLabel.setText(feedback);
      }
      else{
        feedbackLabel.setText("");
      }
    }
  }
	
	/**
	 * Handler for chech option.
	 * @author klangner
	 *
	 */
	private class OptionHandler implements ValueChangeHandler<Boolean>{

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			CheckBox button = (CheckBox)event.getSource();
			if(button.getValue()){
				response.set(identifier);
				showFeedback(true);
			}
			else{
				response.unset(identifier);
				showFeedback(false);
			}
		}

	}

}
