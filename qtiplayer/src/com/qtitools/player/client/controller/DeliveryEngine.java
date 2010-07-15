package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.ActivityActionType;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.IAssessmentReport;
import com.qtitools.player.client.controller.communication.PageData;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;
import com.qtitools.player.client.controller.communication.PageReference;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.communication.Result;
import com.qtitools.player.client.controller.data.DataSourceManager;
import com.qtitools.player.client.controller.data.DataSourceManagerMode;
import com.qtitools.player.client.controller.data.events.DataLoaderEventListener;
import com.qtitools.player.client.controller.flow.FlowEventsListener;
import com.qtitools.player.client.controller.flow.FlowManager;
import com.qtitools.player.client.controller.flow.navigation.NavigationCommandsListener;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.session.SessionDataManager;
import com.qtitools.player.client.controller.session.StateInterface;
import com.qtitools.player.client.controller.session.events.StateChangedEventsListener;
import com.qtitools.player.client.controller.style.StyleLinkManager;
import com.qtitools.player.client.util.xml.document.XMLData;
import com.qtitools.player.client.view.player.PlayerViewCarrier;
import com.qtitools.player.client.view.player.PlayerViewSocket;

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
public class DeliveryEngine implements DataLoaderEventListener, FlowEventsListener, StateChangedEventsListener {

	/*
	public Assessment assessment;
	public Item currentAssessmentItem;
	private int currentAssessmentItemIndex;
	
	private long assessmentSessionTimeStarted;
	private long assessmentSessionTimeFinished;
	
	private DeliveryEngineEventListener listener;
	
	public JSONArray states;
	public Result[] results;
	public String[] titles;
	public AssessmentItemStatistics[] stats;
	*/
	public EngineModeManager mode;
	
	private int masteryScore;
	//private Vector<Boolean> itemsVisited;
	
	private StyleLinkManager styleManager;
	
	//private AssessmentEventsHandler deliveryEngineHandler;
	

	private DataSourceManager dataManager;
	private FlowManager flowManager;
	private SessionDataManager sessionDataManager;
	
	private PlayerViewSocket playerViewSocket;
	private DeliveryEngineEventListener deliveryEngineEventsListener;
	
	private AssessmentController assessmentController;
	
	/**
	 * C'tor.
	 */
	public DeliveryEngine(PlayerViewSocket pvs, DeliveryEngineEventListener deel){

		mode = new EngineModeManager();
		styleManager = new StyleLinkManager();
		masteryScore = 100;

		playerViewSocket = pvs;
		deliveryEngineEventsListener = deel;
		
		dataManager = new DataSourceManager(this);
		flowManager = new FlowManager(this);
		sessionDataManager = new SessionDataManager(this);
		
		assessmentController = new AssessmentController(playerViewSocket.getAssessmentViewSocket(), flowManager, sessionDataManager);
		
		playerViewSocket.setPlayerViewCarrier(new PlayerViewCarrier());
		
	}
	
	public void load(String url){
		dataManager.loadAssessment(url);
	}

	public void load(XMLData assessmentData, XMLData[] itemsData){
		dataManager.loadData(assessmentData, itemsData);
	}

	@Override
	public void onAssessmentLoaded() {
		sessionDataManager.init(dataManager.getItemsCount());
		
	}

	@Override
	public void onAssessmentLoadingError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataReady() {
		flowManager.init(dataManager.getItemsCount());
		assessmentController.init(dataManager.getAssessmentData());
		flowManager.startFlow();
		updateAssessmentStyle();
		deliveryEngineEventsListener.onAssessmentStarted();
		/*
		PageReference pr = flowManager.getPageReference();
		PageData pd = dataManager.generatePageData(pr);
		assessmentController.initPage(pd);
		*/
		updatePageStyle();
	}

	@Override
	public void onActivityAction(ActivityActionType action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNavigationIncident(NavigationIncidentType incidentType) {
		assessmentController.onNavigationIncident(incidentType);
	}

	@Override
	public void onNavigatePageSwitched() {
		PageReference pr = flowManager.getPageReference();
		PageData pd = dataManager.generatePageData(pr);
		if (pd.type == PageType.SUMMARY)
			((PageDataSummary)pd).setSessionData( sessionDataManager.getSessionData() );
		assessmentController.initPage(pd);
		if (pd.type == PageType.SUMMARY)
			deliveryEngineEventsListener.onSummary();
	}

	@Override
	public void onStateChanged() {
		assessmentController.updateState();
		
	}
	
	public NavigationCommandsListener getNavigationListener(){
		return flowManager;
	}
	
	public StateInterface getStateInterface(){
		return sessionDataManager;
	}
	

	public String getEngineMode(){
		if (dataManager.getMode() == DataSourceManagerMode.NONE)
			return EngineMode.NONE.toString();
		else if (dataManager.getMode() == DataSourceManagerMode.LOADING_ASSESSMENT)
			return EngineMode.ASSESSMENT_LOADING.toString();
		else if (dataManager.getMode() == DataSourceManagerMode.LOADING_ITEMS)
			return EngineMode.ITEM_LOADING.toString();
		else if (flowManager.getPageType() == PageType.TEST  &&  flowManager.isPreviewMode()){
			return EngineMode.PREVIEW.toString();
		}
		
		return EngineMode.RUNNING.toString();
	}

	public void setMasteryScore(int mastery){
		masteryScore = mastery;
	}

	public void setFlowOptions(FlowOptions o){
		flowManager.setFlowOptions(o);
	}
	public IAssessmentReport report(){
		return new IAssessmentReport() {
			
			@Override
			public int getItemsVisitedCount() {
				int ivc = sessionDataManager.getItemsVisitedCount();
				return ivc;
			}
			
			@Override
			public int getItemsCount() {
				int ic = dataManager.getItemsCount(); 
				return ic;
			}
			
			@Override
			public String getCurrentItemTitle() {
				String t = dataManager.getItemTitle(flowManager.getCurrentPageIndex());
				return t;
			}
			
			@Override
			public Result getCurrentItemResult() {
				Result r = sessionDataManager.getSessionData(flowManager.getCurrentPageIndex()).result; 
				return r;
			}
			
			@Override
			public int getCurrentItemModulesCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getCurrentItemIndex() {
				int ii = flowManager.getCurrentPageIndex();
				return ii;
			}
			
			@Override
			public String getAssessmentTitle() {
				String at = dataManager.getItemTitle(0);
				return at;
			}
			
			@Override
			public int getAssessmentSessionTime() {
				int ast = sessionDataManager.getAssessmentTotalTime();
				if (flowManager.getFlowOptions().itemsDisplayMode == PageItemsDisplayMode.ALL  &&  dataManager.getItemsCount() > 0)
					ast /= dataManager.getItemsCount();
				return ast;
			}
			
			@Override
			public Result getAssessmentResult() {
				Result r = sessionDataManager.getAssessmentTotalResult();
				return r;
			}
			
			@Override
			public boolean getAssessmentMasteryPassed() {
				Result r = sessionDataManager.getAssessmentTotalResult();
				
				if (r.getMaxPoints() - r.getMinPoints() == 0)
					return false;
				
				return (100*r.getScore()/(r.getMaxPoints() - r.getMinPoints()) >= masteryScore);
			}
		};
	}
	//------------------------- QTI XML CONTENT LOADING --------------------------------


	//------------------------- INTERFACE --------------------------------
	
	//------------------------- CONTROLLER --------------------------------

	/*
	public void nextAssessmentItem(){
		
		if(currentAssessmentItemIndex  < assessment.getAssessmentItemsCount()-1)
			gotoAssessmentItem(currentAssessmentItemIndex+1);
	}

	public void previousAssessmentItem(){
		
		if(currentAssessmentItemIndex  > 0)
			gotoAssessmentItem(currentAssessmentItemIndex-1);
	}

	public void gotoAssessmentItem(int index){

		if (!mode.canNavigate())
			return;
		
		if(index >= 0  &&  index < assessment.getAssessmentItemsCount()){
		
			if (currentAssessmentItem != null)
				endItemSession();
			
			loadAssessmentItem(index);
		}
	}
	
	public int getCurrentAssessmentItemIndex(){
		return currentAssessmentItemIndex;
	}
	
	public int getAssessmentItemCount(){
		return assessment.getAssessmentItemsCount();
	}

	public boolean isLastAssessmentItem(){
		return (currentAssessmentItemIndex == assessment.getAssessmentItemsCount()-1);
	}

	public boolean isFirstAssessmentItem(){
		return (currentAssessmentItemIndex == 0);
	}
	
	public boolean isNavigationPossible(){
		return mode.canNavigate();
	}
	
	public boolean isAssessmentItemLocked(){
		return currentAssessmentItem.isLocked();
	}
	
	/*
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

	public void endAssessmentSession(){
		if (mode.canFinish()){
			endItemSession();
			currentAssessmentItem = null;
			mode.finish();
			assessmentSessionTimeFinished = (long) ((new Date()).getTime() * 0.001);
			listener.onAssessmentSessionFinished();
		} 
	}

	public void gotoAssessmentSummary(){
		if (mode.canSummary()){
			endItemSession();
			mode.finish();
			listener.onAssessmentSessionFinished();
		}
	}
	
	public void continueAssessment(){
		if (mode.canContinueAssessment()){
			mode.continueAssessment();
			gotoAssessmentItem(assessment.getAssessmentItemsCount()-1);
		}
	}

	public void beginItemSession(){

		if (mode.canRun()){
			
			if (currentAssessmentItem != null){
			    // Load state
				updateState();
				initItemSessionStatistics();
				itemsVisited.set(currentAssessmentItemIndex, true);
			    listener.onItemSessionBegin(currentAssessmentItemIndex);
				currentAssessmentItem.process(false);
			} else {
				listener.onAssessmentItemLoadingError("Could not load Assessment Item.");
			}
			mode.run();
			
		} else if (mode.canPreview()){
			
			if (currentAssessmentItem != null){
			    // Load state
				updateState();
			    currentAssessmentItem.lock(true);
			    listener.onItemSessionBegin(currentAssessmentItemIndex);
			} else {
				listener.onAssessmentItemLoadingError("Could not load Assessment Item.");
			}
			mode.preview();
			
		}
	}

	public void endItemSession(){
		if (mode.canNavigate()){
			if (currentAssessmentItem != null){
				//onStateChanged();
				updateHistory();
				updateItemSessionStatistics();
				listener.onItemSessionFinished(currentAssessmentItemIndex);
			} else {
				listener.onItemSessionFinished(currentAssessmentItemIndex);
			}
		}
		
	}
*/
	
	/*
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

	public String getAssessmentItemTitle(int index){
		if (index < titles.length)
			return titles[index];
			
		return null;
	}
	
	public AssessmentItemStatistics getAssessmentItemStatistics(int index){
		if (index < stats.length)
			return stats[index];
			
		return null;
	}
*/
	/*
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

			@Override
			public int getAssessmentItemModulesCount() {
				return currentAssessmentItem.getModuleCount();
			}
		};
	}
*/
/*
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
	*/
	/*
	private void updateState(){
		
		if (states.size() <= currentAssessmentItemIndex  ||  states.get(currentAssessmentItemIndex) == null)
			return;
		
	    if(states.get(currentAssessmentItemIndex).isArray() != null){
	    	JSONArray statesArr = states.get(currentAssessmentItemIndex).isArray();
	    	currentAssessmentItem.setState(statesArr);
	    }
	}
	*/

	
	//------------------------- HISTORY --------------------------------

	/**
	 * Initiates the history of the assessment session.
	 * 
	 * The history of assessment session consists of 
	 * results and interaction modules' states for each
	 * assessment item.
	 */
	/*
	private void initHistory(){
	    results = new Result[assessment.getAssessmentItemsCount()];
	    titles = new String[assessment.getAssessmentItemsCount()];
	    stats = new AssessmentItemStatistics[assessment.getAssessmentItemsCount()];
	    if (states == null)
	    	states = new JSONArray(); 
	}
	*/
	/**
	 * Called when item session is finished or the state of the page changes
	 */
	/*
	private void updateHistory() {
		states.set(currentAssessmentItemIndex, currentAssessmentItem.getState());
		results[currentAssessmentItemIndex] = currentAssessmentItem.getResult();
		titles[currentAssessmentItemIndex] = currentAssessmentItem.getTitle();
	}
	
	public void initItemSessionStatistics(){
		if (stats[currentAssessmentItemIndex] == null)
			stats[currentAssessmentItemIndex] = new AssessmentItemStatistics();
		
		stats[currentAssessmentItemIndex].onBeginItemSession();
	}
	*/
	/**
	 * Called when item session is finished
	 */
	/*
	private void updateItemSessionStatistics(){
		stats[currentAssessmentItemIndex].onEndItemSession();
		stats[currentAssessmentItemIndex].addMistakesCount(currentAssessmentItem.getMistakesCount());
	}
	*/
	
	//------------------------- IACTIVITY --------------------------------
/*
	@Override
	public void markAnswers() {
		if (currentAssessmentItem != null){
			currentAssessmentItem.markAnswers();
			stats[currentAssessmentItemIndex].addCheckIncident();
		}
	}
	
	@Override
	public void unmark() {
		if (currentAssessmentItem != null){
			currentAssessmentItem.unmark();
		}
	}


	@Override
	public void reset() {

		if (mode.canNavigate()){
		
			results[currentAssessmentItemIndex] = null;
			currentAssessmentItem.reset();
		}
		
	}

	@Override
	public void lock(boolean l) {
		currentAssessmentItem.lock(l);
		
	}

	@Override
	public void showCorrectAnswers() {
		
	}
	@Override
	public void onStateChanged(IInteractionModule sender) {
		currentAssessmentItem.process(sender != null, sender != null ? sender.getIdentifier() : "");
		updateHistory();
	}
	
*/
	//------------------------- STYLE --------------------------------

	public void updateAssessmentStyle(){
		String userAgent = styleManager.getUserAgent();
		styleManager.registerAssessmentStyles(dataManager.getAssessmentStyleLinksForUserAgent(userAgent));
	}

	public void updatePageStyle(){
		String userAgent = styleManager.getUserAgent();
		styleManager.registerItemStyles(dataManager.getPageStyleLinksForUserAgent(flowManager.getPageReference(), userAgent));
	}

}
