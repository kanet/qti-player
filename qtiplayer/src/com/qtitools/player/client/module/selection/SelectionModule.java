package com.qtitools.player.client.module.selection;

import java.util.Vector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.components.AccessibleRadioButton;
import com.qtitools.player.client.model.feedback.InlineFeedback;
import com.qtitools.player.client.model.feedback.InlineFeedbackSocket;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.CommonsFactory;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.util.RandomizedSet;
import com.qtitools.player.client.util.xml.XMLUtils;

public class SelectionModule extends Composite implements IInteractionModule {

	public SelectionModule(Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener){

		shuffle = XMLUtils.getAttributeAsBoolean(element, "shuffle");
		
		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		stateListener = (ModuleStateChangedEventsListener)moduleEventsListener;
		
		NodeList choices = element.getElementsByTagName("simpleChoice");
		NodeList items = element.getElementsByTagName("item");
		
		grid = new Grid(items.getLength()+1, choices.getLength()+1);
		grid.setStyleName("qp-selection-table");
		fillGrid(choices, items, moduleSocket);
		
		panel = new VerticalPanel();
		panel.setStyleName("qp-selection-module");
		panel.add(CommonsFactory.getPromptView(XMLUtils.getFirstElementWithTagName(element, "prompt")));
		panel.add(grid);
		
		initWidget(panel);

		NodeList childNodes = element.getChildNodes();
		for (int f = 0 ; f < childNodes.getLength() ; f ++){
			if (childNodes.item(f).getNodeName().compareTo("feedbackInline") == 0)
				moduleSocket.add(new InlineFeedback(grid, childNodes.item(f)));
		}
	}

	/** response processing interface */
	private Response response;
	/** module state changed listener */
	private ModuleStateChangedEventsListener stateListener;
	/** response id */
	private String responseIdentifier;
	/** Shuffle answers */
	private boolean shuffle = false;
	
	private VerticalPanel panel;
	private Grid grid;
	private Vector<Vector<AccessibleRadioButton>> buttons;
	private Vector<Vector<String>> buttonIds;
	private Vector<String> choiceIdentifiers;
	private Vector<String> itemIdentifiers;

	private boolean showingAnswers = false;
	private boolean locked = false;

	@Override
	public void onOwnerAttached() {
		// TODO Auto-generated method stub

	}
	
	private void fillGrid(NodeList choices, NodeList items, InlineFeedbackSocket inlineFeedbackSocket){
		buttons = new Vector<Vector<AccessibleRadioButton>>();

		// header - choices
		
		choiceIdentifiers = new Vector<String>();
		for (int c = 0 ; c < choices.getLength() ; c ++){
			Vector<String> ignoredTags = new Vector<String>();
			ignoredTags.add("feedbackInline");
			
			Widget choiceView = CommonsFactory.getInlineTextView((Element)choices.item(c), ignoredTags);
			choiceView.setStyleName("qp-selection-choice");
			
			grid.setWidget(0, c+1, choiceView);
			
			choiceIdentifiers.add( ((Element)choices.item(c)).getAttribute("identifier") );

			NodeList inlineFeedbackNodes = ((Element)choices.item(c)).getElementsByTagName("feedbackInline");
			for (int f = 0 ; f < inlineFeedbackNodes.getLength() ; f ++){
				inlineFeedbackSocket.add(new InlineFeedback(choiceView, inlineFeedbackNodes.item(f)));
			}
		}
		
		// body - items
		Vector<Node> itemNodes = new Vector<Node>();
		if (shuffle){
			RandomizedSet<Node> randomizedItemNodes = new RandomizedSet<Node>();
			for (int i = 0 ; i < items.getLength() ; i ++){
				if(!XMLUtils.getAttributeAsBoolean((Element)items.item(i), "fixed")){
					randomizedItemNodes.push(items.item(i));
				}
			}
			for (int i = 0 ; i < items.getLength() ; i ++){
				if(!XMLUtils.getAttributeAsBoolean((Element)items.item(i), "fixed")){
					itemNodes.add(randomizedItemNodes.pull());
				} else {
					itemNodes.add(items.item(i));
				}
			}
		} else {
			for (int i = 0 ; i < items.getLength() ; i ++){
				itemNodes.add(items.item(i));
			}
		}
		
		itemIdentifiers = new Vector<String>();
		for (int i = 0 ; i < itemNodes.size() ; i ++){
			Vector<String> ignoredTags = new Vector<String>();
			ignoredTags.add("feedbackInline");
			
			Widget itemView = CommonsFactory.getInlineTextView((Element)itemNodes.get(i), ignoredTags);
			itemView.setStyleName("qp-selection-item");
			
			grid.setWidget(i+1, 0, itemView);
			
			itemIdentifiers.add( ((Element)itemNodes.get(i)).getAttribute("identifier") );

			NodeList inlineFeedbackNodes = ((Element)itemNodes.get(i)).getElementsByTagName("feedbackInline");
			for (int f = 0 ; f < inlineFeedbackNodes.getLength() ; f ++){
				inlineFeedbackSocket.add(new InlineFeedback(itemView, inlineFeedbackNodes.item(f)));
			}
		}
		
		// body - buttons
		buttonIds = new Vector<Vector<String>>();
		for (int i = 0 ; i < items.getLength() ; i ++){
			buttons.add(new Vector<AccessibleRadioButton>());
			buttonIds.add(new Vector<String>());
			for (int c = 0 ; c < choices.getLength() ; c ++){
				AccessibleRadioButton arb = new AccessibleRadioButton("btns"+String.valueOf(i));
				arb.setStyleName("qp-selection-button");
				
				String buttonId = Document.get().createUniqueId();
			    com.google.gwt.dom.client.Element buttonElement = (com.google.gwt.dom.client.Element)arb.getElement();
				(buttonElement.getElementsByTagName("input").getItem(0)).setId(buttonId);
				buttonIds.get(i).add(buttonId);
				
				grid.setWidget(i+1, c+1, arb);
				buttons.get(i).add(arb);
			}
		}
	}
	
	@Override
	public String getIdentifier() {
		return responseIdentifier;
	}

	@Override
	public void lock(boolean l) {
		if (locked != l){
			locked = l;
			for (int i = 0 ; i < buttons.size() ; i ++){
				for (int c = 0 ; c < buttons.get(i).size() ; c ++){
					buttons.get(i).get(c).setEnabled(!locked);
				}
			}
			
		}

	}

	@Override
	public void markAnswers(boolean mark) {
		if (mark){
			for (int r = 0 ; r < response.correctAnswers.size() ; r ++){
				String currItemIdentifier = response.correctAnswers.get(r).split(" ")[0];
				if (itemIdentifiers.indexOf(currItemIdentifier) == -1)
					continue;
				if (response.values.contains(response.correctAnswers.get(r))){
					grid.getWidget(itemIdentifiers.indexOf(currItemIdentifier)+1, 0).setStyleName("qp-selection-item-correct");
				} else {
					grid.getWidget(itemIdentifiers.indexOf(currItemIdentifier)+1, 0).setStyleName("qp-selection-item-wrong");
				}
			}
		} else {
			for (int i = 0 ; i < itemIdentifiers.size() ; i ++){
				grid.getWidget(i+1, 0).setStyleName("qp-selection-item");
			}
		}

	}

	@Override
	public void reset() {
		clearAnswers();
	}
	
	private void clearAnswers(){

		for (int i = 0 ; i < buttons.size() ; i ++){
			for (int c = 0 ; c < buttons.get(i).size() ; c ++){
				if (buttons.get(i).get(c).isChecked())
					buttons.get(i).get(c).setChecked(false);
			}
		}

	}

	@Override
	public void showCorrectAnswers(boolean show) {
		if (show  &&  !showingAnswers){
			showingAnswers = true;
			clearAnswers();

			for (int i = 0 ; i < itemIdentifiers.size() ; i ++){
				for (int c = 0 ; c < choiceIdentifiers.size() ; c ++){
					if (response.correctAnswers.contains(itemIdentifiers.get(i) + " " + choiceIdentifiers.get(c)))
						buttons.get(i).get(c).setChecked(true);
				}
			}
				
		} else if (!show  &&  showingAnswers) {
			clearAnswers();

			for (int i = 0 ; i < itemIdentifiers.size() ; i ++){
				for (int c = 0 ; c < choiceIdentifiers.size() ; c ++){
					if (response.values.contains(itemIdentifiers.get(i) + " " + choiceIdentifiers.get(c)))
						buttons.get(i).get(c).setChecked(true);
				}
			}
			showingAnswers = false;
		}

	}

	@Override
	public JSONArray getState() {
		JSONArray newState = new JSONArray();
		
		for (int r = 0 ; r < response.values.size() ; r ++){
			newState.set(newState.size(), new JSONString(response.values.get(r)) );
		}

		return newState;
	}

	@Override
	public void setState(JSONArray newState) {
		for (int e = 0 ; e < newState.size() ; e ++){
			String[] components = newState.get(e).isString().stringValue().split(" ");
			if (components.length == 2  &&  itemIdentifiers.indexOf(components[0]) != -1  &&  choiceIdentifiers.indexOf(components[1]) != -1){
				buttons.get(itemIdentifiers.indexOf(components[0])).get(choiceIdentifiers.indexOf(components[1])).setChecked(true);
			}
			
		}
		updateResponse("", false);

	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {
		Vector<InternalEventTrigger> triggers = new Vector<InternalEventTrigger>();
		for (int i = 0 ; i < buttons.size() ; i ++){
			for (int c = 0 ; c < buttons.get(i).size() ; c ++){
				triggers.add(new InternalEventTrigger(buttonIds.get(i).get(c), Event.ONMOUSEUP));
			}
		}
		
		return triggers;
	}

	@Override
	public void handleEvent(String tagID, InternalEvent event) {
		updateResponse(tagID, true);

	}
	
	private void updateResponse(String senderId, boolean userInteract){
		if (showingAnswers)
			return;

		Vector<String> currResponseValues = new Vector<String>();
		
		for (int i = 0 ; i < buttons.size() ; i ++){
			
			boolean isSenderFromCurrentItem = false;
			
			if (senderId.length() > 0){
				for (int c = 0 ; c < buttons.get(i).size() ; c ++){
					if (buttonIds.get(i).get(c).compareTo(senderId) == 0){
						isSenderFromCurrentItem = true;
						break;
					}
				}
			}
			for (int c = 0 ; c < buttons.get(i).size() ; c ++){
				if (isSenderFromCurrentItem  &&  buttonIds.get(i).get(c).compareTo(senderId) == 0  &&  !buttons.get(i).get(c).isChecked()  ||
					!isSenderFromCurrentItem  &&  buttonIds.get(i).get(c).compareTo(senderId) != 0  &&   buttons.get(i).get(c).isChecked()){
					currResponseValues.add(itemIdentifiers.get(i) + " " + choiceIdentifiers.get(c));
				}					
			}
		}

		if (!response.compare(currResponseValues)){
			response.set(currResponseValues);
			stateListener.onStateChanged(userInteract, this);
		}
	}

}
