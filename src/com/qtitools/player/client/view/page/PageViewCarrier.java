package com.qtitools.player.client.view.page;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.controller.communication.PageDataError;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageDataToC;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.flow.navigation.NavigationCommandsListener;
import com.qtitools.player.client.controller.session.SessionDataCarrier;
import com.qtitools.player.client.model.feedback.AssessmentFeedbackSocket;

/**
 * 
 * Contains all data that is required to create Page View
 * 
 * @author Rafal Rybacki
 *
 */
public class PageViewCarrier {

	public PageViewCarrier(){
		pageType = PageType.TEST;
	}

	public PageViewCarrier(PageDataToC p, NavigationCommandsListener ncl){
		pageType = PageType.TOC;
		titles = p.titles;
		navigationCommandsListener = ncl;
	}

	public PageViewCarrier(PageDataSummary p, NavigationCommandsListener ncl){
		pageType = PageType.SUMMARY;
		titles = p.titles;
		sessionData = p.sessionData;
		navigationCommandsListener = ncl;
		assessmentFeedbackSocket = p.getAssessmentFeedbackSocket();
	}

	public PageViewCarrier(PageDataError p){
		pageType = PageType.ERROR;
		errorMessage = p.errorMessage;
	}
	public PageType pageType;
		
	public String[] titles;
	public SessionDataCarrier sessionData;
	public String errorMessage;
	
	public NavigationCommandsListener navigationCommandsListener;
	public AssessmentFeedbackSocket assessmentFeedbackSocket;
	
	public Widget getPageTitle(){
		return new Label("");
	}
	
	public boolean hasContent(){
		return pageType != PageType.TEST;
	}
	
	public Widget getPageContent(){
		return null;
	}
}
