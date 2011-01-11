package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.PageData;
import com.qtitools.player.client.controller.communication.PageDataError;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageDataTest;
import com.qtitools.player.client.controller.communication.PageDataToC;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.flow.navigation.NavigationSocket;
import com.qtitools.player.client.controller.log.OperationLogEvent;
import com.qtitools.player.client.controller.log.OperationLogManager;
import com.qtitools.player.client.controller.session.PageSessionSocket;
import com.qtitools.player.client.model.ItemVariablesAccessor;
import com.qtitools.player.client.model.Page;
import com.qtitools.player.client.style.StyleSocket;
import com.qtitools.player.client.view.page.PageViewCarrier;
import com.qtitools.player.client.view.page.PageViewSocket;


public final class PageController {
	
	public PageController(PageViewSocket pvs, NavigationSocket ns, PageSessionSocket pss){
		pageViewSocket = pvs;
		navigationSocket = ns;
		pageSessionSocket = pss;
	}

	@SuppressWarnings("unused")
	private Page page;
	private PageViewSocket pageViewSocket;
	private PageSessionSocket pageSessionSocket;
	private NavigationSocket navigationSocket;
	private ItemController[] items;
	
	private StyleSocket styleSocket;
	public void setStyleSocket( StyleSocket ss) {
		styleSocket = ss;
	}
	
	public void initPage(PageData pageData){
		
		// conception compatibility issue
		page = new Page();

		if (pageData.type == PageType.ERROR ){
			
			pageViewSocket.setPageViewCarrier( new PageViewCarrier((PageDataError)pageData) );
			
			OperationLogManager.logEvent(OperationLogEvent.DISPLAY_PAGE_FAILED);
			
		} else if (pageData.type == PageType.TEST ){
			
			PageDataTest pageDataTest = (PageDataTest)pageData;
			
			items = new ItemController[pageDataTest.datas.length];
			pageViewSocket.initItemViewSockets(pageDataTest.datas.length);
			
			pageViewSocket.setPageViewCarrier(new PageViewCarrier());
	
			for (int i = 0 ; i < pageDataTest.datas.length ; i ++){
				ItemController controller = new ItemController(pageViewSocket.getItemViewSocket(i), navigationSocket, pageSessionSocket.getItemSessionSocket());
				controller.setStyleSocket( styleSocket );
				controller.init(pageDataTest.datas[i], pageDataTest.displayOptions);
				if (pageDataTest.displayOptions.isPreviewMode()){
					controller.setPreviewMode();
				}
				items[i] = controller;
			}
			
		} else if (pageData.type == PageType.TOC){
			items = null;
			pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataToC)pageData, navigationSocket));
		} else if (pageData.type == PageType.SUMMARY){
			items = null;
			pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataSummary)pageData, navigationSocket));
		}
	}
	
	public void close(){
		
		if (items != null){
			for (int i = 0 ; i < items.length ; i ++){
				items[i].close();
			}
		}
	}
	
	public void reset(){
		items = null;
	}
	
	public void updateState(){
		if (items != null){
			for (int i = 0 ; i < items.length ; i ++){
				items[i].updateState();
			}
		}
	}

	public void onNavigationIncident(NavigationIncidentType nit){
		if (items != null){
			for (int i = 0 ; i < items.length ; i ++){
				items[i].onNavigationIncident(nit);
			}
		}
	}

	public ItemVariablesAccessor getItemVariablesAccessor(){
		if (items != null  &&  items.length > 0)
			return items[0].getItemVariablesAccessor();
			
		return null;
	}
}
