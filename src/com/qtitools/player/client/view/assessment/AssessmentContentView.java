package com.qtitools.player.client.view.assessment;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.qtitools.player.client.view.page.PageContentView;
import com.qtitools.player.client.view.page.PageViewSocket;

public class AssessmentContentView implements AssessmentViewSocket {

	public AssessmentContentView(Panel ap){
		assessmentPanel = ap;
		pagePanel = new FlowPanel();
		pagePanel.setStyleName("qp-body");
		headerPanel = new HorizontalPanel();
		headerPanel.setStyleName("qp-header");
		navigationPanel = new FlowPanel();
		navigationPanel.setStyleName("qp-footer");
		
		assessmentPanel.add(headerPanel);
		assessmentPanel.add(pagePanel);
		assessmentPanel.add(navigationPanel);
		
		pageContentView = new PageContentView(pagePanel);
		
	}
	
	private PageContentView pageContentView;
	
	private Panel assessmentPanel;
	private Panel headerPanel;
	private Panel pagePanel;
	private Panel navigationPanel;


	@Override
	public void setAssessmentViewCarrier(AssessmentViewCarrier a) {
		headerPanel.clear();
		headerPanel.add(a.getTitleView());
		headerPanel.add(a.getNavigationComboView());
		navigationPanel.clear();
		navigationPanel.add(a.getNavigationMenuView());
		
	}

	@Override
	public PageViewSocket getPageViewSocket() {
		return pageContentView;
	}


}
