package com.klangner.qtiplayer.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class PlayerView {

	/** Show this assessment */
	private Assessment			assessment;
	/** Body panel. AssessmentItem view will be shown there */
	private VerticalPanel 	body;
	/** Check/Next/Finish button */ 
	private Button					checkButton;
	
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
	 * @return view with player
	 */
	public Widget getView(){
		VerticalPanel playerPanel = new VerticalPanel();
		Label					label;
		Label 				header = new Label();
		Label 				feedback = new Label();
		Label 				footer = new Label();
		

		playerPanel.setStyleName("qp-player");
		header.setText(assessment.getTitle());
		header.setStyleName("qp-header");
		playerPanel.add(header);

		body = new VerticalPanel();
		body.setStyleName("qp-body");
		label = new Label("There are: " + assessment.getItemCount() + " items.");
		body.add(label);
		playerPanel.add(body);
		
		feedback.setText("feeback");
		feedback.setStyleName("qp-feedback");
		playerPanel.add(feedback);

		checkButton = new Button("Check");
		checkButton.setStyleName("qp-checkbutton");
		playerPanel.add(checkButton);
		
		footer.setText("Footer");
		footer.setStyleName("qp-footer");
		playerPanel.add(footer);

		return playerPanel;
	}
	
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showAssessmentItem(AssessmentItem assessmentItem){

		body.clear();
		for(int i = 0; i < assessmentItem.getModuleCount(); i++){
			body.add(assessmentItem.getModule(i).getView());
		}
	}
	
	/**
	 * Show view with assessment score
	 * @param index of assessment item
	 */
	public void showResultPage(){

		body.clear();
		body.add(new Label("Well done"));
	}
	
}
