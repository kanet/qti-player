package com.qtitools.player.client.style;

import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.controller.communication.PageReference;

public interface StyleSocket {
	public Map<String,String> getStyles( Element element );
	public void setCurrentPages( PageReference pr );
}
