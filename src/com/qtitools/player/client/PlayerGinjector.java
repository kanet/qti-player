package com.qtitools.player.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.qtitools.player.client.controller.DeliveryEngine;
import com.qtitools.player.client.view.ViewEngine;

@GinModules(PlayerGinModule.class)
public interface PlayerGinjector extends Ginjector {
	ViewEngine getViewEngine();
	DeliveryEngine getDeliveryEngine();
}
