package com.qtitools.player.client.controller.session;

import java.util.Date;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONValue;
import com.qtitools.player.client.controller.communication.Result;

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
		if (timeStarted != 0)
			return time + (int)((long) ((new Date()).getTime() * 0.001) - timeStarted);
		else if (time != 0)
			return time;
		else
			return 0;
	}
	
	public JSONValue getState(){
		JSONArray stateArr = new JSONArray();
		stateArr.set(0, new JSONNumber(result.getMinPoints()));
		stateArr.set(1, new JSONNumber(result.getMaxPoints()));
		stateArr.set(2, new JSONNumber(result.getScore()));
		stateArr.set(3, new JSONNumber(checks));
		stateArr.set(4, new JSONNumber(mistakes));
		stateArr.set(5, new JSONNumber(getActualTime()));
		stateArr.set(6, JSONBoolean.getInstance(visited));
		return stateArr;
	}
	
	public void setState(JSONValue value){
		JSONArray stateArr = (JSONArray)value;
		if (stateArr.size() == 7){
			result = new Result((float)stateArr.get(2).isNumber().doubleValue(), (float)stateArr.get(0).isNumber().doubleValue(), (float)stateArr.get(1).isNumber().doubleValue());
			checks = (int)stateArr.get(3).isNumber().doubleValue();
			mistakes = (int)stateArr.get(4).isNumber().doubleValue();
			time = (int)stateArr.get(5).isNumber().doubleValue();
			visited = stateArr.get(6).isBoolean().booleanValue();
		}
	}
}
