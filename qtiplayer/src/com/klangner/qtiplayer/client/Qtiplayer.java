package com.klangner.qtiplayer.client;

import java.util.Vector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;
import com.klangner.qtiplayer.client.model.Result;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Qtiplayer implements EntryPoint {

	/** Wrap this tag */
	private static final String	PLAYER_NODE_ID = "player";
	/** Single instance handler */
	private static Qtiplayer		theInstance;
	/** Current assessment */
	private Assessment					assessment;
	/** Player view */
	private PlayerView					playerView;
	private int									currentItemIndex;
	private AssessmentItem			currentItem;
	private Vector<Result>			results = new Vector<Result>();
	
	/**
	 * Load js interface
	 * @param url
	 */
	 public static void load(String url) {
		 theInstance.loadAssessment(url);
	 }
	 
   public static native void defineAPIMethods() /*-{
      $wnd.qpLoadAssessment = function(url) {
        @com.klangner.qtiplayer.client.Qtiplayer::load(Ljava/lang/String;)(url);
      }
   }-*/;

   /**
	 * Send result to javascript native method
	 * @param msg
	 */
	public static native void sendResult(int score, int max) /*-{
		
		if(typeof $wnd.qpSendResult == 'function') {
			$wnd.qpSendResult(score, max);
		}
	}-*/;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Define js API
		theInstance = this;
		defineAPIMethods();
	}
	
	/**
	 * Create user interface
	 */
	private void initialize() {
		
		RootPanel rootPanel = RootPanel.get(PLAYER_NODE_ID); 
		Element element = rootPanel.getElement();
		Node node = element.getFirstChild();
		element.removeChild(node);
		
		playerView = new PlayerView(assessment);
		rootPanel.add(playerView.getView());
		
		playerView.getCheckButton().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				checkItemScore();
			}
		});

		playerView.getNextButton().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				loadAssessmentItem(currentItemIndex+1);
			}
		});
		
		playerView.getFinishButton().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				showAssessmentResult();
			}
		});


		// Switch to first item
		loadAssessmentItem(0);
	}
	
	/**
	 * create view for assessment item
	 */
	private void showCurrentItem(){
		
		playerView.showAssessmentItem(currentItem, currentItemIndex+1);
		
		playerView.getCheckButton().setVisible(true);
		playerView.getNextButton().setVisible(false);
		playerView.getFinishButton().setVisible(false);
		
	}
	
	/**
	 * Load assessment file from server
	 */
	private void loadAssessment(String url){
		
		assessment = new Assessment();
		assessment.load(GWT.getHostPageBaseURL() + url, new IDocumentLoaded(){

			public void finishedLoading() {
        initialize();
			}
		});
	}
	
	
	/**
	 * Show assessment item in body part of player
	 * @param index
	 */
	private void loadAssessmentItem(int index){
		String 					url = assessment.getItemRef(index);

		currentItem = new AssessmentItem();
		currentItemIndex = index;

		currentItem.load(url, new IDocumentLoaded(){

			public void finishedLoading() {
				showCurrentItem();
			}
		});

	}
	
	/**
	 * Check score
	 * @param index
	 */
	private void checkItemScore(){

		playerView.getCheckButton().setVisible(false);
		
		if(currentItemIndex+1 < assessment.getItemCount()){
			playerView.getNextButton().setVisible(true);
		}
		else{
			playerView.getFinishButton().setVisible(true);
		}

		results.add(currentItem.getResponseProcesing().getResult());
		playerView.showFeedback(currentItem.getResponseProcesing().getFeedback());
	}
	
	/**
	 * Show assessment item in body part of player
	 * @param index
	 */
	private void showAssessmentResult(){
		int score = 0;
		int max = 0;
		
		for(int i = 0; i < results.size(); i++){
			Result result = results.elementAt(i);
			score += result.getScore();
			max += result.getMaxPoints();
		}
		
		playerView.getCheckButton().setVisible(false);
		playerView.getNextButton().setVisible(false);
		playerView.getFinishButton().setVisible(false);
		playerView.showResultPage("Your score is: " + (int)((score * 100)/max) + "%");
		sendResult(score, max);
	}
	 
}
