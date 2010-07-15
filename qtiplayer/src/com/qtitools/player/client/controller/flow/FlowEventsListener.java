package com.qtitools.player.client.controller.flow;

import com.qtitools.player.client.controller.communication.ActivityActionType;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;


public interface FlowEventsListener {

	public void onNavigatePageSwitched();
	
	public void onActivityAction(ActivityActionType action);
	
	public void onNavigationIncident(NavigationIncidentType incidentType);
}
