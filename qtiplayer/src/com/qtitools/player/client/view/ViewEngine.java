package com.qtitools.player.client.view;

import com.google.gwt.user.client.ui.RootPanel;
import com.qtitools.player.client.view.player.PlayerContentView;
import com.qtitools.player.client.view.player.PlayerViewSocket;

public class ViewEngine {
	
	public ViewEngine(String id){
		mountView(id);
	}

	public void mountView(String id){
		root = RootPanel.get(id);
		
		playerView = new PlayerContentView();
		root.add(playerView.getView());
		
	}
	
	private RootPanel root;
	private PlayerContentView playerView;
	
	public PlayerViewSocket getPlayerViewSocket(){
		return playerView;
	}
	
	
	
}
