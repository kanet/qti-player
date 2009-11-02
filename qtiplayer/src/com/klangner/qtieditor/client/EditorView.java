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

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class EditorView {

	/** assessment editor panel */
	private ItemView				itemView;
	/** assessment editor panel */
	private AssessmentEditorWidget	assessmentView;
	
	
	/**
	 * Constructor
	 */
	public EditorView(Assessment assessment){
		
		assessmentView = new AssessmentEditorWidget(assessment);
		itemView = new ItemView();
	}
	
	
	/**
	 * @return view with player
	 */
	public Widget getView(){

		TabPanel	tabPanel = new TabPanel();
		
		tabPanel.setStyleName("qe-tab-panel");
		tabPanel.add(itemView.getView(), "Item");
		tabPanel.add(assessmentView.getView(), "Assessment");
		tabPanel.selectTab(0);
		
		return tabPanel;
	}

	
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showPage(AssessmentItem assessmentItem){

			itemView.showPage(assessmentItem);
	}
	
}
