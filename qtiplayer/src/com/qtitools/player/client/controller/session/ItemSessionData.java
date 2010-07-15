package com.qtitools.player.client.controller.session;

import java.util.Date;

public class ItemSessionData extends ItemSessionResultAndStats {

	public ItemSessionData(){
		timeStarted = 0;
		time = 0;
		visited = false;
	}
	
	public long timeStarted;
	public int time;
	public boolean visited;
	
	public void begin(){
		timeStarted = (long) ((new Date()).getTime() * 0.001);
		visited = true;
	}
	
	public void end(){
		time += ((long) ((new Date()).getTime() * 0.001) - timeStarted);
		timeStarted = 0;
	}
	
	public int getActualTime(){
		if (time != 0)
			return time;
		else if (timeStarted != 0)
			return (int)((long) ((new Date()).getTime() * 0.001) - timeStarted);
		else
			return 0;
	}
}
