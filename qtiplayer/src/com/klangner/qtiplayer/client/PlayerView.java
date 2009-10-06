package com.klangner.qtiplayer.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class PlayerView {

	/** Show this assessment */
	private Assessment			assessment;
	/** Counter label */
	private Label						counterLabel;
	/** Body panel. AssessmentItem view will be shown there */
	private VerticalPanel 	bodyPanel;
	/** Assessment item feedback */
	private Label 					feedbackLabel;
	/** Check button */ 
	private Button					checkButton;
	/** Next button */ 
	private Button					nextButton;
	/** Finish button */ 
	private Button					finishButton;
	
	/**
	 * constructor
	 * @param assessment to show
	 */
	public PlayerView(Assessment assessment){
		this.assessment = assessment;
	}
	
	/**
	 * @return check button
	 */
	public Button getCheckButton(){
		return checkButton;
	}
	
	/**
	 * @return next button
	 */
	public Button getNextButton(){
		return nextButton;
	}
	
	/**
	 * @return finish button
	 */
	public Button getFinishButton(){
		return finishButton;
	}
	
	/**
	 * @return view with player
	 */
	public Widget getView(){
		VerticalPanel 	playerPanel = new VerticalPanel();
		Label						label;
		HorizontalPanel header = new HorizontalPanel();
		HorizontalPanel	footer = new HorizontalPanel();

		
		playerPanel.setStyleName("qp-player");
		header.setStyleName("qp-header");
		label = new Label(assessment.getTitle());
		label.setStyleName("qp-assessment-title");
		header.add(label);
		counterLabel = new Label("1/" + assessment.getItemCount());
		counterLabel.setStyleName("qp-page-counter");
		header.add(counterLabel);
		playerPanel.add(header);

		bodyPanel = new VerticalPanel();
		bodyPanel.setStyleName("qp-body");
		playerPanel.add(bodyPanel);
		
		feedbackLabel = new Label();
		feedbackLabel.setStyleName("qp-feedback");
		playerPanel.add(feedbackLabel);

		footer.setStyleName("qp-footer");
		playerPanel.add(footer);

		checkButton = new Button("Check");
		checkButton.setStyleName("qp-check-button");
		footer.add(checkButton);
		nextButton = new Button("Next");
		nextButton.setStyleName("qp-next-button");
		footer.add(nextButton);
		
		finishButton = new Button("Finish");
		finishButton.setStyleName("qp-finish-button");
		footer.add(finishButton);
		
		return playerPanel;
	}
	
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showAssessmentItem(AssessmentItem assessmentItem, int pageNumber){

		Label itemTitleLabel = new Label();
		
		bodyPanel.clear();
		feedbackLabel.setText("");

		counterLabel.setText(pageNumber + "/" + assessment.getItemCount());		

		itemTitleLabel.setText(pageNumber + ". " + assessmentItem.getTitle());
		itemTitleLabel.setStyleName("qp-item-title");
		bodyPanel.add(itemTitleLabel);
		for(int i = 0; i < assessmentItem.getModuleCount(); i++){
			bodyPanel.add(assessmentItem.getModule(i));
		}
	}
	
	public void showFeedback(String feedback){
		feedbackLabel.setText(feedback);
	}
	
	/**
	 * Show view with assessment score
	 * @param index of assessment item
	 */
	public void showResultPage(String message){

		bodyPanel.clear();
		showFeedback("");
		bodyPanel.add(new Label(message));
	}
	
}
