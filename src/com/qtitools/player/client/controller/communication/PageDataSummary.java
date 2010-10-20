package com.qtitools.player.client.controller.communication;

import com.qtitools.player.client.controller.session.SessionDataCarrier;
import com.qtitools.player.client.model.feedback.AssessmentFeedbackSocket;

public class PageDataSummary extends PageData {

	public PageDataSummary(String[] ts) {
		super(PageType.SUMMARY);
		titles = ts;
	}
	
	public String[] titles;
	public SessionDataCarrier sessionData;
	private AssessmentFeedbackSocket assessmentFeedbackSocket;

	public void setSessionData(SessionDataCarrier sdc){
		sessionData = sdc;
	}
	
	public void setAssessmentFeedbackSocket(AssessmentFeedbackSocket afs){
		assessmentFeedbackSocket = afs;
	}
	
	public AssessmentFeedbackSocket getAssessmentFeedbackSocket(){
		return assessmentFeedbackSocket;
	}
}
