package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.ItemData;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
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
		item = new Item(data.data, this);
		itemIndex = data.itemIndex;
		item.setState(itemSessionSocket.getState(itemIndex));
		itemViewSocket.setItemView(new ItemViewCarrier(String.valueOf(itemIndex+1) + ". " + item.getTitle(), item.getContentView(), item.getFeedbackView(), item.getScoreView()));
		itemSessionSocket.beginItemSession(itemIndex);
		navigationIncidentsStats = new ItemNavigationIncidentsStats();
	}
	
	public void close(){
		item.close();
		itemSessionSocket.setState(itemIndex, item.getState());
		/*
		ItemSessionData isd = new ItemSessionData();
		itemSessionSocket.setSessionData(itemIndex, isd);
		*/
		itemSessionSocket.endItemSession(itemIndex);
		itemSessionSocket.setSessionResultAndStats(itemIndex, 
				new ItemSessionResultAndStats(item.getResult(), navigationIncidentsStats.getNavigationIncidentsCount(NavigationIncidentType.CHECK), item.getMistakesCount())
			);
		// albo zresetowaæ mistakes count
		// albo zrobiæ przekazywanie mistakes count tylko na zamkniêciu (close())
		// to zale¿y od tego kto bêdzie pokazywa³ item results
		// POBIERANIE ON CLOSE - STATS RESETUJ¥ SIÊ SAME
	}
	
	public void updateState(){
		if (item != null){
			item.setState(itemSessionSocket.getState(itemIndex));
		}
	}

	@Override
	public void onStateChanged(boolean processFeedback, IInteractionModule sender) {
		item.process(processFeedback, sender != null ? sender.getIdentifier() : "");
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
}
