package com.klangner.qtiplayer.client.module.text;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IActivity;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.util.RandomizedSet;
import com.klangner.qtiplayer.client.util.XmlElement;

public class SelectionWidget extends InlineHTML implements ITextControl, IActivity{

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
		
		XmlElement choiceElement = new XmlElement(element);
		String		 responseIdentifier = choiceElement.getAttributeAsString("responseIdentifier"); 

		id = Document.get().createUniqueId();
		this.response = moduleSocket.getResponse(responseIdentifier);
		this.shuffle = choiceElement.getAttributeAsBoolean("shuffle");
		
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
	 * init widget view
	 * @param element
	 */
	private void init(Element inlineChoiceElement){
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				XmlElement choiceElement = new XmlElement((Element)nodes.item(i));
				listBox.addItem(choiceElement.getText(), choiceElement.getAttributeAsString("identifier"));
			}
		}
	}
	
	/**
	 * init widget view. Randomize options
	 * @param element
	 */
	private void initRandom(Element inlineChoiceElement){
		RandomizedSet<XmlElement>	randomizedNodes = new RandomizedSet<XmlElement>();
		NodeList nodes = inlineChoiceElement.getChildNodes();

		// Add no answer as first option
		listBox.addItem("");
		
		// Add nodes to temporary list
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				XmlElement choiceElement = new XmlElement((Element)nodes.item(i));
				randomizedNodes.push(choiceElement);
			}
		}
		
		while(randomizedNodes.hasMore()){
			XmlElement choiceElement = randomizedNodes.pull();
			listBox.addItem(choiceElement.getText(), choiceElement.getAttributeAsString("identifier"));
		}
		
	}

}
