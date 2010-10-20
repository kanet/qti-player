package com.qtitools.player.client.view.page;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.flow.navigation.NavigationCommandsListener;
import com.qtitools.player.client.util.localisation.LocalePublisher;
import com.qtitools.player.client.util.localisation.LocaleVariable;
import com.qtitools.player.client.view.item.ItemContentView;
import com.qtitools.player.client.view.item.ItemViewSocket;

public class PageContentView implements PageViewSocket {
	
	public PageContentView(Panel p){
		pagePanel = p;
		titlePanel = new FlowPanel();
		titlePanel.setStyleName("qp-page-title");
		itemsPanel = new FlowPanel();
		itemsPanel.setStyleName("qp-page-content");
		//pagePanel.add(titlePanel);  // removed page title
		pagePanel.add(itemsPanel);
	}

	private Panel pagePanel;
	private Panel itemsPanel;
	private Panel titlePanel;
	private Panel contentPanel;
	
	private Panel[] itemPanels;
	private ItemContentView[] items;
	
	@Override
	public ItemViewSocket getItemViewSocket(int index) {
		return items[index];
	}

	@Override
	public void initItemViewSockets(int count) {
		itemsPanel.clear();
		itemPanels = new Panel[count];
		items = new ItemContentView[count];
		
		for (int i = 0 ; i < count ; i ++){
			itemPanels[i] = new FlowPanel();
			itemPanels[i].setStyleName("qp-page-item");
			items[i] = new ItemContentView(itemPanels[i]);
			itemsPanel.add(itemPanels[i]);
		}

	}

	@Override
	public void setPageViewCarrier(PageViewCarrier pvc) {
		titlePanel.clear();
		titlePanel.add(pvc.getPageTitle());
		
		if (pvc.hasContent()){
			contentPanel = new FlowPanel();

			if (pvc.pageType == PageType.ERROR){

				contentPanel.setStyleName("qp-page-error");
				
				Label errorLabel =  new Label(pvc.errorMessage);
				errorLabel.setStyleName("qp-page-error-text");
				contentPanel.add(errorLabel);
				
			} else if (pvc.pageType == PageType.TOC){
				contentPanel.setStyleName("qp-toc");
				
				Label tocTitle = new Label(LocalePublisher.getText(LocaleVariable.TOC_TITLE));
				tocTitle.setStyleName("qp-toc-title");
				contentPanel.add(tocTitle);
				
				Panel titlesPanel = new VerticalPanel();
				titlesPanel.setStyleName("qp-toc-items");
				final NavigationCommandsListener naviListener = pvc.navigationCommandsListener;
				
				for (int t = 0 ; t < pvc.titles.length ; t ++){
					final int tt = t;
					Label titleLabel = new Label(LocalePublisher.getText(LocaleVariable.TOC_PAGE) + " " + String.valueOf(t+1) + LocalePublisher.getText(LocaleVariable.TOC_PAGE_DOT) + pvc.titles[t]);
					titleLabel.setStyleName("qp-toc-item-title");
					titleLabel.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							naviListener.gotoPage(tt);
						}
					});
					titleLabel.addMouseOverHandler(new MouseOverHandler() {
						public void onMouseOver(MouseOverEvent event) {
							((Label)event.getSource()).setStyleName("qp-toc-item-title-hover");
						}
					});
					titleLabel.addMouseOutHandler(new MouseOutHandler() {
						public void onMouseOut(MouseOutEvent event) {
							((Label)event.getSource()).setStyleName("qp-toc-item-title");
						}
					});
					titlesPanel.add(titleLabel);
				}
				
				contentPanel.add(titlesPanel);
				
			} else if (pvc.pageType == PageType.SUMMARY){
				Grid resultItemsInfo = new Grid(pvc.titles.length, 5);
			    resultItemsInfo.setStylePrimaryName("qp-resultpage-items");
				final NavigationCommandsListener naviListener = pvc.navigationCommandsListener;
				
				for (int t = 0 ; t < pvc.titles.length ; t ++){
					final int tt = t;
					String titleString = LocalePublisher.getText(LocaleVariable.SUMMARY_PAGE) + " " + String.valueOf(t+1) + ": " + pvc.titles[t];
					Label titleLabel = new Label(titleString);
					titleLabel.setStyleName("qp-toc-item-title");
					titleLabel.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							naviListener.previewPage(tt);
						}
					});
					titleLabel.addMouseOverHandler(new MouseOverHandler() {
						public void onMouseOver(MouseOverEvent event) {
							((Label)event.getSource()).setStyleName("qp-toc-item-title-hover");
						}
					});
					titleLabel.addMouseOutHandler(new MouseOutHandler() {
						public void onMouseOut(MouseOutEvent event) {
							((Label)event.getSource()).setStyleName("qp-toc-item-title");
						}
					});
			    	resultItemsInfo.setWidget(t, 0, titleLabel);
			    	
			    	String resultString = "";
			    	if (pvc.sessionData.visiteds[t]){
			    		if (pvc.sessionData.results[t].getMaxPoints() > 0)
			    			resultString = String.valueOf(pvc.sessionData.results[t].getScore()).replace(".0", "") + "/" + String.valueOf(pvc.sessionData.results[t].getMaxPoints()-pvc.sessionData.results[t].getMinPoints()).replace(".0", "");
			    		else
			    			resultString = LocalePublisher.getText(LocaleVariable.SUMMARY_NOTSCORED);
			    	} else {
			    		resultString = LocalePublisher.getText(LocaleVariable.SUMMARY_NOTVISITED);
			    	}
			    	resultItemsInfo.setText(t, 1, resultString);

					String timeFormatted = String.valueOf( pvc.sessionData.times[t]/60 ) + ":" + ((pvc.sessionData.times[t]%60 < 10)?"0":"") + String.valueOf( pvc.sessionData.times[t]%60 );
					
			    	resultItemsInfo.setText(t, 2, timeFormatted + LocalePublisher.getText(LocaleVariable.SUMMARY_STATS_TIME_SUFIX));
			    	
			    	if (pvc.sessionData.results[t].getMaxPoints() > 0){
			    		resultItemsInfo.setText(t, 3, String.valueOf(pvc.sessionData.checks[t]) + LocalePublisher.getText(LocaleVariable.SUMMARY_STATS_CHECKCOUNT_SUFIX));
			    		resultItemsInfo.setText(t, 4, String.valueOf(pvc.sessionData.mistakes[t]) + LocalePublisher.getText(LocaleVariable.SUMMARY_STATS_MISTAKES_SUFIX));
			    	} else {
			    		resultItemsInfo.setText(t, 3, LocalePublisher.getText(LocaleVariable.SUMMARY_STATS_CHECKCOUNT_NO));
			    		resultItemsInfo.setText(t, 4, LocalePublisher.getText(LocaleVariable.SUMMARY_STATS_MISTAKES_NO));
			    	}
		    	
				}

				contentPanel.add(resultItemsInfo);
				
	    	    contentPanel.add(pvc.assessmentFeedbackSocket.getFeedbackView((int)((pvc.sessionData.resultTotal.getScore() * 100)/pvc.sessionData.resultTotal.getMaxPoints())));
				
	    	    FlowPanel resultScorePanel = new FlowPanel();
	    	    resultScorePanel.setStyleName("qp-resultpage-score");
	    	    contentPanel.add(resultScorePanel);
	    	    
	    		Label resultScoreInfoPercent = new Label(LocalePublisher.getText(LocaleVariable.SUMMARY_INFO_YOURSCOREIS1) + 
	    				(int)((pvc.sessionData.resultTotal.getScore() * 100)/pvc.sessionData.resultTotal.getMaxPoints()) + 
	    	    		LocalePublisher.getText(LocaleVariable.SUMMARY_INFO_YOURSCOREIS2)); 
	    		resultScoreInfoPercent.setStylePrimaryName("qp-resultpage-percents");
	    		resultScorePanel.add(resultScoreInfoPercent);
	    	    
	    		Label resultScoreInfoPoints = new Label(pvc.sessionData.resultTotal.getScore() + 
	    	    		LocalePublisher.getText(LocaleVariable.SUMMARY_INFO_YOURSCOREIS3));
	    	    resultScoreInfoPoints.setStylePrimaryName("qp-resultpage-points");
	    	    resultScorePanel.add(resultScoreInfoPoints);
	    	    
	    	    String timeFormatted = String.valueOf( pvc.sessionData.timeTotal/60 ) + ":" + ((pvc.sessionData.timeTotal%60 < 10)?"0":"") + String.valueOf( pvc.sessionData.timeTotal%60 );
				
	    		Label resultScoreInfoTime = new Label(LocalePublisher.getText(LocaleVariable.SUMMARY_INFO_YOURSCOREIS4) + 
	    				timeFormatted + 
	    	    		LocalePublisher.getText(LocaleVariable.SUMMARY_INFO_YOURSCOREIS5));
	    	    resultScoreInfoTime.setStylePrimaryName("qp-resultpage-time");
	    	    resultScorePanel.add(resultScoreInfoTime);

	    	    contentPanel.setStyleName("qp-summary");
			}
			
			itemsPanel.clear();
			itemsPanel.add(contentPanel);
		}
	}

}
