package com.qtitools.player.client.model;

import java.util.Vector;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.control.Result;
import com.qtitools.player.client.control.XMLData;
import com.qtitools.player.client.model.responseprocessing.ResponseProcessor;
import com.qtitools.player.client.model.variables.BaseType;
import com.qtitools.player.client.model.variables.BaseTypeConverter;
import com.qtitools.player.client.model.variables.IVariableCreator;
import com.qtitools.player.client.model.variables.VariableManager;
import com.qtitools.player.client.model.variables.outcome.Outcome;
import com.qtitools.player.client.model.variables.response.Response;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.module.IStateful;

public class AssessmentItem implements IStateful, IActivity {
		
	public ItemBody itemBody;
	
	private ResponseProcessor responseProcessor;
	
	public VariableManager<Response> responseManager;
	
	public VariableManager<Outcome> outcomeManager;
	
	private String title;

	private XMLData xmlData;
	/** Link to the Assessment Item style CSS */
	private String styleLink;
			
	public AssessmentItem(XMLData data, IStateChangedListener stateChangedListener){

		xmlData = data;
		
		Node rootNode = xmlData.getDocument().getElementsByTagName("assessmentItem").item(0);
		Node itemBodyNode = xmlData.getDocument().getElementsByTagName("itemBody").item(0);

		styleLink = ((Element)rootNode).getAttribute("styleLink");
		if (styleLink == null)
			styleLink = "";
		else if (styleLink.length() > 0  &&  !styleLink.contains("http://")  &&  !styleLink.contains("file:///"))
			styleLink = data.getBaseURL() + styleLink;
		
	    responseProcessor = new ResponseProcessor(xmlData.getDocument().getElementsByTagName("responseProcessing"));
	    
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
	    
	    checkVariables();
   
	    itemBody = new ItemBody((Element)itemBodyNode, moduleSocket, stateChangedListener);
	    
	    title = ((Element)rootNode).getAttribute("title");
	}
	
	private void checkVariables(){
		if (outcomeManager.variables.size() == 0){
			if (responseManager.getVariablesMap().containsKey("RESPONSE")){
				Outcome tmpOutcome = new Outcome();
				tmpOutcome.identifier = "SCORE";
				tmpOutcome.cardinality = responseManager.getVariable("RESPONSE").cardinality;
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
	
	public void process(){
		responseProcessor.process(responseManager.getVariablesMap(), outcomeManager.getVariablesMap());
	}

	public String getTitle(){
		return title;
	}

	public int getModuleCount(){
		return itemBody.getModuleCount();
	}
	
	public Widget getContentWidget(){
		
		return itemBody;
	}	
	
	public Vector<Widget> getModules(){
		return itemBody.widgets;
	}
	
	public Result getResult(){
		
		Result result;
		
		String score = "";
		Float lowerBound = new Float(0);
		Float upperBound = new Float(1);
		
		if (outcomeManager.getVariable("SCORE") != null)
			if (outcomeManager.getVariable("SCORE").values.size() > 0)
			score = outcomeManager.getVariable("SCORE").values.get(0);
			
		if (responseManager.getVariable("RESPONSE").mapping.lowerBound != null)
			lowerBound = responseManager.getVariable("RESPONSE").mapping.lowerBound;
		
		if (responseManager.getVariable("RESPONSE").mapping.upperBound != null)
			upperBound = responseManager.getVariable("RESPONSE").mapping.upperBound;
			
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
	
	public String getStyleLink(){
		if (styleLink == null)
			return "";
		
		return styleLink;
	}
}
