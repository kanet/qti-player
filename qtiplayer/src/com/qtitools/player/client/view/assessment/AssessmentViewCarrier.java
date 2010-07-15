package com.qtitools.player.client.view.assessment;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.controller.flow.navigation.NavigationViewSocket;
import com.qtitools.player.client.model.Assessment;

public class AssessmentViewCarrier {

	public AssessmentViewCarrier(Assessment a, NavigationViewSocket nvs){
		navigationViewSocket = nvs;
		assessmentTitle = a.getTitle();
	}
	
	private NavigationViewSocket navigationViewSocket;
	private String assessmentTitle; 
	
	public Widget getNavigationMenuView(){
		return navigationViewSocket.getMenuView(); 
	}

	public Widget getNavigationComboView(){
		return navigationViewSocket.getComboView(); 
	}

	public Widget getTitleView(){
		return new Label(assessmentTitle);
	}
}
