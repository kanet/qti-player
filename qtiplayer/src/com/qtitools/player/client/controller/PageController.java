package com.qtitools.player.client.controller;

import com.qtitools.player.client.controller.communication.PageData;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageDataTest;
import com.qtitools.player.client.controller.communication.PageDataToC;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.flow.navigation.NavigationIncidentType;
import com.qtitools.player.client.controller.flow.navigation.NavigationSocket;
import com.qtitools.player.client.controller.session.PageSessionSocket;
import com.qtitools.player.client.model.Page;
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
	
	public void initPage(PageData pageData){
		
		if (items != null){
			for (int i = 0 ; i < items.length ; i ++){
				items[i].close();
			}
		}
		
		// conception compatibility issue
		page = new Page();
		
		if (pageData.type == PageType.TEST ){
			
			PageDataTest pageDataTest = (PageDataTest)pageData;
			
			items = new ItemController[pageDataTest.datas.length];
			pageViewSocket.initItemViewSockets(pageDataTest.datas.length);
			
			pageViewSocket.setPageViewCarrier(new PageViewCarrier());
	
			for (int i = 0 ; i < pageDataTest.datas.length ; i ++){
				items[i] = new ItemController(pageViewSocket.getItemViewSocket(i), pageSessionSocket.getItemSessionSocket());
				
			}
			for (int i = 0 ; i < pageDataTest.datas.length ; i ++){
				items[i].init(pageDataTest.datas[i]);
				
			}
		} else if (pageData.type == PageType.TOC){
			pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataToC)pageData, navigationSocket));	
		} else if (pageData.type == PageType.SUMMARY){
			pageViewSocket.setPageViewCarrier(new PageViewCarrier((PageDataSummary)pageData, navigationSocket));	
		} else {
			pageViewSocket.setPageViewCarrier(new PageViewCarrier());	
		}
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
}
