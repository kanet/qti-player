package com.qtitools.player.client.view;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.qtitools.player.client.view.player.PlayerContentView;
import com.qtitools.player.client.view.player.PlayerViewSocket;

public class ViewEngine {
	
	public ViewEngine(String id){
		root = RootPanel.get(id);
		mountView(root);
	}
	
	public ViewEngine(ComplexPanel container){
		mountView(container);
	}

	public void mountView(ComplexPanel container){
		
		playerView = new PlayerContentView();
		root.add(playerView.getView());
		
	}
	
	private RootPanel root;
	private PlayerContentView playerView;
	
	public PlayerViewSocket getPlayerViewSocket(){
		return playerView;
	}
	
	
	
}
