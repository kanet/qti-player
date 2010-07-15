package com.qtitools.player.client.controller.communication;

public class ActivityAction {
	
	public ActivityAction(){
		actionType = ActivityActionType.NONE;
		enable = false;
	}

	public ActivityActionType actionType;
	public boolean enable;
}
