package com.klangner.qtieditor.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;

public class AssessmentView {

	/** model */
	private Assessment 			assessment;
	/** assessment editor panel */
	private Panel						mainPanel;
	/** Item tool bar */
	private Panel						toolbar;
	/** Add button */
	private Button					addButton;
	/** Grid with items */
	private Panel						itemsPanel;
	
	
	/**
	 * Constructor
	 */
	public AssessmentView(Assessment assessment){

		this.assessment = assessment;
	}
	
	
	/**
	 * @return view with player
	 */
	public Widget getView(){
		
		Grid	grid;
		
		mainPanel = new VerticalPanel();
		mainPanel.setStyleName("qe-assessment-editor");
		
		toolbar = new FlowPanel();
		toolbar.setStyleName("qe-toolbar");
		mainPanel.add(toolbar);
		addButton = new Button("Add...");
		toolbar.add(addButton);

		mainPanel.add(toolbar);

		grid = new Grid(2,2);
		grid.getColumnFormatter().addStyleName(0, "qe-label-column");
		grid.setText(0, 0, "Title: ");
		grid.setText(0, 1, assessment.getTitle());
		grid.setText(1, 0, "Items: ");
		itemsPanel = new VerticalPanel();
		grid.setWidget(1, 1, itemsPanel);
		
		mainPanel.add(grid);
		
		update();

		return mainPanel;
	}
	
	/**
	 * Update view when model changed
	 */
	public void update(){

		Grid	grid = new Grid(assessment.getItemCount(), 3);
		
		itemsPanel.add(grid);
		
		for(int i = 0; i < assessment.getItemCount(); i++){
			grid.setWidget(i, 0, new Label(assessment.getItemRef(i)));
			grid.setWidget(i, 1, new Button("Up"));
			grid.setWidget(i, 2, new Button("Down"));
		}
	
	}
	
}
