package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.PageData;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.flow.navigation.NavigationSocket;
import com.qtitools.player.client.controller.flow.navigation.NavigationViewSocket;
import com.qtitools.player.client.controller.session.AssessmentSessionSocket;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.util.xml.document.XMLData;
import com.qtitools.player.client.view.assessment.AssessmentViewCarrier;
import com.qtitools.player.client.view.assessment.AssessmentViewSocket;


public class AssessmentController {

	public AssessmentController(AssessmentViewSocket avs, NavigationSocket ns, AssessmentSessionSocket ass){
		assessmentViewSocket = avs;
		navigationViewSocket = ns.getNavigationViewSocket();
		pageController = new PageController(avs.getPageViewSocket(), ns, ass.getPageSessionSocket());
		assessmentSessionSocket = ass;
	}
	
	private AssessmentViewSocket assessmentViewSocket;
	private NavigationViewSocket navigationViewSocket;
	
	@SuppressWarnings("unused")
	private AssessmentSessionSocket assessmentSessionSocket;
	
	private Assessment assessment;
	private PageController pageController;
	
	public void init(XMLData data){
		if (data != null){
			assessment = new Assessment(data);
			assessmentViewSocket.setAssessmentViewCarrier(new AssessmentViewCarrier(assessment, navigationViewSocket));
		}
	}
	
	public void initPage(PageData pageData){
		if (pageData.type == PageType.SUMMARY)
			((PageDataSummary)pageData).setAssessmentFeedbackSocket(assessment);
		pageController.initPage(pageData);
	}
	
	public void updateState(){
		pageController.updateState();
	}
	
	public void onNavigationIncident(NavigationIncidentType nit){
		if (pageController != null)
			pageController.onNavigationIncident(nit);
	}
	
}
