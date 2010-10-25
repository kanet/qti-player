package com.qtitools.player.client.module.math.interaction;

import java.util.Vector;

import pl.smath.expression.model.Term;
import pl.smath.expression.parser.ExpressionParser;
import pl.smath.expression.parser.ExpressionParserException;
import pl.smath.renderer.renderer.TermRendererException;
import pl.smath.renderer.renderer.TermWidgetFactory;
import pl.smath.renderer.utils.InteractionManager;

import com.google.gwt.dom.client.Document;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.util.xml.XMLUtils;

public class MathModule extends Widget implements IInteractionModule {

	public MathModule(Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener){

		responseIdentifier = XMLUtils.getAttributeAsString(element, "responseIdentifier");
		response = moduleSocket.getResponse(responseIdentifier);
		this.moduleEventsListener = moduleEventsListener;

		term = null;
		container = new FlowPanel();
		container.setStyleName("math-inline");
		if (element.hasAttribute("expression")){
			try {
				String expression = element.getAttribute("expression");
				term = (new ExpressionParser()).parseText(expression);
				int fromIndex = 0;
				while ((fromIndex = expression.indexOf("\\box", fromIndex+1)) != -1){
					gapsCount++;
				}
				
				gapsIds = new Vector<String>();
				for (int g = 0 ; g < gapsCount ; g ++)
					gapsIds.add(Document.get().createUniqueId());
			} catch (ExpressionParserException e1) {
			}
		}
		
		setElement(container.getElement());
	}
	
	private IModuleEventsListener moduleEventsListener;
	/** response identifier */
	private String responseIdentifier;
	/** response processing interface */
	private Response response;

	private Panel container;
	private Term term;
	private InteractionManager interactionManager;
	private boolean showingAnswers = false;
	

	private int gapsCount;
	private Vector<String> gapsIds;
	
	@Override
	public void onOwnerAttached() {

		interactionManager = new InteractionManager(container);
		try {
			(new TermWidgetFactory()).createWidget(term, interactionManager);
		} catch (TermRendererException e) {
		}
		
		interactionManager.process();

		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			interactionManager.getGapAt(i).getElement().setId(gapsIds.get(i));
		}
	}
	
	@Override
	public String getIdentifier() {
		return responseIdentifier;
	}

	@Override
	public void lock(boolean l) {
		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			interactionManager.getGapAt(i).setEnabled(!l);
		}
	}

	@Override
	public void markAnswers(boolean mark) {

		if (mark){
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
				
				String currUserAnswer = interactionManager.getGapAt(correct).getText();
				
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
					interactionManager.getGapAt(correct).setStyleName("smath-gap-correct");
				else
					interactionManager.getGapAt(correct).setStyleName("smath-gap-wrong");
			}
		} else {
			for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){			
				interactionManager.getGapAt(i).setStyleName("smath-gap");
			}
		}
	}

	@Override
	public void reset() {
		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			interactionManager.getGapAt(i).setText("");
		}
	}

	@Override
	public void showCorrectAnswers(boolean show) {
		if (show  &&  !showingAnswers){
			showingAnswers = true;
		} else if (!show  &&  showingAnswers){
			showingAnswers = false;
		}

	}

	@Override
	public JSONArray getState() {
		JSONArray newState = new JSONArray();
		
		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			newState.set(i, new JSONString( interactionManager.getGapAt(i).getText() ) );
		}
		return newState;
	}

	@Override
	public void setState(JSONArray newState) {
		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			interactionManager.getGapAt(i).setText(newState.get(i).isString().stringValue());
		}
		updateResponse(false);
	}

	@Override
	public Vector<InternalEventTrigger> getTriggers() {

		Vector<InternalEventTrigger> ids = new Vector<InternalEventTrigger>();

		for (int i = 0 ; i < gapsCount ; i ++){
			ids.add(new InternalEventTrigger(gapsIds.get(i), Event.ONCHANGE));
		}
		return ids;
	}

	@Override
	public void handleEvent(String tagID, InternalEvent event) {
		updateResponse(true);

	}

	
	private void updateResponse(boolean userInteract){
		if (showingAnswers)
			return;
		

		Vector<String> currResponseValues = new Vector<String>();
		
		for (int i = 0 ; i < interactionManager.getGapsCount() ; i ++){
			currResponseValues.add(interactionManager.getGapAt(i).getText());
		}

		if (!response.compare(currResponseValues)){
			response.set(currResponseValues);
			moduleEventsListener.onStateChanged(userInteract, this);
		}
	
	}


}
