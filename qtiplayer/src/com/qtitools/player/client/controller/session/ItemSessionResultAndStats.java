package com.qtitools.player.client.controller.session;

import com.qtitools.player.client.controller.communication.Result;

public class ItemSessionResultAndStats {

	public ItemSessionResultAndStats(){
		result = new Result();
		this.checks = 0;
		this.mistakes = 0;
	}
	

	public ItemSessionResultAndStats(Result r, int checks, int mistakes){
		result = r;
		this.checks = checks;
		this.mistakes = mistakes;
	}
	
	public Result result;
	public int checks;
	public int mistakes;

	public void update(ItemSessionResultAndStats isr){
		result = isr.result;
		checks += isr.checks;
		mistakes += isr.mistakes;
	}

	public void update(Result r){
		result = r;
	}
}
