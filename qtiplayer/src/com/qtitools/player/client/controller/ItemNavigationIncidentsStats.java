package com.qtitools.player.client.controller;

import java.util.HashMap;

import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;

public class ItemNavigationIncidentsStats {

	public ItemNavigationIncidentsStats(){
		incidentCounts = new HashMap<NavigationIncidentType, Integer>();
	}
	
	private HashMap<NavigationIncidentType, Integer> incidentCounts; 
	
	public void addNavigiationIncident(NavigationIncidentType nit){
		if (!incidentCounts.containsKey(nit))
			incidentCounts.put(nit, 1);
		else
			incidentCounts.put(nit, incidentCounts.get(nit) + 1);
	}
	
	public int getNavigationIncidentsCount(NavigationIncidentType nit){
		if (!incidentCounts.containsKey(nit))
			return 0;
		else
			return incidentCounts.get(nit);
	}
}
