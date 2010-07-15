package com.qtitools.player.client.view.page;

import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.view.item.ItemViewSocket;

public interface PageViewSocket {

	public void initItemViewSockets(int count);
	
	public ItemViewSocket getItemViewSocket(int index);
	
	public void setPageViewCarrier(PageViewCarrier pvc);
}
