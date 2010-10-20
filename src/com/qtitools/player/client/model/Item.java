package com.qtitools.player.client.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.controller.communication.DisplayContentOptions;
import com.qtitools.player.client.controller.communication.PageReference;
import com.qtitools.player.client.controller.communication.Result;
import com.qtitools.player.client.controller.style.StyleLinkDeclaration;
import com.qtitools.player.client.model.feedback.FeedbackManager;
import com.qtitools.player.client.model.feedback.InlineFeedback;
import com.qtitools.player.client.model.responseprocessing.ResponseProcessor;
import com.qtitools.player.client.model.variables.BaseType;
import com.qtitools.player.client.model.variables.BaseTypeConverter;
import com.qtitools.player.client.model.variables.Cardinality;
import com.qtitools.player.client.model.variables.IVariableCreator;
import com.qtitools.player.client.model.variables.VariableManager;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.style.StyleSocket;
import com.qtitools.player.client.util.localisation.LocalePublisher;
import com.qtitools.player.client.util.localisation.LocaleVariable;
import com.qtitools.player.client.util.xml.document.XMLData;

public class Item implements IStateful {
		
	public ItemBody itemBody;
	
	public Panel scorePanel;
	
	private ResponseProcessor responseProcessor;
	
	private FeedbackManager feedbackManager;
	
	public VariableManager<Response> responseManager;
	
	public VariableManager<Outcome> outcomeManager;
	
	public StyleLinkDeclaration styleDeclaration;
	
	public StyleSocket styleSocket;
	
	private String title;

	private XMLData xmlData;
			
	public Item(XMLData data, DisplayContentOptions options, ModuleStateChangedEventsListener stateChangedListener, StyleSocket ss){

		xmlData = data;
		
		styleSocket = ss;
		
		Node rootNode = xmlData.getDocument().getElementsByTagName("assessmentItem").item(0);
		Node itemBodyNode = xmlData.getDocument().getElementsByTagName("itemBody").item(0);
	
	    responseProcessor = new ResponseProcessor(xmlData.getDocument().getElementsByTagName("responseProcessing"));
	    
	    feedbackManager = new FeedbackManager(xmlData.getDocument().getElementsByTagName("modalFeedback"), xmlData.getBaseURL());
	    
	    responseManager = new VariableManager<Response>(xmlData.getDocument().getElementsByTagName("responseDeclaration"), new IVariableCreator<Response>() {
				@Override
				public Response createVariable(Node node) {
					return new Response(node);
				}
			});
	    
	    outcomeManager = new VariableManager<Outcome>(xmlData.getDocument().getElementsByTagName("outcomeDeclaration"), new IVariableCreator<Outcome>() {
			@Override
			public Outcome createVariable(Node node) {
				return new Outcome(node);
			}
		});
	    
	    styleDeclaration = new StyleLinkDeclaration(xmlData.getDocument().getElementsByTagName("styleDeclaration"), data.getBaseURL());
	    
	    checkVariables();
	    
	    ResponseProcessor.interpretFeedbackAutoMark(itemBodyNode, responseManager.getVariablesMap());
   
	    itemBody = new ItemBody((Element)itemBodyNode, options, moduleSocket, stateChangedListener);
	    
	    feedbackManager.setBodyView(itemBody);
	    
	    title = ((Element)rootNode).getAttribute("title");
	    
	    scorePanel = new FlowPanel();
	    scorePanel.setStyleName("qp-feedback-hidden");
	}
	
	private void checkVariables(){
		if (responseManager.getVariablesMap().keySet().size() > 0){
			if (!outcomeManager.getVariablesMap().containsKey("SCORE")){
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "SCORE";
				tmpOutcome.cardinality = Cardinality.SINGLE;
				tmpOutcome.baseType = BaseType.INTEGER;

				outcomeManager.variables.put("SCORE", tmpOutcome);
			}
			if (!outcomeManager.getVariablesMap().containsKey("SCOREHISTORY")){
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "SCOREHISTORY";
				tmpOutcome.cardinality = Cardinality.MULTIPLE;
				tmpOutcome.baseType = BaseType.INTEGER;

				outcomeManager.variables.put("SCOREHISTORY", tmpOutcome);
			}
			if (!outcomeManager.getVariablesMap().containsKey("SCORECHANGES")){
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "SCORECHANGES";
				tmpOutcome.cardinality = Cardinality.MULTIPLE;
				tmpOutcome.baseType = BaseType.INTEGER;

				outcomeManager.variables.put("SCORECHANGES", tmpOutcome);
			}
			if (!outcomeManager.getVariablesMap().containsKey("MISTAKES")){
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "MISTAKES";
				tmpOutcome.cardinality = Cardinality.SINGLE;
				tmpOutcome.baseType = BaseType.INTEGER;
				tmpOutcome.values.add("0");

				outcomeManager.variables.put("MISTAKES", tmpOutcome);
			}
			Iterator<String> responseKeys = responseManager.getVariablesMap().keySet().iterator();
			
			while (responseKeys.hasNext()){
				Response currResp = responseManager.getVariablesMap().get(responseKeys.next());
				String currRespIdentifier = currResp.identifier;
				if (!outcomeManager.getVariablesMap().containsKey(currRespIdentifier+"-LASTCHANGE")){
					Outcome tmpOutcome = new Outcome();
					tmpOutcome.identifier = currRespIdentifier+"-LASTCHANGE";
					tmpOutcome.cardinality = Cardinality.MULTIPLE;
					tmpOutcome.baseType = BaseType.INTEGER;

					outcomeManager.variables.put(currRespIdentifier+"-LASTCHANGE", tmpOutcome);
					
				}
				if (!outcomeManager.getVariablesMap().containsKey(currRespIdentifier+"-PREVIOUS")){
					Outcome tmpOutcome = new Outcome();
					tmpOutcome.identifier = currRespIdentifier+"-PREVIOUS";
					tmpOutcome.cardinality = Cardinality.MULTIPLE;
					tmpOutcome.baseType = BaseType.INTEGER;

					outcomeManager.variables.put(currRespIdentifier+"-PREVIOUS", tmpOutcome);
					
				}
				if (!outcomeManager.getVariablesMap().containsKey(currRespIdentifier+"-MISTAKES")){
					Outcome tmpOutcome = new Outcome();
					tmpOutcome.identifier = currRespIdentifier+"-MISTAKES";
					tmpOutcome.cardinality = Cardinality.SINGLE;
					tmpOutcome.baseType = BaseType.INTEGER;
					tmpOutcome.values.add("0");

					outcomeManager.variables.put(currRespIdentifier+"-MISTAKES", tmpOutcome);
					
				}
			}
		}
	}
	
	/**
	 * Inner class for module socket implementation
	 */
	private ModuleSocket moduleSocket = new ModuleSocket(){

		public com.qtitools.player.client.model.variables.response.Response getResponse(String id) {
			return responseManager.getVariable(id);
		}

		@Override
		public void add(InlineFeedback inlineFeedback) {
			feedbackManager.add(inlineFeedback);
		}
		
		@Override
		public Map<String,String> getStyles(Element element) {
			return (styleSocket!=null)? styleSocket.getStyles(element) : new HashMap<String,String>();
		}
		
		public void setCurrentPages( PageReference pr) {
			if (styleSocket!=null) {
				styleSocket.setCurrentPages(pr);
			}
		}
		
	};
	
	public void close(){
		feedbackManager.hideAllInlineFeedbacks();
	}
	
	public void process(boolean userInteract){
		process(userInteract, "");
	}
	
	public void process(boolean userInteract, String senderIdentifier){
		responseProcessor.process(responseManager.getVariablesMap(), outcomeManager.getVariablesMap(), userInteract);
		if (userInteract){
			feedbackManager.process(responseManager.getVariablesMap(), outcomeManager.getVariablesMap(), senderIdentifier);
			//MathJaxProcessor.process();
		}
	}

	public String getTitle(){
		return title;
	}

	public int getModulesCount(){
		return itemBody.getModuleCount();
	}
	
	public Widget getContentView(){
		return itemBody;
	}
	
	public Widget getFeedbackView(){
		return feedbackManager.getModalFeedbackView();
	}
	
	public Widget getScoreView(){
		return scorePanel;
	}
		
	public Result getResult(){
		
		Result result;
		
		String score = "";
		Float lowerBound = new Float(0);
		Float upperBound = new Float(0);
		
		if (outcomeManager.getVariable("SCORE") != null)
			if (outcomeManager.getVariable("SCORE").values.size() > 0)
				score = outcomeManager.getVariable("SCORE").values.get(0);
		
		Iterator<String> iterator = responseManager.getVariablesMap().keySet().iterator();
		while (iterator.hasNext()){
			String currKey = iterator.next();
			
			if (!responseManager.getVariable(currKey).isModuleAdded())
				continue;
			
			if (responseManager.getVariable(currKey).mapping.lowerBound != null)
				lowerBound += responseManager.getVariable(currKey).mapping.lowerBound;
			
			if (responseManager.getVariable(currKey).mapping.upperBound != null)
				upperBound += responseManager.getVariable(currKey).mapping.upperBound;
			else
				upperBound += 1;
		}
		
		//if (lowerBound == 0  &&  upperBound == 0)
		//	upperBound = 1.0f;
			
		result = new Result(BaseTypeConverter.tryParseFloat(score), lowerBound, upperBound);
		
		return result;
	}
	
	public int getMistakesCount(){
		if (outcomeManager.getVariablesMap().containsKey("MISTAKES")){
			if (outcomeManager.getVariable("MISTAKES").values.size() == 1){
				int mistakesCount = Integer.parseInt( outcomeManager.getVariable("MISTAKES").values.get(0) );
				outcomeManager.getVariable("MISTAKES").values.set(0, "0");
				return mistakesCount; 
			}
		}
		return 0;
	}
	
	// -------------------------- SCORE -------------------------------
	
	public void showScore(){
		if (itemBody.getModuleCount() > 0){
			Result r = getResult();
			Label feedbackLabel = new Label(LocalePublisher.getText(LocaleVariable.ITEM_SCORE1) + r.getScore() + LocalePublisher.getText(LocaleVariable.ITEM_SCORE2) + r.getMaxPoints() + LocalePublisher.getText(LocaleVariable.ITEM_SCORE3));
			feedbackLabel.setStyleName("qp-feedback-score-text");
			scorePanel.add(feedbackLabel);
			scorePanel.setStyleName("qp-feedback");
		}
	}

	public void hideScore(){
		scorePanel.clear();
		scorePanel.setStyleName("qp-feedback-hidden");
	}
	
	// -------------------------- NAVIGATION -------------------------------
	
	public int getItemModuleCount(){
		return itemBody.getModuleCount();
	}

	public void checkItem(){
		itemBody.markAnswers(true);
		itemBody.lock(true);
		showScore();
	}
	
	public void continueItem(){
		itemBody.markAnswers(false);
		itemBody.lock(false);
		hideScore();
	}
	
	public void showAnswers(boolean show){
		itemBody.showCorrectAnswers(show);
		itemBody.lock(show);
	}


	public void resetItem(){
		responseManager.reset();
		outcomeManager.reset();
		itemBody.reset();
		hideScore();
	}
	
	public boolean isLocked(){
		return itemBody.isLocked();
	}

	@Override
	public JSONArray getState() {
		return itemBody.getState();
	}

	@Override
	public void setState(JSONArray newState) {
		itemBody.setState(newState);

	}
	
}