package com.qtitools.player.client.controller.flow.navigation;

import com.qtitools.player.client.controller.communication.ItemParametersSocket;

public interface NavigationSocket extends NavigationCommandsListener {

	public NavigationViewSocket getNavigationViewSocket();
	public void setItemParamtersSocket(ItemParametersSocket ips);
}
