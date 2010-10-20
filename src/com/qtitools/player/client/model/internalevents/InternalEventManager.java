package com.qtitools.player.client.model.internalevents;

import java.util.Vector;
import com.qtitools.player.client.module.IBrowserEventHandler;

public class InternalEventManager {
	public InternalEventManager(){
		handlers = new Vector<InternalEventHandlerInfo>(0);
	}
	
	private Vector<InternalEventHandlerInfo> handlers;
	
	public void register(InternalEventHandlerInfo iehi){
		handlers.add(iehi);
	}
	
	public Vector<IBrowserEventHandler> getHandlers(String id, int e){
		
		Vector<IBrowserEventHandler> h = new Vector<IBrowserEventHandler>(0);
		
		for (InternalEventHandlerInfo hi : handlers){
			if (hi.match(id, e))
				h.add(hi.getModule());
		}
		return h;
	}
}
