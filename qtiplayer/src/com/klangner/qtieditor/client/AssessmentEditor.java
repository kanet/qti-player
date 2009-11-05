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
		
		grid = new Grid(3,2);
		grid.setWidth("100%");
		grid.getColumnFormatter().addStyleName(0, "qe-label-column");
		
		titleTextBox = new TextBox();
		titleTextBox.setWidth("100%");
		titleTextBox.setText(assessment.getTitle());
		grid.setText(0, 0, "Title: ");
		grid.setWidget(0, 1, titleTextBox);
		grid.setText(1, 0, "Pages: ");
		itemsPanel = new VerticalPanel();
    itemsPanel.setWidth("100%");
		grid.setWidget(2, 1, itemsPanel);
		
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

    grid.setWidth("100%");
    grid.getColumnFormatter().addStyleName(0, "qe-item-column");
    
    itemsPanel.clear();
		itemsPanel.add(grid);
		
		for(int i = 0; i < assessment.getItemCount(); i++){
		  Button  upButton = new Button("Up");
		  Button  downButton = new Button("Down");
			
		  if(i == 0)
		    upButton.setEnabled(false);
		  else
		    upButton.addClickHandler(new UpHandler(i));

		  if(i+1 == assessment.getItemCount())
        downButton.setEnabled(false);
      else
        downButton.addClickHandler(new DownHandler(i));

		  grid.setWidget(i, 0, new Label(assessment.getItemTitle(i)));
			grid.setWidget(i, 1, upButton);
			grid.setWidget(i, 2, downButton);
		}
	
	}

	/**
	 * Up button action
	 */
	class UpHandler implements ClickHandler{

	  private int index;
	  
	  public UpHandler(int index){
	    this.index = index;
	  }
	  
    public void onClick(ClickEvent event) {
      assessment.moveItem(index, index-1);
      updateItemList();
    }
	};

  /**
   * Down button action
   */
  class DownHandler implements ClickHandler{

    private int index;
    
    public DownHandler(int index){
      this.index = index;
    }
    
    public void onClick(ClickEvent event) {
      assessment.moveItem(index, index+1);
      updateItemList();
    }
  };
	
}
