package com.klangner.qtieditor.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.AssessmentItem;

public class ItemView {

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
	public ItemView(){
	}
	
	
	/**
	 * @return view with player
	 */
	public Widget getView(){

		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("qe-item-editor");
		
		toolbar = new FlowPanel();
		toolbar.setStyleName("qe-toolbar");
		mainPanel.add(toolbar);
		addButton = new Button("Add...");
		toolbar.add(addButton);
		removeButton = new Button("Remove Item");
		toolbar.add(removeButton);

		prevButton = new Button("<<");
		toolbar.add(prevButton);
		pageCounter = new InlineLabel("1/10");
		toolbar.add(pageCounter);
		nextButton = new Button(">");
		toolbar.add(nextButton);
		
		pagePanel = new VerticalPanel();
		pagePanel.setStyleName("qe-body");
		mainPanel.add(pagePanel);
		
		return mainPanel;
	}

	
	/**
	 * Create view for given assessment item and show it in player
	 * @param index of assessment item
	 */
	public void showPage(AssessmentItem assessmentItem){

		Label itemTitleLabel = new Label();
		Grid	moduleGrid = new Grid(assessmentItem.getModuleCount()+1, 5);
		
//		this.assessmentItem = assessmentItem; 
		pagePanel.clear();
		moduleGrid.getColumnFormatter().addStyleName(0, "qe-label-column");
		pagePanel.add(moduleGrid);

		itemTitleLabel.setText(assessmentItem.getTitle());
		itemTitleLabel.setStyleName("qp-item-title");
		moduleGrid.setStyleName("qe-module-table");
		moduleGrid.setText(0, 0, "Title");
		moduleGrid.setWidget(0, 1, itemTitleLabel);
		moduleGrid.setWidget(0, 2, new Button("Edit"));

		for(int i = 0; i < assessmentItem.getModuleCount(); i++){
			moduleGrid.setText(i+1, 0, "Text");
			moduleGrid.setWidget(i+1, 1, assessmentItem.getModule(i));
			moduleGrid.setWidget(i+1, 2, new Button("Edit"));
			Panel panel = new HorizontalPanel();
			panel.add(new Button("Up"));
			panel.add(new Button("Down"));
//			moduleGrid.setWidget(i+1, 3, panel);
//			moduleGrid.setWidget(i+1, 4, new Button("Remove"));
		}

	}
	
}
