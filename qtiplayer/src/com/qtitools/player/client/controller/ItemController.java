package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.ItemData;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.log.OperationLogEvent;
import com.qtitools.player.client.controller.log.OperationLogManager;
import com.qtitools.player.client.controller.session.ItemSessionResultAndStats;
import com.qtitools.player.client.controller.session.ItemSessionSocket;
import com.qtitools.player.client.model.Item;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.view.item.ItemViewCarrier;
import com.qtitools.player.client.view.item.ItemViewSocket;

public class ItemController implements ModuleStateChangedEventsListener {

	public ItemController(ItemViewSocket ivs, ItemSessionSocket iss){
		itemViewSocket = ivs;
		itemSessionSocket = iss;
	}
	
	private Item item;
	
	private int itemIndex;
	
	private ItemViewSocket itemViewSocket;
	private ItemSessionSocket itemSessionSocket;
	
	private ItemNavigationIncidentsStats navigationIncidentsStats;
	
	public void init(ItemData data){
		boolean success = false;
		try {
			if (data.data != null){
				item = new Item(data.data, this);
				itemIndex = data.itemIndex;
				item.setState(itemSessionSocket.getState(itemIndex));
				itemViewSocket.setItemView(new ItemViewCarrier(String.valueOf(itemIndex+1) + ". " + item.getTitle(), item.getContentView(), item.getFeedbackView(), item.getScoreView()));
				itemSessionSocket.beginItemSession(itemIndex);
				navigationIncidentsStats = new ItemNavigationIncidentsStats();
				success = true;
			}
		} finally {
			if (!success){
				item = null;
				itemViewSocket.setItemView(new ItemViewCarrier(data.errorMessage));
				OperationLogManager.logEvent(OperationLogEvent.DISPLAY_ITEM_FAILED);
			}
		}
		
	}
	
	public void close(){
		if (item != null){
			item.close();
			itemSessionSocket.setState(itemIndex, item.getState());
			itemSessionSocket.endItemSession(itemIndex);
			itemSessionSocket.setSessionResultAndStats(itemIndex, 
					new ItemSessionResultAndStats(item.getResult(), navigationIncidentsStats.getNavigationIncidentsCount(NavigationIncidentType.CHECK), item.getMistakesCount())
				);
		}

	}
	
	public void updateState(){
		if (item != null){
			item.setState(itemSessionSocket.getState(itemIndex));
		}
	}

	@Override
	public void onStateChanged(boolean userInteract, IInteractionModule sender) {
		item.process(userInteract, sender != null ? sender.getIdentifier() : "");
		itemSessionSocket.setSessionResult(itemIndex, item.getResult());
		itemSessionSocket.setState(itemIndex, item.getState());
	}
	
	public void onNavigationIncident(NavigationIncidentType nit){
		if (item != null){
			if (nit == NavigationIncidentType.CHECK){
				item.checkItem();
			} else if (nit == NavigationIncidentType.CONTINUE){
				item.continueItem();
			} else if (nit == NavigationIncidentType.RESET){
				item.resetItem();
			}
			navigationIncidentsStats.addNavigiationIncident(nit);
		}
	}
	
	public void setPreviewMode(){
		if(item != null)
			item.checkItem();
	}
}
