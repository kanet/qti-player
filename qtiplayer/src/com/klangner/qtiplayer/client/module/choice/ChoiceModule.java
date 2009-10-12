package com.klangner.qtiplayer.client.module.choice;

import java.util.Vector;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IModule;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.RandomizedSet;
import com.klangner.qtiplayer.client.util.XmlElement;

/**
 * Widget with choice implementation
 * @author Krzysztof Langner
 *
 */
public class ChoiceModule extends Composite implements IModule{

	/** root element for this module */
	private XmlElement			choiceElement;
	/** Work mode single or multiple choice */
	private boolean 				multi = false;
	/** Shuffle? */
	private boolean 				shuffle = false;
	/** option widgets */
	private Vector<OptionWidget>	options;
	/** response processing interface */
	private IResponse 			response;
	
	
	public ChoiceModule(Element choiceNode, IResponse response){
		
		choiceElement = new XmlElement(choiceNode);

		this.response = response;
		this.multi = (choiceElement.getAttributeAsInt("maxChoices") != 1);
		this.shuffle = choiceElement.getAttributeAsBoolean("shuffle");

		VerticalPanel vp = new VerticalPanel();
		
		vp.setStyleName("qp-choice-module");
		vp.add(getPromptView());
		vp.add(getOptionsView());
		
		initWidget(vp);
	}
	
	/**
	 * @see IModule#markErrors()
	 */
	public void markErrors() {
		
		for(OptionWidget option : options){
			option.setEnabled(false);
		}
		
	}

	/**
	 * @see IModule#reset()
	 */
	public void reset() {
	}

	/**
	 * @see IModule#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	}

	
	/**
	 * Get options view
	 * @return
	 */
	private Widget getOptionsView(){
		
		VerticalPanel 	panel = new VerticalPanel();
		NodeList 				optionNodes = choiceElement.getElementsByTagName("simpleChoice");
		RandomizedSet<XmlElement>	randomizedNodes = new RandomizedSet<XmlElement>();

		options = new Vector<OptionWidget>();
		// Add randomized nodes to shuffle table
		if(shuffle){
			for(int i = 0; i < optionNodes.getLength(); i++){
				XmlElement	option = new XmlElement((Element)optionNodes.item(i));
				if(!option.getAttributeAsBoolean("fixed"))
					randomizedNodes.push(option);
			}
		}
		
		// Create buttons
		for(int i = 0; i < optionNodes.getLength(); i++){
			XmlElement		option = new XmlElement((Element)optionNodes.item(i));
			OptionWidget 	button;
			
			if(shuffle && !option.getAttributeAsBoolean("fixed") ){
				option = randomizedNodes.pull();
			}
			
			button = new OptionWidget(option, response, multi);
			options.add(button);
			panel.add(button);
		}
		
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
	
}
