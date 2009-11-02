package com.klangner.qtieditor.client;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class EditorView {

	/** assessment editor panel */
	private ItemView				itemView;
	/** assessment editor panel */
	private AssessmentView	assessmentView;
	
	
	/**
	 * Constructor
	 */
	public EditorView(Assessment assessment){
		
		assessmentView = new AssessmentView(assessment);
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
