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

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;
import com.klangner.qtiplayer.client.model.XMLDocument;

public class AssessmentEditor extends Composite{

	/** model */
	private Assessment assessment;
	/** title edit box */
	private TextBox    titleTextBox;
	/** Grid with items */
	private Panel      itemsPanel;
	
	
	/**
	 * Constructor
	 */
	public AssessmentEditor(Assessment assessment){

		this.assessment = assessment;
    initWidget(createView());
	}
	
	
	/**
	 * Create editor view 
	 * @return view with player
	 */
	private Widget createView(){
		
	  Panel  mainPanel;
		Grid	 grid;
		
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("qe-assessment-editor");
		
		grid = new Grid(2,2);
		grid.getColumnFormatter().addStyleName(0, "qe-label-column");
		
		titleTextBox = new TextBox();
		titleTextBox.setWidth("100%");
		titleTextBox.setText(assessment.getTitle());
		grid.setText(0, 0, "Title: ");
		grid.setWidget(0, 1, titleTextBox);
		grid.setText(1, 0, "Items: ");
		itemsPanel = new VerticalPanel();
		grid.setWidget(1, 1, itemsPanel);
		
		mainPanel.add(grid);
		
		assessment.loadTitles(new IDocumentLoaded(){

      public void finishedLoading(XMLDocument document) {
        updateItemList();
      }
		  
		});

		return mainPanel;
	}
	
	/**
	 * Update view when model changed
	 */
	private void updateItemList(){

		Grid	grid = new Grid(assessment.getItemCount(), 3);

    grid.getColumnFormatter().addStyleName(0, "qe-item-column");
		itemsPanel.add(grid);
		
		for(int i = 0; i < assessment.getItemCount(); i++){
			grid.setWidget(i, 0, new Label(assessment.getItemTitle(i)));
			grid.setWidget(i, 1, new Button("Up"));
			grid.setWidget(i, 2, new Button("Down"));
		}
	
	}
	
}
