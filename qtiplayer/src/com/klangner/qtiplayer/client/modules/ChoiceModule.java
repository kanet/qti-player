package com.klangner.qtiplayer.client.modules;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.util.DomUtils;
import com.klangner.qtiplayer.client.util.RandomizedSet;

public class ChoiceModule implements IModule {

	/** Id for grouping radio buttons */
	private static int 	choiceID = 1;
	/** root element for this module */
	private Element			choiceNode;
	/** Work mode single or multiple choice */
	private boolean 		multi = false;
	/** Suffle? */
	private boolean 		shuffle = false;
	/** response processing interface */
	private IResponse 	response;
	
	
	public ChoiceModule(Element choiceNode, IResponse response){
		
		this.choiceNode = choiceNode;
		this.response = response;
		this.multi = (DomUtils.getAttribute(choiceNode, "maxChoices").compareTo("1") != 0);
		this.shuffle = (DomUtils.getAttribute(choiceNode, "shuffle").compareTo("true") == 0);
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
	 * Create option button
	 * @return
	 */
	private CheckBox createOptionButton(Element option){
		
		CheckBox button;
		
		if(multi)
			button = new CheckBox();
		else
			button = new RadioButton("choice"+choiceID);
		button.setStyleName("qp-choice-option");
		button.setName(option.getAttribute("identifier"));
		button.setText(option.getFirstChild().getNodeValue());
		button.addValueChangeHandler(new OptionHandler());

		return button;
	}
	
	
	/**
	 * Get options view
	 * @return
	 */
	private Widget getOptionsView(){
		
		VerticalPanel 	panel = new VerticalPanel();
		NodeList 				options = choiceNode.getElementsByTagName("simpleChoice");
		RandomizedSet<Element>	randomizedNodes = new RandomizedSet<Element>();

		// Add randomized nodes to shuffle table
		if(shuffle){
			for(int i = 0; i < options.getLength(); i++){
				Element			option = (Element)options.item(i);
				if(DomUtils.getAttribute(option, "fixed").compareTo("true") != 0)
					randomizedNodes.push(option);
			}
		}
		
		// Create buttons
		for(int i = 0; i < options.getLength(); i++){
			Element			option = (Element)options.item(i);
			CheckBox button;
			
			if(shuffle && DomUtils.getAttribute(option, "fixed").compareTo("true") != 0){
				option = randomizedNodes.pull();
			}
			
			button = createOptionButton(option);
			panel.add(button);
		}
		
		choiceID ++;
		return panel;
	}
	
	
	/**
	 * Get prompt
	 * @return
	 */
	private Widget getPromptView(){
		
		HTML	promptHTML = new HTML();
		NodeList prompts = choiceNode.getElementsByTagName("prompt");
		
		promptHTML.setStyleName("qp-choice-prompt");
		if(prompts.getLength() > 0){
			// TODO Zamienic to na html
			promptHTML.setHTML(prompts.item(0).getFirstChild().getNodeValue());
		}
		
		return promptHTML;
		
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
