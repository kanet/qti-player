package com.qtitools.player.client.module.dragdrop;

import java.util.Vector;

import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.components.ElementWrapperWidget;
import com.qtitools.player.client.components.Rectangle;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.model.feedback.InlineFeedback;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IUnattachedComponent;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.util.xml.XMLConverter;
import com.qtitools.player.client.util.xml.XMLUtils;

public class DragDropModule extends Composite implements IInteractionModule {

	/** response id */
	private String responseIdentifier;
	/** response processing interface */
	private Response response;
	/** module state changed listener */
	private ModuleStateChangedEventsListener stateListener;
	
	private Vector<DragElement> elements;
	private Vector<DragSlot> slots;

	private Vector<IUnattachedComponent> inlineModules;
	
	private AbsolutePanel containerPanel;
	private FlowPanel backLayerPanel;
	private FlowPanel contentsPanel;
	private FlowPanel sourcelistPanel;
	private DragPanel dragPanel;
	
	private boolean locked = false;
	private boolean showingAnswers = false;
	
	public DragDropModule(Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener){
		
		elements = new Vector<DragElement>();
		slots = new Vector<DragSlot>();

		inlineModules = new Vector<IUnattachedComponent>();
		stateListener = (ModuleStateChangedEventsListener)moduleEventsListener;

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		
		NodeList elementNodes = element.getElementsByTagName("dragElement");
		for (int i = 0 ; i < elementNodes.getLength() ; i ++){
			DragElement de = new DragElement(((Element)elementNodes.item(i)) , inlineModules);
			elements.add(de);
		}
		
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM((Element)element.getElementsByTagName("contents").item(0), moduleSocket, moduleEventsListener, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return name.equals("slot");
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element1,
					ModuleSocket moduleSocket,
					IModuleEventsListener moduleEventsListener) {
				DragSlot ds = new DragSlot(element1);
				slots.add(ds);
				
				NodeList childNodes = element1.getChildNodes();
				for (int f = 0 ; f < childNodes.getLength() ; f ++){
					if (childNodes.item(f).getNodeName().compareTo("feedbackInline") == 0)
					moduleSocket.add(new InlineFeedback(ds, childNodes.item(f)));
				}
				
				return ds.getElement();
			}
			
		}, new Vector<String>());
		
		containerPanel = new AbsolutePanel();
		containerPanel.setStyleName("qp-dragdrop-container");
		
		backLayerPanel = new FlowPanel();
		backLayerPanel.setStyleName("bqp-dragdrop-backlayer");
		containerPanel.add(backLayerPanel, 0, 0);
		
		contentsPanel = new FlowPanel();
		contentsPanel.setStyleName("qp-dragdrop-contents");
		backLayerPanel.add(contentsPanel);
		contentsPanel.add(new ElementWrapperWidget(dom));
		
		sourcelistPanel = new FlowPanel();
		sourcelistPanel.setStyleName("qp-dragdrop-sourcelist");
		backLayerPanel.add(sourcelistPanel);
		
		dragPanel = new DragPanel();
		dragPanel.setStyleName("qp-dragdrop-dragpanel");
		containerPanel.add(dragPanel, 0, 0);
		dragPanel.getElement().setId(Document.get().createUniqueId());
		
		dragPanel.registerElements(elements);
		
		initWidget(containerPanel);
	}
	
	@Override
	public void onOwnerAttached() {
		containerPanel.setWidth( new Integer(contentsPanel.getOffsetWidth()).toString() );
		containerPanel.setHeight( new Integer(contentsPanel.getOffsetHeight() + sourcelistPanel.getOffsetHeight()).toString() );

		dragPanel.registerOrigins(new Rectangle(0, contentsPanel.getOffsetHeight(), sourcelistPanel.getOffsetWidth(), sourcelistPanel.getOffsetHeight()));
		dragPanel.registerSlots(slots);
		dragPanel.placeElements();

		for (IUnattachedComponent uac : inlineModules)
			uac.onOwnerAttached();
	}
	@Override
	public String getIdentifier() {
		return responseIdentifier;
	}

	@Override
	public void lock(boolean l) {
		locked = l;
	}

	@Override
	public void markAnswers(boolean mark) {
		if (mark){
			
			lock(true);
			
			Vector<String> currentAnswers = new Vector<String>();
			for( int i = 0 ; i < dragPanel.getElementsLocations().size() ; i ++){
				if (dragPanel.getElementsLocations().indexOf(i) == -1) {
					currentAnswers.add("");
				} else {
					currentAnswers.add( elements.get(dragPanel.getElementsLocations().indexOf(i)).getIdentifier() );
				}
					
			}

			Vector<Boolean> used = new Vector<Boolean>();
			for (int g = 0 ; g < response.correctAnswers.size() ; g ++ ){
				used.add(false);
			}
			
			for (int correct = 0 ; correct < response.correctAnswers.size() ; correct ++){
				
				boolean passed = true;
				
				int groupIndex = -1;
				for (int g = 0 ; g < response.groups.size() ; g ++ ){
					if (response.groups.get(g).contains(correct)){
						groupIndex = g;
						break;
					}
				}
				
				String currUserAnswer = currentAnswers.get(correct);

				if (groupIndex == -1){
					if (response.correctAnswers.get(correct).compareTo(currUserAnswer) != 0){
						passed = false;
					}
				} else {
					boolean answerFound = false;
					for (int a = 0 ; a < response.groups.get(groupIndex).size() ; a ++){
						int answerIndex = response.groups.get(groupIndex).get(a);
						if (response.correctAnswers.get(answerIndex).compareTo(currUserAnswer) == 0  &&  used.get(answerIndex) == false){
							answerFound = true;
							used.set(answerIndex, true);
							break;
						}
					}
					if (!answerFound){
						passed = false;
					}
				}
				
				if (passed)
					slots.get(correct).setStyleName("qp-dragdrop-slot-correct");
				else
					slots.get(correct).setStyleName("qp-dragdrop-slot-wrong");
			}
		} else {
			lock(false);
			
			for (DragSlot ds : slots){
				ds.setStyleName("qp-dragdrop-slot");
			}
		}

	}

	@Override
	public void reset() {
		for (int i = 0 ; i < dragPanel.getElementsLocations().size() ; i ++){
			dragPanel.getElementsLocations().set(i, -1);
		}
		dragPanel.placeElements();
	}

	@Override
	public void showCorrectAnswers(boolean show) {
		if (show  &&  !showingAnswers){
			
			showingAnswers = true;
			
			Vector<String> identifiers = new Vector<String>();
			for (DragElement de : elements){
				identifiers.add(de.getIdentifier());
			}
			for (int s = 0 ; s < dragPanel.getElementsLocations().size() ; s ++){
				dragPanel.getElementsLocations().set(s, -1);
			}
			for (int c = 0 ; c < response.correctAnswers.size() ; c ++){
				String tmpAnswerIdentifier = response.correctAnswers.get(c);
				dragPanel.getElementsLocations().set(identifiers.indexOf(tmpAnswerIdentifier), c);
			}
			dragPanel.placeElements();
			
		} else if (!show  &&  showingAnswers){

			
			Vector<String> identifiers = new Vector<String>();
			for (DragElement de : elements){
				identifiers.add(de.getIdentifier());
			}
			for (int s = 0 ; s < dragPanel.getElementsLocations().size() ; s ++){
				dragPanel.getElementsLocations().set(s, -1);
			}
			for (int c = 0 ; c < response.values.size() ; c ++){
				String tmpAnswerIdentifier = response.values.get(c);
				if (!tmpAnswerIdentifier.equals(""))
					dragPanel.getElementsLocations().set(identifiers.indexOf(tmpAnswerIdentifier), c);
			}
			
			dragPanel.placeElements();
			
			showingAnswers = false;
		}
	}

	@Override
	public JSONArray getState() {
		JSONArray array = new JSONArray();
		for (Integer i : dragPanel.getElementsLocations()){
			array.set(array.size(), new JSONNumber(i));
		}
		return array;
	}

	@Override
	public void setState(JSONArray newState) {
		for (int i = 0 ; i < newState.size() ; i ++){
			dragPanel.getElementsLocations().set(i, (int)newState.get(i).isNumber().doubleValue());
		}
		dragPanel.placeElements();
		updateResponse(false);
	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {
		Vector<InternalEventTrigger> triggers = new Vector<InternalEventTrigger>();

		triggers.add(new InternalEventTrigger(dragPanel.getElement().getId(), Event.ONMOUSEUP));
		triggers.add(new InternalEventTrigger(dragPanel.getElement().getId(), Event.ONMOUSEMOVE));
		
		for (DragElement de : elements){
			triggers.add(new InternalEventTrigger(de.getCoverId(), Event.ONMOUSEDOWN));
			triggers.add(new InternalEventTrigger(de.getCoverId(), Event.ONMOUSEUP));
			triggers.add(new InternalEventTrigger(de.getCoverId(), Event.ONMOUSEMOVE));
		}
		return triggers;
	}

	@Override
	public void handleEvent(String tagID, InternalEvent event) {		
		if (locked)
			return;
		
		DragElement currentDragElement = null;
		for (DragElement de : elements){
			if (de.getCoverId().equals(tagID)){
				currentDragElement = de;
				break;
			}
		}
		if (event.getTypeInt() == Event.ONMOUSEDOWN){
			dragPanel.startDrag(currentDragElement, event.getClientX(), event.getClientY());
		} else if (event.getTypeInt() == Event.ONMOUSEMOVE){
			dragPanel.processDrag(event.getClientX(), event.getClientY());
		} else if (event.getTypeInt() == Event.ONMOUSEUP){
			dragPanel.stopDrag();
			updateResponse(true);
		}
			
	}

	private void updateResponse(boolean userInteract){
		if (showingAnswers)
			return;

		Vector<String> currResponseValues = new Vector<String>();
		
		for (int i = 0 ; i < slots.size() ; i++){
			int tmpElementIndex = dragPanel.getElementsLocations().indexOf(i);
			if (tmpElementIndex != -1){
				currResponseValues.add(elements.get(tmpElementIndex).getIdentifier());
			} else {
				currResponseValues.add("");
			}
		}

		if (!response.compare(currResponseValues)){
			response.set(currResponseValues);
			stateListener.onStateChanged(userInteract, this);
		}
	}

}
