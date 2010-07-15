package com.qtitools.player.client.view.player;

import com.qtitools.player.client.view.assessment.AssessmentViewSocket;

public interface PlayerViewSocket {
	
	public void setPlayerViewCarrier(PlayerViewCarrier pvd);
	
	public AssessmentViewSocket getAssessmentViewSocket(); 
}
