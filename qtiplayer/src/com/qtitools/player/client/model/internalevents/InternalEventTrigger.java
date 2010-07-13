package com.qtitools.player.client.model.internalevents;

public class InternalEventTrigger {

	public InternalEventTrigger(String id, int e){
		tagID = id;
		eventTypeInt = e;
	}
	
	protected String tagID;
	protected int eventTypeInt;
	
	/**
	 * @return the module
	 */
	public String getTagId() {
		return tagID;
	}

	/**
	 * @return the module
	 */
	public int getEventTypeInt() {
		return eventTypeInt;
	}
	
}
