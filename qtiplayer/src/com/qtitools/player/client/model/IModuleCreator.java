package com.qtitools.player.client.model;

import com.google.gwt.dom.client.Element;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;

public interface IModuleCreator {

	public boolean isSupported(String name);
	
	public Element createModule(com.google.gwt.xml.client.Element element, IModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener);
}
