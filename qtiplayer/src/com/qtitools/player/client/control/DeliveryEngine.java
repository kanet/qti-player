package com.qtitools.player.client.control;

import java.io.Serializable;
import java.util.Date;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.qtitools.player.client.IDocumentLoaded;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.model.AssessmentItem;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.util.xml.XMLDocument;

/**
 * Responsible for:
 * - loading the content, 
 * - managing the content,
 * - delivering content to player,
 * - managing state, results and reports about the assessments.
 * 
 * @author Rafal Rybacki
 *
 */
public class DeliveryEngine implements IActivity {

	public Assessment assessment;
	public AssessmentItem currentAssessmentItem;
	private int currentAssessmentItemIndex;
	
	private long assessmentSessionTimeStarted;
	private long assessmentSessionTimeFinished;
	
	private DeliveryEngineEventListener listener;
	
	public Serializable[] states;
	public Result[] results;
	
	//private AssessmentEventsHandler deliveryEngineHandler;
	
	/**
	 * C'tor.
	 */
	public DeliveryEngine(DeliveryEngineEventListener lnr){
		listener = lnr;
		assessmentSessionTimeStarted = 0;
		assessmentSessionTimeFinished = 0;
		
	}
	
	//------------------------- QTI XML CONTENT LOADING --------------------------------

	/**
	 * Load assessment file from server
	 */
	public void loadAssessment(String url){
	
		String resolvedURL;

		if( GWT.getHostPageBaseURL().startsWith("file://") ){

			String localURL = URL.decode( GWT.getHostPageBaseURL() );
			resolvedURL = localURL + url;
		}
		else if( url.contains("://") || url.startsWith("/") ){
			resolvedURL = url;
		}
		else{
			resolvedURL = GWT.getHostPageBaseURL() + url;
		}

		new com.qtitools.player.client.util.xml.XMLDocument(resolvedURL, new IDocumentLoaded(){

			public void finishedLoading(Document document, String baseURL) {
				assessment = new Assessment(new XMLData(document, baseURL));
				beginAssessmentSession();				
			}

			@Override
			public void loadingErrorHandler(String error) {
				listener.onAssessmentLoadingError(error);
			}
		});

	}

	/**
	 * Load assessment item
	 * @param item index
	 */
	public void loadAssessmentItem(int index){

		if(index >= 0 && index < assessment.getAssessmentItemsCount()){
			String  url = assessment.getAssessmentItemRef(index);

			currentAssessmentItemIndex = index;

			new XMLDocument(url, new IDocumentLoaded(){

				public void finishedLoading(Document document, String baseURL) {
					currentAssessmentItem = new AssessmentItem(new XMLData(document, baseURL));
					beginItemSession();
				}

				@Override
				public void loadingErrorHandler(String error) {
					listener.onAssessmentItemLoadingError(error);
				}
			});
		}
	}


	//------------------------- INTERFACE --------------------------------
		
	/**
	 * @return the content ready do display on the web page
	 */
	public Widget getContent(){
		return currentAssessmentItem.getContentWidget();
	}

	
	//------------------------- CONTROLLER --------------------------------

	/**
	 * Moves the assessment to the next item.
	 */
	public void nextAssessmentItem(){
		
		if(currentAssessmentItemIndex  < assessment.getAssessmentItemsCount()-1){
		
			if (currentAssessmentItem != null)
				endItemSession();
			
			loadAssessmentItem(currentAssessmentItemIndex+1);
		}
	}

	/**
	 * Moves the assessment to the previous item.
	 */
	public void previousAssessmentItem(){
		
		if(currentAssessmentItemIndex  > 0){
		
			if (currentAssessmentItem != null)
				endItemSession();
			
			loadAssessmentItem(currentAssessmentItemIndex-1);
		}
	}
	
	/**
	 * 
	 * @return Current assessment item index (starting from 0).
	 */
	public int getCurrentAssessmentItemIndex(){
		return currentAssessmentItemIndex;
	}
	
	/**
	 * Gets the number of assessment items in current assessment.
	 * @return The number of assessment items in current assessment.
	 */
	public int getAssessmentItemCount(){
		return assessment.getAssessmentItemsCount();
	}

	/**
	 * Checks whether current assessment item is the last one
	 * @return True if the last, otherwise false
	 */
	public boolean isLastAssessmentItem(){
		return (currentAssessmentItemIndex == assessment.getAssessmentItemsCount()-1);
	}

	/**
	 * Checks whether current assessment item is the first one
	 * @return True if the first, otherwise false
	 */
	public boolean isFirstAssessmentItem(){
		return (currentAssessmentItemIndex == 0);
	}
	
	/**
	 * Begins assessment session.
	 */
	public void beginAssessmentSession(){
		assessmentSessionTimeStarted = (long) ((new Date()).getTime() * 0.001);
		initHistory();
		listener.onAssessmentSessionBegin();
	}

	/**
	 * Ends assessment session.
	 */
	public void endAssessmentSession(){
		assessmentSessionTimeFinished = (long) ((new Date()).getTime() * 0.001);
	}

	/**
	 * Begins assessment item session. 
	 */
	public void beginItemSession(){

	    // Load state
		updateState();
	    
	    listener.onItemSessionBegin(currentAssessmentItemIndex);
	}

	/**
	 * Ends assessment item session. 
	 */
	public void endItemSession(){
		currentAssessmentItem.process();
		updateHistory();
		listener.onItemSessionFinished(currentAssessmentItemIndex);
		
	}

	/**
	 * Returns assessment result.
	 * TODO: move result delivery to AssessmentSessionReport class?
	 * @return the result
	 */
	public Result getAssessmentResult(){
	    float score = 0;
	    float min = 0;
	    float max = 0;
		
	    for(Result result : results){
	    	if(result != null){
		      score += result.getScore();
		      max += result.getMaxPoints();
		      min += result.getMinPoints();
	    	}
	    }
	    
	    return new Result(score, min, max);
	}

	/**
	 * Returns assessment item result.
	 * TODO: move result delivery to AssessmentSessionReport class?
	 * @return the result
	 */
	public Result getAssessmentItemResult(){
		return getAssessmentItemResult(currentAssessmentItemIndex);
	}
	public Result getAssessmentItemResult(int item){
	    float score = 0;
	    float min = 0;
	    float max = 0;
		
    	if(results[item] != null){
	      score = results[item].getScore();
	      max = results[item].getMaxPoints();
	      min = results[item].getMinPoints();
    	}
	    
	    return new Result(score, min, max);
	}
	
	/** 
	 * Get report concerning current assessment session.
	 * 
	 * @return the report
	 */
	public IAssessmentSessionReport report(){
		return new IAssessmentSessionReport() {
			
			@Override
			public String getAssessmentTitle() {
				return assessment.getTitle();
			}
			
			@Override
			public int getAssessmentSessionTime() {
				if (assessmentSessionTimeStarted == 0)
					return 0;
				
				if (assessmentSessionTimeFinished != 0)
					return new Long(assessmentSessionTimeFinished - assessmentSessionTimeStarted).intValue();
				
				return new Long(((long) ((new Date()).getTime() * 0.001)) - assessmentSessionTimeStarted).intValue();
			}
			
			@Override
			public Result getAssessmentResult() {
				return getAssessmentResult();
			}
			
			@Override
			public Result getAssessmentItemResult() {
				return getAssessmentItemResult();
			}
			
			@Override
			public int getAssessmentItemIndex() {
				return currentAssessmentItemIndex;
			}
			
			@Override
			public int getAssessmentItemsCount() {
				return assessment.getAssessmentItemsCount();
			}
			
			@Override
			public String getAssessmentItemTitle() {
				return assessment.getAssessmentItemTitle(currentAssessmentItemIndex);
			}
		};
	}


	public Serializable[] getState(){
	    return states;
	}
	

	public void setState(Serializable[] obj){
		states = obj;
		updateState();
	}
	
	private void updateState(){
	    if(states[currentAssessmentItemIndex] != null)
	    	currentAssessmentItem.setState(states[currentAssessmentItemIndex]);
	}
	
	//------------------------- HISTORY --------------------------------

	/**
	 * Initiates the history of the assessment session.
	 * 
	 * The history of assessment session consists of 
	 * results and interaction modules' states for each
	 * assessment item.
	 */
	private void initHistory(){
	    results = new Result[assessment.getAssessmentItemsCount()];
	    states = new Serializable[assessment.getAssessmentItemsCount()];
	}
	
	/**
	 * Called when item session is finished
	 */
	private void updateHistory() {
		states[currentAssessmentItemIndex] = currentAssessmentItem.getState();
		results[currentAssessmentItemIndex] = currentAssessmentItem.getResult();
	}
	
	//------------------------- IACTIVITY --------------------------------

	@Override
	public void markAnswers() {
		
	}

	/**
	 *  Resets results and interaction modules for 
	 *  current assessment session. 
	 */
	@Override
	public void reset() {
		results[currentAssessmentItemIndex] = null;
		currentAssessmentItem.reset();
		
	}

	@Override
	public void showCorrectAnswers() {
		
	}

}
