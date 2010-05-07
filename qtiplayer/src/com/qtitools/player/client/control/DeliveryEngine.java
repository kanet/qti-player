package com.qtitools.player.client.control;

import java.util.Date;
import java.util.Vector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.qtitools.player.client.control.style.StyleLinkManager;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.model.AssessmentItem;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IStateChangedListener;
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
public class DeliveryEngine implements IActivity, IStateChangedListener {

	public Assessment assessment;
	public AssessmentItem currentAssessmentItem;
	private int currentAssessmentItemIndex;
	
	private long assessmentSessionTimeStarted;
	private long assessmentSessionTimeFinished;
	
	private DeliveryEngineEventListener listener;
	
	public JSONArray states;
	public Result[] results;
	
	public EngineModeManager mode;
	
	private int masteryScore;
	private Vector<Boolean> itemsVisited;
	
	private StyleLinkManager styleManager;
	
	//private AssessmentEventsHandler deliveryEngineHandler;
	
	/**
	 * C'tor.
	 */
	public DeliveryEngine(DeliveryEngineEventListener lnr){
		listener = lnr;
		assessmentSessionTimeStarted = 0;
		assessmentSessionTimeFinished = 0;
		mode = new EngineModeManager();
		styleManager = new StyleLinkManager();
		masteryScore = 100;
		
	}
	
	//------------------------- QTI XML CONTENT LOADING --------------------------------

	/**
	 * Load assessment file from server
	 */
	public void loadAssessment(String url){

		if (!mode.canBeginAssessmentLoading())
			return;
	
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
		
		mode.beginAssessmentLoading();

		new com.qtitools.player.client.util.xml.XMLDocument(resolvedURL, new IDocumentLoaded(){

			public void finishedLoading(Document document, String baseURL) {
				if (mode.canEndAssessmentLoading()){
					assessment = new Assessment(new XMLData(document, baseURL));
					mode.endAssessmentLoading();
					beginAssessmentSession();			
				}
			}

			@Override
			public void loadingErrorHandler(String error) {
				if (mode.canEndAssessmentLoading()){
					assessment = null;
					mode.endAssessmentLoading();
					beginAssessmentSession();	
				}
			}
		});

	}

	/**
	 * Load assessment item
	 * @param item index
	 */
	public void loadAssessmentItem(int index){
		
		if (!mode.canBeginItemLoading())
			return;

		if(index >= 0 && index < assessment.getAssessmentItemsCount()){
			String  url = assessment.getAssessmentItemRef(index);

			currentAssessmentItemIndex = index;

			mode.beginItemLoading();
			
			final IStateChangedListener listener = this;

			new XMLDocument(url, new IDocumentLoaded(){

				public void finishedLoading(Document document, String baseURL) {
					if (mode.canEndItemLoading()){
						currentAssessmentItem = new AssessmentItem(new XMLData(document, baseURL), listener);
						mode.endItemLoading();
						beginItemSession();
					}
				}

				@Override
				public void loadingErrorHandler(String error) {
					if (mode.canEndItemLoading()){
						currentAssessmentItem = null;
						mode.endItemLoading();
						beginItemSession();
					}
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
		
		if (!mode.canNavigate())
			return;
		
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

		if (!mode.canNavigate())
			return;
		
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
	
	public boolean isNavigationPossible(){
		return mode.canNavigate();
	}
	
	
	/**
	 * Begins assessment session.
	 */
	public void beginAssessmentSession(){
		
		itemsVisited = new Vector<Boolean>();
		for (int i = 0 ; i < assessment.getAssessmentItemsCount() ; i ++){
			itemsVisited.add(false);
		}
		
		assessmentSessionTimeStarted = (long) ((new Date()).getTime() * 0.001);
		listener.onAssessmentSessionBegin();
		
		if (assessment != null){
			initHistory();
			loadAssessmentItem(0);
		} else {
			listener.onAssessmentLoadingError("Could not load Assessment");
			
		}
		
		
	}

	/**
	 * Ends assessment session.
	 */
	public void endAssessmentSession(){
		if (mode.canFinish()){
			endItemSession();
			mode.finish();
			assessmentSessionTimeFinished = (long) ((new Date()).getTime() * 0.001);
			listener.onAssessmentSessionFinished();
		}
	}

	/**
	 * Begins assessment item session. 
	 */
	public void beginItemSession(){

		if (mode.canRun()){
			if (currentAssessmentItem != null){
			    // Load state
				updateState();
				itemsVisited.set(currentAssessmentItemIndex, true);
			    listener.onItemSessionBegin(currentAssessmentItemIndex);
				currentAssessmentItem.process();
			} else {
				listener.onAssessmentItemLoadingError("Could not load Assessment Item.");
			}
			mode.run();
		}
	}

	/**
	 * Ends assessment item session. 
	 */
	public void endItemSession(){
		if (mode.canNavigate()){
			if (currentAssessmentItem != null){
				onStateChanged();
				listener.onItemSessionFinished(currentAssessmentItemIndex);
			} else {
				listener.onItemSessionFinished(currentAssessmentItemIndex);
			}
		}
		
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
		
	    for ( int r = 0 ; r < results.length ; r ++){
	    	
	    	Result result = results[r];
	    
	    	if(result != null){
		      score += result.getScore();
		      max += result.getMaxPoints();
		      min += result.getMinPoints();
	    	} else {
	    		max++;
	    	}
	    }
	    
	    return new Result(score, min, max);
	}
	
	public Result getAssessmentItemResultAt(int index){
		if (index < results.length)
			return results[index];
		
		return null;
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
		
		final DeliveryEngine owner = this;
		
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
				return owner.getAssessmentResult();
			}
			
			@Override
			public Result getItemResult() {
				return getAssessmentItemResult(currentAssessmentItemIndex);
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

			@Override
			public boolean getAssessmentMasteryPassed() {
				Result r = getAssessmentResult();
				
				if (r.getMaxPoints() - r.getMinPoints() == 0)
					return false;
				
				return (100*r.getScore()/(r.getMaxPoints() - r.getMinPoints()) >= masteryScore);
			}

			@Override
			public int getAssessmentItemsVisitedCount() {
				int count = 0;
				for (Boolean i : itemsVisited){
					if (i) count++;
				}
				return count;
			}
		};
	}

	public void setMasteryScore(int mastery){
		masteryScore = mastery;
	}

	public JSONArray getState(){
		if (mode.isAssessmentLoaded())
			return states;
		else
			return null;
	}
	

	public void setState(JSONArray obj){
		states = obj;
		if (mode.isAssessmentLoaded()){
			updateState();
		}
	}
	
	private void updateState(){
		
		if (states.size() <= currentAssessmentItemIndex)
			return;
		
	    if(states.get(currentAssessmentItemIndex).isArray() != null){
	    	JSONArray statesArr = states.get(currentAssessmentItemIndex).isArray();
	    	currentAssessmentItem.setState(statesArr);
	    }
	}
	
	public String getEngineMode(){
		return mode.toString();
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
	    if (states == null)
	    	states = new JSONArray(); 
	}
	
	/**
	 * Called when item session is finished
	 */
	private void updateHistory() {
		states.set(currentAssessmentItemIndex, currentAssessmentItem.getState());
		results[currentAssessmentItemIndex] = currentAssessmentItem.getResult();
	}
	
	//------------------------- IACTIVITY --------------------------------

	@Override
	public void markAnswers() {
		if (currentAssessmentItem != null){
			currentAssessmentItem.markAnswers();
		}
	}
	
	@Override
	public void unmark() {
		if (currentAssessmentItem != null){
			currentAssessmentItem.unmark();
		}
	}

	/**
	 *  Resets results and interaction modules for 
	 *  current assessment session. 
	 */
	@Override
	public void reset() {

		if (mode.canNavigate()){
		
			results[currentAssessmentItemIndex] = null;
			currentAssessmentItem.reset();
		}
		
	}

	@Override
	public void showCorrectAnswers() {
		
	}

	@Override
	public void onStateChanged() {
		currentAssessmentItem.process();
		updateHistory();
		
	}

	//------------------------- STYLE --------------------------------

	public void updateAssessmentStyle(){
		String userAgent = styleManager.getUserAgent();
		styleManager.registerAssessmentStyles(assessment.styleDeclaration.getStyleLinksForUserAgent(userAgent));
	}

	public void updateItemStyle(){
		String userAgent = styleManager.getUserAgent();
		styleManager.registerItemStyles(currentAssessmentItem.styleDeclaration.getStyleLinksForUserAgent(userAgent));
	}
}
