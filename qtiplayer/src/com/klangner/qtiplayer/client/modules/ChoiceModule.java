package com.klangner.qtiplayer.client.modules;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class ChoiceModule implements IModule {

	/** Id for grouping radio buttons */
	private static int 	choiceID = 1;
	/** root element for this module */
	private Element			choiceNode;
	/** Work mode single or multiple choice */
	private boolean 		multi = false;
	/** response processing interface */
	private IResponse 	response;
	
	
	public ChoiceModule(Element choiceNode, IResponse response){
		
		this.choiceNode = choiceNode;
		this.response = response;
		String maxChoices = choiceNode.getAttribute("maxChoices");
		multi = (maxChoices != null && maxChoices.compareTo("1") != 0);
	}
	
	/**
	 * Create view
	 */
	public Widget getView() {
		VerticalPanel vp = new VerticalPanel();
		
		vp.setStyleName("qp-choice-module");
		vp.add(getPromptView());
		vp.add(getOptionsView());
		
		return vp;
	}

	/**
	 * Get options view
	 * @return
	 */
	private Widget getOptionsView(){
		
		VerticalPanel panel = new VerticalPanel();
		NodeList options = choiceNode.getElementsByTagName("simpleChoice");
		
		for(int i = 0; i < options.getLength(); i++){
			Element			option = (Element)options.item(i);
			CheckBox button;
			
			if(multi)
				button = new CheckBox();
			else
				button = new RadioButton("choice"+choiceID);
			button.setStyleName("qp-choice-option");
			button.setName(option.getAttribute("identifier"));
			button.setText(option.getFirstChild().getNodeValue());
			button.addValueChangeHandler(new OptionHandler());
			panel.add(button);
		}
		
		return panel;
		
	}
	/**
	 * Get prompt
	 * @return
	 */
	private Widget getPromptView(){
		
		Label	label = new Label();
		NodeList prompts = choiceNode.getElementsByTagName("prompt");
		
		label.setStyleName("qp-choice-prompt");
		if(prompts.getLength() > 0){
			label.setText(prompts.item(0).getFirstChild().getNodeValue());
		}
		
		return label;
		
	}
	
	class OptionHandler implements ValueChangeHandler<Boolean>{

		public void onValueChange(ValueChangeEvent<Boolean> event) {
			CheckBox button = (CheckBox)event.getSource();
			if(button.getValue())
				response.set(button.getName());
			else
				response.unset(button.getName());
		}
	};
}
