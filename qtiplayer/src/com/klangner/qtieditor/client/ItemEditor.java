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
package com.klangner.qtieditor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtieditor.client.model.EditableModuleFactory;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;
import com.klangner.qtiplayer.client.model.LoadException;
import com.klangner.qtiplayer.client.model.XMLDocument;

public class ItemEditor extends Composite{

  /** Model */
  private Assessment      assessment;
  /** current item object */
  private AssessmentItem  currentItem = null;
  /** current index */
  private int             currentIndex;
	/** Item editor panel */
	private Panel 					mainPanel;
	/** Item tool bar */
	private Panel 					toolbar;
	/** page counter */
	private Label						pageCounter;
	/** previous button */
	private Button					prevButton;
	/** Next button */
	private Button					nextButton;
	/** Add page button */
	private Button					addButton;
	/** Remove page from panel */
	private Button					removeButton;
	/** Put page in this panel */
	private Panel						pagePanel;

	/**
	 * Constructor
	 */
	
	public ItemEditor(Assessment assessment){

	  this.assessment = assessment;
	  initWidget(createView());
	  loadAssessmentItem(0);
	}
	
	
	/**
	 * @return view with player
	 */
	private Widget createView(){

		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("qe-item-editor");
		
		toolbar = new FlowPanel();
		toolbar.setStyleName("qe-toolbar");
		mainPanel.add(toolbar);
		addButton = new Button("Add...");
		toolbar.add(addButton);
		removeButton = new Button("Remove Page");
		toolbar.add(removeButton);

		prevButton = new Button("<<");
		prevButton.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        loadAssessmentItem(currentIndex-1);
      }
		});
		prevButton.setEnabled(false);
		toolbar.add(prevButton);
		pageCounter = new InlineLabel();
		toolbar.add(pageCounter);
		nextButton = new Button(">");
    nextButton.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent event) {
        loadAssessmentItem(currentIndex+1);
      }
    });
    nextButton.setEnabled(false);
		toolbar.add(nextButton);
		
		pagePanel = new VerticalPanel();
		pagePanel.setStyleName("qe-body");
		mainPanel.add(pagePanel);
		
		return mainPanel;
	}

  /**
   * Show assessment item in body part of player
   * @param index
   */
  private void loadAssessmentItem(int index){
    
    if(index >= 0 && index < assessment.getItemCount()){
      
      currentIndex = index;
      String  url = assessment.getItemRef(index);

      currentItem = new AssessmentItem(new EditableModuleFactory());
      try {
        currentItem.load(url, new IDocumentLoaded(){

          public void finishedLoading(XMLDocument doc) {
            onItemLoaded();
          }
        });
      } catch (LoadException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * create view for assessment item
   */
  private void onItemLoaded(){
    
    pageCounter.setText((currentIndex+1) + "/" + assessment.getItemCount());
    showPage(currentItem);
    if(currentIndex > 0)
      prevButton.setEnabled(true);
    else
      prevButton.setEnabled(false);
    
    if(currentIndex < assessment.getItemCount()-1)
      nextButton.setEnabled(true);
    else
      nextButton.setEnabled(false);
    
  }

  
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	private void showPage(AssessmentItem assessmentItem){

		TextBox itemTitleLabel;
		Label	label;
		Panel	panel;
		
		pagePanel.clear();

		panel = new HorizontalPanel();
		panel.setStyleName("qe-module-panel");
		label = new Label("Title: ");
		panel.add(label);
		itemTitleLabel = new TextBox();
		itemTitleLabel.setText(assessmentItem.getTitle());
		panel.add(itemTitleLabel);
		pagePanel.add(panel);

		for(int i = 0; i < assessmentItem.getModuleCount(); i++){
//			moduleGrid.setText(i+1, 0, "Text");
			pagePanel.add(assessmentItem.getModule(i));
		}

	}
	
}
