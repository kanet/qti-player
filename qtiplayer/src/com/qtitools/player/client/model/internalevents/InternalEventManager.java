package com.qtitools.player.client.model.internalevents;

import java.util.Vector;

import com.qtitools.player.client.module.IInteractionModule;

public class InternalEventManager {
	public InternalEventManager(){
		handlers = new Vector<InternalEventHandlerInfo>(0);
	}
	
	private Vector<InternalEventHandlerInfo> handlers;
	
	public void register(InternalEventHandlerInfo iehi){
		handlers.add(iehi);
	}
	
	public Vector<IInteractionModule> getHandlers(String id, int e){
		
		Vector<IInteractionModule> h = new Vector<IInteractionModule>(0);
		
		for (InternalEventHandlerInfo hi : handlers){
			if (hi.match(id, e))
				h.add(hi.getModule());
		}
		return h;
	}
}
