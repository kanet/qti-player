package com.klangner.qtiplayer.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class PlayerView {

	/** Show this assessment */
	private Assessment			assessment;
	/** current item */
//	private AssessmentItem 	assessmentItem;
	private Panel 					playerPanel;
	/** Counter label */
	private Label						counterLabel;
	/** Body panel. AssessmentItem view will be shown there */
	private Panel 					bodyPanel;
	/** Assessment item feedback */
	private Label 					feedbackLabel;
	/** Footer */
	private Panel						footer;
	/** Check button */ 
	private Button					checkButton;
	/** Reset button */ 
	private Button					resetButton;
	/** Next button */ 
	private Button					prevButton;
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
	 * @return finish button
	 */
	public Button getFinishButton(){
		return finishButton;
	}
	
	/**
	 * @return next button
	 */
	public Button getNextButton(){
		return nextButton;
	}
	
	/**
	 * @return previous button
	 */
	public Button getPrevButton(){
		return prevButton;
	}
	
	/**
	 * @return reset button
	 */
	public Button getResetButton(){
		return resetButton;
	}
	
	/**
	 * @return view with player
	 */
	public Widget getView(){
		Label						label;
		HorizontalPanel header = new HorizontalPanel();

		playerPanel = new VerticalPanel();
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

		footer = new FlowPanel();
		footer.setStyleName("qp-footer");
		playerPanel.add(footer);

		checkButton = new Button("Check");
		checkButton.setStyleName("qp-check-button");
		footer.add(checkButton);
		resetButton = new Button("Reset");
		resetButton.setStyleName("qp-reset-button");
		footer.add(resetButton);

		prevButton = new Button("Previuos");
		prevButton.setStyleName("qp-prev-button");
		footer.add(prevButton);
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
	public void showPage(AssessmentItem assessmentItem, int pageNumber){

		Label itemTitleLabel = new Label();
		
//		this.assessmentItem = assessmentItem; 
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
		footer.setVisible(false);
		showFeedback("");
		bodyPanel.add(new Label(message));
	}
	
}
