package com.klangner.qtiplayer.client.module.text;

import java.io.Serializable;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;
import com.klangner.qtiplayer.client.util.RandomizedSet;
import com.klangner.qtiplayer.client.util.XMLUtils;

public class SelectionWidget extends InlineHTML implements ITextControl, IActivity, IStateful{

	/** response processing interface */
	private IResponse 	response;
	/** widget id */
	private String  id;
	/** panel widget */
	private ListBox  listBox;
	/** Shuffle? */
	private boolean 		shuffle = false;
	/** Last selected value */
	private String	lastValue = null;

	/**
	 * constructor
	 * @param moduleSocket
	 */
	public SelectionWidget(Element element, IModuleSocket moduleSocket){
		
		String		 responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier"); 

		id = Document.get().createUniqueId();
		this.response = moduleSocket.getResponse(responseIdentifier);
		this.shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");
		
    listBox = new ListBox();
		if(shuffle)
			initRandom(element);
		else
			init(element);

		listBox.getElement().setId(id);
		getElement().appendChild(listBox.getElement());
	}
	
  /**
   * @see ITextControl#getID()
   */
  public String getID() {
    return id;
  }

  /**
	 * Process on change event 
	 */
	public void onChange(){
		
		if(lastValue != null)
			response.unset(lastValue);
		
		lastValue = listBox.getValue(listBox.getSelectedIndex());
		response.set(lastValue);
	}

	/**
	 * @see IActivity#markAnswers()
	 */
	public void markAnswers() {
	  listBox.setEnabled(false);
		if( response.isCorrectAnswer(lastValue) )
			setStyleName("qp-text-choice-correct");
		else
			setStyleName("qp-text-choice-wrong");
	}

	/**
	 * @see IActivity#reset()
	 */
	public void reset() {
	  listBox.setEnabled(true);
		setStyleName("");
	}

	/**
	 * @see IActivity#showCorrectAnswers()
	 */
	public void showCorrectAnswers() {
	  listBox.setEnabled(false);
	}
	
  /**
   * @see IStateful#getState()
   */
  public Serializable getState() {
    return lastValue;
  }

  /**
   * @see IStateful#setState(Serializable)
   */
  public void setState(Serializable newState) {
    String state = (String)newState;
    
    lastValue = state;
    for(int i = 0; i < listBox.getItemCount(); i++){
      if( listBox.getValue(i).compareTo(state) == 0){
        listBox.setSelectedIndex(i);
        break;
      }
    }
  }
  
	/**
	 * init widget view
	 * @param element
	 */
	private void init(Element inlineChoiceElement){
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				Element choiceElement = (Element)nodes.item(i);
				listBox.addItem(XMLUtils.getText(choiceElement), 
				    XMLUtils.getAttributeAsString(choiceElement, "identifier"));
			}
		}
	}
	
	/**
	 * init widget view. Randomize options
	 * @param element
	 */
	private void initRandom(Element inlineChoiceElement){
		RandomizedSet<Element>	randomizedNodes = new RandomizedSet<Element>();
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		// Add nodes to temporary list
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				randomizedNodes.push((Element)nodes.item(i));
			}
		}
		
		while(randomizedNodes.hasMore()){
			Element choiceElement = randomizedNodes.pull();
      listBox.addItem(XMLUtils.getText(choiceElement), 
          XMLUtils.getAttributeAsString(choiceElement, "identifier"));
		}
		
	}

}
