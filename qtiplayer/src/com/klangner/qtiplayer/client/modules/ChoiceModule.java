package com.klangner.qtiplayer.client.modules;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class ChoiceModule implements IModule {

	private static int choiceID = 1;
	private Element	choiceNode;
	
	public ChoiceModule(Element choiceNode){
		
		this.choiceNode = choiceNode;
	}
	
	/**
	 * Create view
	 */
	public Widget getView() {
		VerticalPanel vp = new VerticalPanel();
		
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
			RadioButton radioButton = new RadioButton("choice"+choiceID);
			
			radioButton.setStyleName("qp-choice-option");
			radioButton.setName(option.getAttribute("identifier"));
			radioButton.setText(option.getFirstChild().getNodeValue());
			panel.add(radioButton);
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
}
