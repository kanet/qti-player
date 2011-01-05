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
import com.qtitools.player.client.controller.communication.DisplayOptions;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.ItemParameters;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.util.localisation.LocalePublisher;
import com.qtitools.player.client.util.localisation.LocaleVariable;

public class NavigationView implements NavigationViewSocket {

	public NavigationView(NavigationCommandsListener l){
		listener = l;
	}
	
	private NavigationCommandsListener listener;
	
	public void init(int itemsCount, FlowOptions flowOptions){

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
				listener.gotoFirstPage();
			}
		});
	    menuPanel.add(continueAssessmentButton);
	    
	    previewAssessmentButton = new PushButton();
	    previewAssessmentButton.setStylePrimaryName("qp-resultpage-preview");
	    previewAssessmentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listener.previewPage(0);
			}
		});
	    menuPanel.add(previewAssessmentButton);

	    // PAGES COMBO

	    final boolean showToC = flowOptions.showToC;
	    final boolean showSummary = flowOptions.showSummary;
	    comboListBox = new ListBox();
	    comboListBox.setVisibleItemCount(1);
	    comboListBox.setStyleName("qp-page-counter-list");
	    if (showToC)
	    	comboListBox.addItem(LocalePublisher.getText(LocaleVariable.COMBO_TOC));
	    for (int p = 0 ; p < itemsCount; p ++)
	    	comboListBox.addItem(String.valueOf(p+1));	   
	    if (showSummary)
	    	comboListBox.addItem(LocalePublisher.getText(LocaleVariable.COMBO_SUMMARY)); 
	    comboListBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				
				boolean firstIndex = ((ListBox)event.getSource()).getSelectedIndex() == 0;
				boolean lastIndex = ((ListBox)event.getSource()).getSelectedIndex() == ((ListBox)event.getSource()).getItemCount()-1;
				int pagesCount = ((ListBox)event.getSource()).getItemCount();
				if (showToC)
					pagesCount--;
				if (showSummary)
					pagesCount--;
				
				if (firstIndex){
					if (showToC)
						listener.gotoToc();
					else
						listener.gotoPage(0);
				} else if (lastIndex){
					if (showSummary)
						listener.gotoSummary();
					else
						listener.gotoPage(pagesCount-1);
				} else {
					listener.gotoPage(((ListBox)event.getSource()).getSelectedIndex() - ((showToC)?1:0));
				}
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
	private PushButton previewAssessmentButton;
	
	private Panel comboPanel;
	private Label comboLabel;
	private ListBox	comboListBox;
	
	
	public void updateButtons(PageType pageType, int pageIndex, int pageCount, FlowOptions flowOptions, boolean isCheck, boolean isAnswers, 
			DisplayOptions displayOptions, ItemParameters itemParameters){
		checkButton.setVisible(flowOptions.showCheck  &&  !isCheck  &&  !isAnswers  &&  pageType == PageType.TEST  &&  !displayOptions.isPreviewMode()  &&  itemParameters.getModulesCount() > 0);
		continueItemButton.setVisible((isCheck || isAnswers)  &&  pageType == PageType.TEST  &&  !displayOptions.isPreviewMode());
		prevButton.setVisible(pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE);
		prevButton.setEnabled(flowOptions.showToC  ||  pageIndex > 0);
		nextButton.setVisible((pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE)  ||  pageType == PageType.TOC);
		nextButton.setEnabled(pageIndex < pageCount-1);
		finishButton.setVisible(pageType == PageType.TEST  &&  flowOptions.showSummary  &&  !displayOptions.isPreviewMode());
		finishButton.setEnabled(pageIndex == pageCount-1);
		summaryButton.setVisible(displayOptions.isPreviewMode()  &&  pageType == PageType.TEST);
		continueAssessmentButton.setVisible(pageType == PageType.SUMMARY);
		previewAssessmentButton.setVisible(pageType == PageType.SUMMARY);
		comboPanel.setVisible(pageType == PageType.TEST  &&  flowOptions.itemsDisplayMode == PageItemsDisplayMode.ONE);
		if (flowOptions.showToC)
			setComboPageIndex(pageIndex+1);
		else
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
