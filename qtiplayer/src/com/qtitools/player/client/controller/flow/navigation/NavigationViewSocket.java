package com.qtitools.player.client.controller.flow.navigation;

import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.controller.communication.ItemParametersSocket;

public interface NavigationViewSocket {

	public Widget getMenuView();
	public Widget getComboView();
	public void setItemParamtersSocket(ItemParametersSocket ips);
}
