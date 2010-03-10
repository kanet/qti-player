package com.qtitools.player.client.model.internalevents;

import com.qtitools.player.client.module.IInteractionModule;

public class InternalEventHandlerInfo extends InternalEventTrigger {

	public InternalEventHandlerInfo(IInteractionModule m , String id, int e){
		super(id, e);
		module = m;
	}
	public InternalEventHandlerInfo(IInteractionModule m , InternalEventTrigger t){
		super(t.getTagId(), t.getEventTypeInt());
		module = m;
	}
	
	protected IInteractionModule module;

	/**
	 * @return the module
	 */
	public IInteractionModule getModule() {
		return module;
	}
	
	
	public boolean match(String id, int e){
		return (eventTypeInt == e  &&  tagID.compareTo(id) == 0);
	}
	
}
