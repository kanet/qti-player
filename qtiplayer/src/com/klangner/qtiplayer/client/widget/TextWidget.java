package com.klangner.qtiplayer.client.widget;

import java.util.HashMap;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.util.XmlElement;
import com.klangner.qtiplayer.client.widget.inline.IOnChangeHandler;
import com.klangner.qtiplayer.client.widget.inline.SelectionWidget;

/**
 * Create Text which can contain gap and inline choice interactions.
 * 
 * @author klangner
 */
public class TextWidget extends Widget{
	
	/** response processing interface */
	private IResponse 	response;
	/** XML root */
	private Element xmlRoot;
	/** All sub widgets */
	private HashMap<String, IOnChangeHandler>	children = new HashMap<String, IOnChangeHandler>();

	
	/**
	 * constructor 
	 * @param node
	 */
	public TextWidget(Element node, IResponse 	response){

		this.xmlRoot = node;
		this.response = response;
		setElement(Document.get().createElement(node.getNodeName()));
		setStyleName("qp-text-module");
		
		copyChildren(xmlRoot, getElement());
		this.sinkEvents(Event.ONCHANGE);

	}

	/**
	 * Catch inner controls events.
	 * Since events are not fired for internal Widgets. All events should be handled in this function
	 */
	public void onBrowserEvent(Event event){
		super.onBrowserEvent(event);
		
		if(event.getTypeInt() == Event.ONCHANGE){
			com.google.gwt.dom.client.Element element = 
				com.google.gwt.dom.client.Element.as(event.getEventTarget());
			
			IOnChangeHandler	handler = children.get(element.getId());
			if(handler != null){
				handler.onChange();
			}
		}
	}

	
  /**
	 * copy content from XML into DOM
	 * @param node
	 * @param element 
	 */
	private void copyChildren(Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NodeList	nodes = srcElement.getChildNodes();
		Document	doc = Document.get();
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				dstElement.appendChild(doc.createTextNode(node.getNodeValue()));
			}
			else if(node.getNodeName().compareTo("inlineChoiceInteraction") == 0){
				dstElement.appendChild( createInlineChoice((Element)node ) );
			}
			else{
				Element xmlElement = (Element)node;
				com.google.gwt.dom.client.Element domElement;
				domElement = doc.createElement(node.getNodeName());
				if(domElement != null){
					dstElement.appendChild(domElement);
					// Copy attributes
					copyAttributes(xmlElement, domElement);
					// Add children
					copyChildren(xmlElement, domElement);
				}
			}
		}
	}

	/**
	 * copy html attributes from XML into DOM
	 * @param srcElement
	 * @param dstElement
	 */
	private void copyAttributes(Element srcElement, com.google.gwt.dom.client.Element dstElement){
		NamedNodeMap attributes = srcElement.getAttributes();
		
		for(int i = 0; i < attributes.getLength(); i++){
			Node attribute = attributes.item(i);
			dstElement.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
		}
	}

	
	/**
	 * Create inline choice 
	 * @param srcElement XML with content
	 * @param dstElement DOM element where this choice should be added
	 * @return Selection element
	 */
	private com.google.gwt.user.client.Element createInlineChoice(Element inlineChoiceElement){
		NodeList nodes = inlineChoiceElement.getChildNodes();
		SelectionWidget	listBox = new SelectionWidget(response);
		String	id = Document.get().createUniqueId();

		// Add to children list;
		// Add no answer option
		listBox.addItem("");
		
		for(int i = 0; i < nodes.getLength(); i++){
			if(nodes.item(i).getNodeName().compareTo("inlineChoice") == 0){
				XmlElement choiceElement = new XmlElement((Element)nodes.item(i));
				listBox.addItem(choiceElement.getText(), choiceElement.getAttributeAsString("identifier"));
			}
		}
		
		// Add change handler
		listBox.getElement().setId(id);
		children.put(id, listBox);
		
		return listBox.getElement();
		
	}
	
}
