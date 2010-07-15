package com.qtitools.player.client.controller.flow.navigation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.ItemActivityOptions;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;
import com.qtitools.player.client.controller.communication.PageType;

public class NavigationView implements NavigationViewSocket {

	public NavigationView(NavigationCommandsListener l){
		listener = l;
	}
	
	private NavigationCommandsListener listener;
	
	public void init(int itemsCount){

		// BUTTONS MENU
		
		menuPanel = new FlowPanel();
		menuPanel.setStyleName("qp-footer-buttons");

	    checkButton = new PushButton();
	    checkButton.setStylePrimaryName("qp-check-button");
	    checkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.checkPage();
			}
		});
	    menuPanel.add(checkButton);
	    
	    continueItemButton = new PushButton();
	    continueItemButton.setStylePrimaryName("qp-reset-button");
	    continueItemButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.continuePage();
			}
		});
	    menuPanel.add(continueItemButton);

	    prevButton = new PushButton();
	    prevButton.setStylePrimaryName("qp-prev-button");
	    prevButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.previousPage();
			}
		});
	    menuPanel.add(prevButton);
	    
	    nextButton = new PushButton();
	    nextButton.setStylePrimaryName("qp-next-button");
	    nextButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.nextPage();
			}
		});
	    menuPanel.add(nextButton);
	    
	    finishButton = new PushButton();
	    finishButton.setStylePrimaryName("qp-finish-button");
	    finishButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.gotoSummary();
			}
		});
	    menuPanel.add(finishButton);
	    
	    summaryButton = new PushButton();
	    summaryButton.setStylePrimaryName("qp-summary-button");
	    summaryButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.gotoSummary();
			}
		});
	    menuPanel.add(summaryButton);
	    
	    continueAssessmentButton = new PushButton();
	    continueAssessmentButton.setStylePrimaryName("qp-resultpage-continue");
	    continueAssessmentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.gotoLastPage();
			}
		});
	    menuPanel.add(continueAssessmentButton);

	    // PAGES COMBO

	    comboListBox = new ListBox();
	    comboListBox.setVisibleItemCount(1);
	    comboListBox.setStyleName("qp-page-counter-list");
	    for (int p = 0 ; p < itemsCount; p ++)
	    	comboListBox.addItem(String.valueOf(p+1));
	    comboListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				listener.gotoPage(((ListBox)event.getSource()).getSelectedIndex());
			}
		});
	        
	    
	    comboLabel = new Label("/" + itemsCount);
	    comboLabel.setStyleName("qp-page-counter-count");
	    
	    comboPanel = new FlowPanel();
	    comboPanel.setStyleName("qp-page-counter");
	    comboPanel.add(comboListBox);
	    comboPanel.add(comboLabel);
	    
	}
	
	private Panel menuPanel;
	private PushButton checkButton;
	private PushButton continueItemButton;
	private PushButton prevButton; 
	private PushButton nextButton;
	private PushButton finishButton; 
	private PushButton summaryButton;
	private PushButton continueAssessmentButton;
	
	private Panel comboPanel;
	private Label comboLabel;
	private ListBox	comboListBox;
	
	
	public void updateButtons(PageType pageType, int pageIndex, int pageCount, FlowOptions flowOptions, boolean isChecked, ItemActivityOptions iao){
		checkButton.setVisible(!isChecked  &&  pageType == PageType.TEST);
		continueItemButton.setVisible(isChecked  &&  pageType == PageType.TEST);
		prevButton.setVisible(pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE);
		prevButton.setEnabled(pageIndex != 0);
		nextButton.setVisible((pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE)  ||  pageType == PageType.TOC);
		nextButton.setEnabled(pageIndex < pageCount-1);
		finishButton.setVisible(pageType == PageType.TEST  &&  flowOptions.showSummary);
		finishButton.setEnabled(pageIndex == pageCount-1);
		summaryButton.setVisible(iao.previewMode  &&  pageType == PageType.TEST);
		continueAssessmentButton.setVisible(pageType == PageType.SUMMARY);
		comboPanel.setVisible(pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE);
		setComboPageIndex(pageIndex);
		
	}
	
	public Widget getMenuView(){
		return menuPanel;
	}

	public Widget getComboView(){
		return comboPanel;
	}
	
	private void setComboPageIndex(int index){
		if (comboListBox != null)
			comboListBox.setSelectedIndex(index);
	}
	
}
