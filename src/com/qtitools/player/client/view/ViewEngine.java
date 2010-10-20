package com.qtitools.player.client.view;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.inject.Inject;
import com.qtitools.player.client.view.player.PlayerContentView;
import com.qtitools.player.client.view.player.PlayerViewSocket;

public class ViewEngine {
	
	@Inject
	public ViewEngine(PlayerContentView pcv){
		playerView = pcv;
	}
	
	public void mountView(ComplexPanel container){
		container.add(playerView.getView());
	}
	
	private PlayerContentView playerView;
	
	public PlayerViewSocket getPlayerViewSocket(){
		return playerView;
	}
	
	
	
}
