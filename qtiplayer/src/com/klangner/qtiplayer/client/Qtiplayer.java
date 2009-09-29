package com.klangner.qtiplayer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Qtiplayer implements EntryPoint {

	/** Normal work mode */
	private static int SHOW_ACTIVITY_MODE = 0;
	/** Show results mode */
	private static int SHOW_RESULT_MODE = 1;

	private Assessment			assessment;
	private PlayerView			playerView;
	private	int							workMode;
	private int							currentItemIndex;
	private AssessmentItem	currentItem;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		Element element = RootPanel.get("player").getElement();
		Node node = element.getFirstChild();
		element.removeChild(node);
		
		loadAssessment(node.getNodeValue());
	}
	
	/**
	 * Create user interface
	 */
	private void createViews() {
		
		playerView = new PlayerView(assessment);
		RootPanel.get("player").add(playerView.getView());
	}
	
	/**
	 * create view for assessment item
	 */
	private void createItemViews(){
		
		if(SHOW_ACTIVITY_MODE == workMode){
			playerView.getCheckButton().setText("Check");
			playerView.getCheckButton().addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					checkScore();
				}
			});
		}
		else if(currentItemIndex+1 < assessment.getItemCount()){
			playerView.getCheckButton().setText("Next");
			playerView.getCheckButton().addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					showItem(currentItemIndex+1);
				}
			});
		}
		else{
			playerView.getCheckButton().setText("Finish");
			playerView.getCheckButton().addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					showAssessmentResult();
				}
			});
		}

		playerView.showAssessmentItem(currentItem);
	}
	
	/**
	 * Load assessment file from server
	 */
	private void loadAssessment(String url){
		
		assessment = new Assessment();
		assessment.load(url, new IDocumentLoaded(){

			public void finishedLoading() {
        createViews();
        showItem(0);
			}
		});
	}
	
	
	/**
	 * Show assessment item in body part of player
	 * @param index
	 */
	private void showItem(int index){
		String 					url = assessment.getItemRef(index);

		currentItem = new AssessmentItem();
		currentItemIndex = index;
		workMode = SHOW_ACTIVITY_MODE;

		currentItem.load(url, new IDocumentLoaded(){

			public void finishedLoading() {
				createItemViews();
			}
		});

	}
	
	/**
	 * Check score
	 * @param index
	 */
	private void checkScore(){

		workMode = SHOW_RESULT_MODE;
		createItemViews();

	}
	
	/**
	 * Show assessment item in body part of player
	 * @param index
	 */
	private void showAssessmentResult(){
		
		workMode = SHOW_RESULT_MODE;
		playerView.showResultPage();
	}
	 
}
