package com.klangner.qtiplayer.client.widget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.util.XmlElement;
import com.klangner.qtiplayer.client.util.RandomizedSet;

public class ChoiceModule implements IModule {

	/** Id for grouping radio buttons */
	private static int 	choiceID = 1;
	/** root element for this module */
	private XmlElement	choiceElement;
	/** Work mode single or multiple choice */
	private boolean 		multi = false;
	/** Suffle? */
	private boolean 		shuffle = false;
	/** response processing interface */
	private IResponse 	response;
	
	
	public ChoiceModule(Element choiceNode, IResponse response){
		
		choiceElement = new XmlElement(choiceNode);

		this.response = response;
		this.multi = (choiceElement.getAttributeAsInt("maxChoices") != 1);
		this.shuffle = choiceElement.getAttributeAsBoolean("shuffle");
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
	private CheckBox createOptionButton(XmlElement option){
		
		CheckBox button;
		
		if(multi)
			button = new CheckBox();
		else
			button = new RadioButton("choice"+choiceID);
		button.setStyleName("qp-choice-option");
		button.setName(option.getAttributeAsString("identifier"));
		button.setHTML(option.getTextAsHtml());
		button.addValueChangeHandler(new OptionHandler());

		return button;
	}
	
	
	/**
	 * Get options view
	 * @return
	 */
	private Widget getOptionsView(){
		
		VerticalPanel 	panel = new VerticalPanel();
		NodeList 				options = choiceElement.getElementsByTagName("simpleChoice");
		RandomizedSet<XmlElement>	randomizedNodes = new RandomizedSet<XmlElement>();

		// Add randomized nodes to shuffle table
		if(shuffle){
			for(int i = 0; i < options.getLength(); i++){
				XmlElement	option = new XmlElement((Element)options.item(i));
				if(!option.getAttributeAsBoolean("fixed"))
					randomizedNodes.push(option);
			}
		}
		
		// Create buttons
		for(int i = 0; i < options.getLength(); i++){
			XmlElement	option = new XmlElement((Element)options.item(i));
			CheckBox button;
			
			if(shuffle && !option.getAttributeAsBoolean("fixed") ){
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
		Element prompt = choiceElement.getElement("prompt");
		
		promptHTML.setStyleName("qp-choice-prompt");
		if(prompt != null){
			promptHTML.setHTML(prompt.getFirstChild().getNodeValue());
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
