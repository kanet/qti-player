package com.qtitools.player.client.module.order;

import java.io.Serializable;
import java.util.Vector;

import org.mortbay.util.ajax.JSON;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.components.htmldnd.DragContainerPanel;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.choice.SimpleChoice;
import com.qtitools.player.client.util.xml.XMLUtils;

public class OrderModule extends Composite implements IInteractionModule {
	
	/** response processing interface */
	private Response response;
	/** response id */
	private String responseIdentifier;
	/** Shuffle? */
	private boolean shuffle = false;
	/** option widgets */
	private Vector<SimplePanel> interactionElements;
	
	private DragContainerPanel container;
	private VerticalPanel mainPanel;

	public OrderModule(Element element, IModuleSocket moduleSocket) {

		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		
		extractOptionsWidgets(element);
		
		container = new DragContainerPanel();
		container.setStylePrimaryName("qp-order-module");
		
		mainPanel = new VerticalPanel();
		mainPanel.add(getPromptView(element));
		mainPanel.add(container);
		
		initWidget(mainPanel);
	}

	@Override
	public void onOwnerAttached() {
		// TODO Auto-generated method stub

	}

	/**
	 * Get prompt
	 * @return
	 */
	private Widget getPromptView(Element element){
		
		HTML	promptHTML = new HTML();
		Element prompt = XMLUtils.getFirstElementWithTagName(element, "prompt");
		
		promptHTML.setStyleName("qp-choice-prompt");
		
		if(prompt != null){
			promptHTML.setHTML(prompt.getFirstChild().getNodeValue());
		}
		
		return promptHTML;
		
	}
	
	private void extractOptionsWidgets(Element element){
		
	}

	@Override
	public void markAnswers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showCorrectAnswers() {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable getState() {
		return null;
	}

	@Override
	public void setState(Serializable newState) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleEvent(String tagID, Event event) {
		// TODO Auto-generated method stub

	}

}
