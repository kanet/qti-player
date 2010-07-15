package com.qtitools.player.client.controller.session;

import com.qtitools.player.client.controller.communication.Result;

public class SessionDataCarrier {
	
	public SessionDataCarrier(){
		
	}

	public Result[] results;
	public int[] times;
	public int[] checks;
	public int[] mistakes;
	public Result resultTotal;
	public int timeTotal;
}
