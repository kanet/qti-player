/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.qtitools.player.client.view;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.model.AssessmentItem;

public class PlayerWidget extends Composite{

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
	private HorizontalPanel				footerContainer;
	/** Check button */ 
	private PushButton					checkButton;
	/** Reset button */ 
	private PushButton					resetButton;
	/** Next button */ 
	private PushButton					prevButton;
	/** Next button */ 
	private PushButton					nextButton;
	/** Finish button */ 
	private PushButton					finishButton;
	
	/**
	 * constructor
	 * @param assessment to show
	 */
	public PlayerWidget(Assessment assessment){
		//this.assessment = assessment;
		
		initWidget(createView(assessment));
	}
	
	/**
	 * @return check button
	 */
	public PushButton getCheckButton(){
		return checkButton;
	}
	
	/**
	 * @return finish button
	 */
	public PushButton getFinishButton(){
		return finishButton;
	}
	
	/**
	 * @return next button
	 */
	public PushButton getNextButton(){
		return nextButton;
	}
	
	/**
	 * @return previous button
	 */
	public PushButton getPrevButton(){
		return prevButton;
	}
	
	/**
	 * @return reset button
	 */
	public PushButton getResetButton(){
		return resetButton;
	}
	
	/**
	 * Show error instead of page
	 */
	public void showError(String message){

		Label	errorLabel = new Label(message);
		bodyPanel.clear();
		feedbackLabel.setText("");

		errorLabel.setStyleName("qp-error");
		bodyPanel.add(errorLabel);
	}
	
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showPage(Assessment assessment, AssessmentItem assessmentItem, int pageNumber){

		Label itemTitleLabel = new Label();
		
//		this.assessmentItem = assessmentItem; 
		bodyPanel.clear();
		feedbackLabel.setText("");

		counterLabel.setText(pageNumber + "/" + assessment.getAssessmentItemsCount());		

		itemTitleLabel.setText(pageNumber + ". " + assessmentItem.getTitle());
		itemTitleLabel.setStyleName("qp-item-title");
		bodyPanel.add(itemTitleLabel);
		
		bodyPanel.add(assessmentItem.getContentWidget());
		
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
	
  /**
   * @return view with player
   */
  private Widget createView(Assessment assessment){
    Label           label;
    HorizontalPanel header = new HorizontalPanel();

    playerPanel = new VerticalPanel();
    playerPanel.setStyleName("qp-player");
    header.setStyleName("qp-header");
    label = new Label(assessment.getTitle());
    label.setStyleName("qp-assessment-title");
    header.add(label);
    counterLabel = new Label("1/" + assessment.getAssessmentItemsCount());
    counterLabel.setStyleName("qp-page-counter");
    header.add(counterLabel);
    playerPanel.add(header);

    bodyPanel = new VerticalPanel();
    bodyPanel.setStyleName("qp-body");
    playerPanel.add(bodyPanel);
    
    feedbackLabel = new Label();
    feedbackLabel.setStyleName("qp-feedback");
    playerPanel.add(feedbackLabel);

    footerContainer = new HorizontalPanel();

    checkButton = new PushButton();
    checkButton.setWidth("70");
    checkButton.setHeight("20");
    checkButton.setStylePrimaryName("qp-check-button");
    footerContainer.add(checkButton);
    
    resetButton = new PushButton();
    resetButton.setWidth("70");
    resetButton.setHeight("20");
    resetButton.setStylePrimaryName("qp-reset-button");
    footerContainer.add(resetButton);

    prevButton = new PushButton();
    //prevButton.setWidth("70");
    //prevButton.setHeight("20");
    prevButton.setStylePrimaryName("qp-prev-button");
    footerContainer.add(prevButton);
    
    nextButton = new PushButton();
    nextButton.setWidth("70");
    nextButton.setHeight("20");
    nextButton.setStylePrimaryName("qp-next-button");
    footerContainer.add(nextButton);
    
    finishButton = new PushButton();
    finishButton.setWidth("70");
    finishButton.setHeight("20");
    finishButton.setStylePrimaryName("qp-finish-button");
    footerContainer.add(finishButton);
        
    footer = new FlowPanel();
    footer.setStyleName("qp-footer");
    footer.add(footerContainer);
    playerPanel.add(footer);
    
    return playerPanel;
  }
  
}
