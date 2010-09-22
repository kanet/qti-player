package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.DisplayContentOptions;
import com.qtitools.player.client.controller.communication.ItemData;
import com.qtitools.player.client.controller.communication.ItemParameters;
import com.qtitools.player.client.controller.communication.ItemParametersSocket;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.flow.navigation.NavigationSocket;
import com.qtitools.player.client.controller.log.OperationLogEvent;
import com.qtitools.player.client.controller.log.OperationLogManager;
import com.qtitools.player.client.controller.session.ItemSessionResultAndStats;
import com.qtitools.player.client.controller.session.ItemSessionSocket;
import com.qtitools.player.client.model.Item;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.style.StyleSocket;
import com.qtitools.player.client.view.item.ItemViewCarrier;
import com.qtitools.player.client.view.item.ItemViewSocket;

public class ItemController implements ModuleStateChangedEventsListener {

	public ItemController(ItemViewSocket ivs, NavigationSocket ns, ItemSessionSocket iss){
		itemViewSocket = ivs;
		navigationSocket = ns;
		itemSessionSocket = iss;
	}
	
	private Item item;
	
	private int itemIndex;
	
	private ItemViewSocket itemViewSocket;
	private ItemSessionSocket itemSessionSocket;
	private NavigationSocket navigationSocket;

	private StyleSocket styleSocket;
	public void setStyleSocket( StyleSocket ss) {
		styleSocket = ss;
	}
	
	private ItemNavigationIncidentsStats navigationIncidentsStats;
	
	public void init(ItemData data, DisplayContentOptions options){
		try {
			if (data.data == null)
				throw new Exception("Item data is null");
			item = new Item(data.data, options, this, styleSocket);
			itemIndex = data.itemIndex;
			item.setState(itemSessionSocket.getState(itemIndex));
			itemViewSocket.setItemView(new ItemViewCarrier(String.valueOf(itemIndex+1) + ". " + item.getTitle(), item.getContentView(), item.getFeedbackView(), item.getScoreView()));
			itemSessionSocket.beginItemSession(itemIndex);
			navigationIncidentsStats = new ItemNavigationIncidentsStats();
			navigationSocket.getNavigationViewSocket().setItemParamtersSocket(new ItemParametersSocket() {
				public ItemParameters getItemParameters() {
					return new ItemParameters(item.getModulesCount());
				}
			});
		} catch (Exception e) {
			item = null;
			itemViewSocket.setItemView(new ItemViewCarrier(data.errorMessage.length() > 0 ? data.errorMessage : e.getMessage()));
			OperationLogManager.logEvent(OperationLogEvent.DISPLAY_ITEM_FAILED);
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
			} else if (nit == NavigationIncidentType.SHOW_ANSWERS){
				item.showAnswers(true);
			} else if (nit == NavigationIncidentType.HIDE_ANSWERS){
				item.showAnswers(false);
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
