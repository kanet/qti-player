package com.qtitools.player.client.model;

import java.util.Iterator;
import java.util.Vector;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.control.Result;
import com.qtitools.player.client.control.style.StyleLinkDeclaration;
import com.qtitools.player.client.model.feedback.ModalFeedbackManager;
import com.qtitools.player.client.model.responseprocessing.ResponseProcessor;
import com.qtitools.player.client.model.variables.BaseType;
import com.qtitools.player.client.model.variables.BaseTypeConverter;
import com.qtitools.player.client.model.variables.IVariableCreator;
import com.qtitools.player.client.model.variables.VariableManager;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.mathexpr.MathJaxProcessor;
import com.qtitools.player.client.util.xml.document.XMLData;

public class AssessmentItem implements IStateful, IActivity {
		
	public ItemBody itemBody;
	
	private ResponseProcessor responseProcessor;
	
	private ModalFeedbackManager feedbackManager;
	
	public VariableManager<Response> responseManager;
	
	public VariableManager<Outcome> outcomeManager;
	
	public StyleLinkDeclaration styleDeclaration;
	
	private String title;

	private XMLData xmlData;
			
	public AssessmentItem(XMLData data, IStateChangedListener stateChangedListener){

		xmlData = data;
		
		Node rootNode = xmlData.getDocument().getElementsByTagName("assessmentItem").item(0);
		Node itemBodyNode = xmlData.getDocument().getElementsByTagName("itemBody").item(0);
	
	    responseProcessor = new ResponseProcessor(xmlData.getDocument().getElementsByTagName("responseProcessing"));
	    
	    feedbackManager = new ModalFeedbackManager(xmlData.getDocument().getElementsByTagName("modalFeedback"), xmlData.getBaseURL());
	    
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
   
	    itemBody = new ItemBody((Element)itemBodyNode, moduleSocket, stateChangedListener);
	    
	    title = ((Element)rootNode).getAttribute("title");
	}
	
	private void checkVariables(){
		if (outcomeManager.variables.size() == 0){
			if (responseManager.getVariablesMap().keySet().size() > 0){
				Iterator<String> iterator = responseManager.getVariablesMap().keySet().iterator();
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "SCORE";
				tmpOutcome.cardinality = responseManager.getVariable(iterator.next()).cardinality;
				tmpOutcome.baseType = BaseType.STRING;

				outcomeManager.variables.put("SCORE", tmpOutcome);
			}
		}
	}
	
	/**
	 * Inner class for module socket implementation
	 */
	private IModuleSocket moduleSocket = new IModuleSocket(){

		public com.qtitools.player.client.model.variables.response.Response getResponse(String id) {
			return responseManager.getVariable(id);
		}

	};
	
	public void process(boolean userTriggered){
		process(userTriggered, "");
	}
	
	public void process(boolean userTriggered, String senderIdentifier){
		responseProcessor.process(responseManager.getVariablesMap(), outcomeManager.getVariablesMap());
		if (userTriggered){
			feedbackManager.process(responseManager.getVariablesMap(), outcomeManager.getVariablesMap(), senderIdentifier);
			//MathJaxProcessor.process();
		}
	}

	public String getTitle(){
		return title;
	}

	public int getModuleCount(){
		return itemBody.getModuleCount();
	}
	
	public Widget getContentView(){
		return itemBody;
	}
	
	public Widget getFeedbackView(){
		return feedbackManager.getView();
	}
	
	public Vector<Widget> getModules(){
		return itemBody.widgets;
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
	
	@Override
	public void markAnswers() {
		itemBody.markAnswers();

	}

	@Override
	public void unmark() {
		itemBody.unmark();

	}

	@Override
	public void reset() {
		responseManager.reset();
		outcomeManager.reset();
		itemBody.reset();
	}


	@Override
	public void lock(boolean l) {
		itemBody.lock(l);
	}
	
	public boolean isLocked(){
		return itemBody.isLocked();
	}

	@Override
	public void showCorrectAnswers() {
		itemBody.showCorrectAnswers();

	}

	@Override
	public JSONArray getState() {
		return itemBody.getState();
	}

	@Override
	public void setState(JSONArray newState) {
		itemBody.setState(newState);

	}
	

	//------------------------- STYLE --------------------------------
	

}
