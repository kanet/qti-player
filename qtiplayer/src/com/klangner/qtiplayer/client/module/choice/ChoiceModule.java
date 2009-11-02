/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.klangner.qtiplayer.client.module.choice;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.RandomizedSet;
import com.klangner.qtiplayer.client.util.XMLUtils;

/**
 * Widget with choice implementation
 * @author Krzysztof Langner
 *
 */
public class ChoiceModule extends Composite implements IActivity, IStateful{

	/** root element for this module */
	private Element			    choiceElement;
	/** response id */
	private String          responseIdentifier;
	/** Work mode single or multiple choice */
	private boolean 				multi = false;
	/** Shuffle? */
	private boolean 				shuffle = false;
	/** option widgets */
	private Vector<OptionWidget>	options;
	/** response processing interface */
	private IModuleSocket 	moduleSocket;
	
	
	public ChoiceModule(Element choiceNode, IModuleSocket moduleSocket){
		
		this.choiceElement = choiceNode;

		this.moduleSocket = moduleSocket;
		this.multi = (XMLUtils.getAttributeAsInt(choiceElement, "maxChoices") != 1);
		this.shuffle = XMLUtils.getAttributeAsBoolean(choiceElement, "shuffle");
    responseIdentifier = XMLUtils.getAttributeAsString(choiceElement, "responseIdentifier");

		VerticalPanel vp = new VerticalPanel();
		
		vp.setStyleName("qp-choice-module");
		vp.add(getPromptView());
		vp.add(getOptionsView());
		
		initWidget(vp);
	}
	
	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
		
		for(OptionWidget option : options){
			option.markAnswers();
		}
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {
		for(OptionWidget option : options){
			option.reset();
		}
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
		for(OptionWidget option : options){
			option.showCorrectAnswers();
		}
	}

	
  /**
   * @see IStateful#getState()
   */
  public Serializable getState() {
    HashMap<String, Serializable>  state = new HashMap<String, Serializable>();
    
    for(OptionWidget option : options){
      state.put(option.getId(), option.getState());
    }
    
    return state;
  }

  /**
   * @see IStateful#setState(Serializable)
   */
  @SuppressWarnings("unchecked")
  public void setState(Serializable newState) {

    if(newState instanceof HashMap){
      HashMap<String, Serializable> state = (HashMap<String, Serializable>)newState;
      
      for(OptionWidget option : options){
        option.setState(state.get(option.getId()));
      }
    }
  }
  
	/**
	 * Get options view
	 * @return
	 */
	private Widget getOptionsView(){
		
		VerticalPanel 	panel = new VerticalPanel();
		NodeList 				optionNodes = choiceElement.getElementsByTagName("simpleChoice");
		RandomizedSet<Element>	randomizedNodes = new RandomizedSet<Element>();

		
		options = new Vector<OptionWidget>();
		// Add randomized nodes to shuffle table
		if(shuffle){
			for(int i = 0; i < optionNodes.getLength(); i++){
				Element	option = (Element)optionNodes.item(i);
				if(!XMLUtils.getAttributeAsBoolean(option, "fixed"))
					randomizedNodes.push(option);
			}
		}
		
		// Create buttons
		for(int i = 0; i < optionNodes.getLength(); i++){
		  Element		option = (Element)optionNodes.item(i);
			OptionWidget 	button;
			
			if(shuffle && !XMLUtils.getAttributeAsBoolean(option, "fixed") ){
				option = randomizedNodes.pull();
			}
			
			button = new OptionWidget(option, moduleSocket, buttonGroup);
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
		Element prompt = XMLUtils.getFirstElementWithTagName(choiceElement, "prompt");
		
		promptHTML.setStyleName("qp-choice-prompt");
		if(prompt != null){
			promptHTML.setHTML(prompt.getFirstChild().getNodeValue());
		}
		
		return promptHTML;
		
	}
	
	private IButtonGroup buttonGroup = new IButtonGroup(){

    public void buttonSelected(OptionWidget option) {
      if( !multi ){
        for(OptionWidget child : options){
          if( child != option)
          child.uncheck();
        }
      }
    }

    public String getId() {
      return responseIdentifier;
    }
	  
	};

}
