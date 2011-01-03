package com.qtitools.player.client.controller;

import java.util.Vector;

import com.google.inject.Inject;
import com.qtitools.player.client.controller.communication.ActivityActionType;
import com.qtitools.player.client.controller.communication.DisplayOptions;
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
import com.qtitools.player.client.model.ItemVariablesAccessor;
import com.qtitools.player.client.style.StyleSocket;
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
 * TODO use com.google.gwt.event.shared.HandlerManager to propagate events
 * TODO use com.google.gwt.user.client.History to change application states
 * 
 * @author Rafal Rybacki
 *
 */
public class DeliveryEngine implements DataLoaderEventListener, FlowEventsListener, StateChangedEventsListener {

	public EngineModeManager mode;
	
	private int masteryScore;
	
	private StyleLinkManager styleManager;
	
	private DataSourceManager dataManager;
	
	private FlowManager flowManager;
	private SessionDataManager sessionDataManager;
	
	private PlayerViewSocket playerViewSocket;
	private DeliveryEngineEventListener deliveryEngineEventsListener;
	
	private AssessmentController assessmentController;

	private StyleSocket styleSocket;
	
	/**
	 * C'tor.
	 */
	@Inject
	public DeliveryEngine(PlayerViewSocket pvs, DataSourceManager dsm, StyleSocket ss){
		
		playerViewSocket = pvs;
		dataManager = dsm;
		dsm.setDataLoaderEventListener( this );
		styleSocket = ss;
		
		mode = new EngineModeManager();
		styleManager = new StyleLinkManager();
		masteryScore = 100;

		
		flowManager = new FlowManager(this);
		sessionDataManager = new SessionDataManager(this);
		
		assessmentController = new AssessmentController(playerViewSocket.getAssessmentViewSocket(), flowManager, sessionDataManager);
		assessmentController.setStyleSocket( ss );
		
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
		getDeliveryEngineEventsListener().onAssessmentStarted();
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
	public void onNavigatePageSwitching() {
		if (flowManager.getCurrentPageType() == PageType.TEST){
			getDeliveryEngineEventsListener().onTestPageSwitching();
		}
	}
	
	@Override
	public void onNavigatePageSwitched() {
		PageReference pr = flowManager.getPageReference();
		PageData pd = dataManager.generatePageData(pr);
		
		// TODO style provider should listen directly to navigation events via HandlerManager or other event bus 
		styleSocket.setCurrentPages( pr );
		
		assessmentController.closePage();
		
		if (pd.type == PageType.SUMMARY)
			((PageDataSummary)pd).setSessionData( sessionDataManager.getSessionData() );
		assessmentController.initPage(pd);
		if (pd.type == PageType.SUMMARY)
			getDeliveryEngineEventsListener().onSummary();

		if (pd.type == PageType.TEST)
			getDeliveryEngineEventsListener().onTestPageSwitched();
		
		updatePageStyle();
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

	public void setDisplayOptions(DisplayOptions o){
		flowManager.setDisplayOptions(o);
	}
	
	public ItemVariablesAccessor getItemVariablesAccessor(){
		return assessmentController.getItemVariablesAccessor();
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

			@Override
			public Result[] getItemsResults() {
				Result[] results = sessionDataManager.getSessionData().results;
				return results;
			}

			@Override
			public int[] getItemsMistakes() {
				int[] mistakes = sessionDataManager.getSessionData().mistakes;
				return mistakes;
			}

			@Override
			public int[] getItemsChecks() {
				int[] checks = sessionDataManager.getSessionData().checks;
				return checks;
			}

			@Override
			public int[] getItemsTimes() {
				int[] times = sessionDataManager.getSessionData().times;
				return times;
			}
		};
	}

	public void updateAssessmentStyle(){
		String userAgent = styleManager.getUserAgent();
		Vector<String> links = dataManager.getAssessmentStyleLinksForUserAgent(userAgent);
		styleManager.registerAssessmentStyles( links );
	}

	public void updatePageStyle(){
		String userAgent = styleManager.getUserAgent();
		Vector<String> links = dataManager.getPageStyleLinksForUserAgent(flowManager.getPageReference(), userAgent);
		styleManager.registerItemStyles( links );
	}

	public void setDeliveryEngineEventsListener(
			DeliveryEngineEventListener deliveryEngineEventsListener) {
		this.deliveryEngineEventsListener = deliveryEngineEventsListener;
	}

	public DeliveryEngineEventListener getDeliveryEngineEventsListener() {
		return deliveryEngineEventsListener;
	}

}
