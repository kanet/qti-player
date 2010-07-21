package com.qtitools.player.client.controller.session;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.qtitools.player.client.controller.communication.Result;
import com.qtitools.player.client.controller.session.events.StateChangedEventsListener;

public class SessionDataManager implements SessionSocket, StateInterface {
	
	public SessionDataManager(StateChangedEventsListener l){
		listener = l;
	}
	
	public void init(int itemsCount){
		if (state == null) {
			state = new JSONArray();
			for (int i = 0 ; i < itemsCount ; i ++)
				state.set(i, new JSONArray());
		}
		itemSessionDatas = new ItemSessionData[itemsCount];
	}

	private JSONArray state;
	private ItemSessionData[] itemSessionDatas;
	private StateChangedEventsListener listener;
	
	@Override
	public ItemSessionSocket getItemSessionSocket() {
		return this;
	}

	@Override
	public PageSessionSocket getPageSessionSocket() {
		return this;
	}


	@Override
	public JSONArray getState(int itemIndex) {
		if (state.get(itemIndex) != null)
			return state.get(itemIndex).isArray();
		else
			return new JSONArray();
	}

	@Override
	public void setState(int itemIndex, JSONArray st) {
		state.set(itemIndex, st);
		
	}

	@Override
	public ItemSessionData getSessionData(int itemIndex) {
		return itemSessionDatas[itemIndex];
	}
	
	@Override
	public void setSessionResultAndStats(int itemIndex, ItemSessionResultAndStats isr) {
		itemSessionDatas[itemIndex].update(isr);
	}

	@Override
	public void setSessionResult(int itemIndex, Result result) {
		if (itemSessionDatas[itemIndex] != null)
			itemSessionDatas[itemIndex].update(result);
	}
	@Override
	public void importState(String st) {
		  try {
			  JSONArray statesArr = (JSONArray)JSONParser.parse(st);
			  state = statesArr;
			  listener.onStateChanged();
		  } catch (Exception e) {
		}
		
	}

	@Override
	public String exportState() {
		return state.toString();
	}
	
	public SessionDataCarrier getSessionData(){
		SessionDataCarrier sd = new SessionDataCarrier();
		
		sd.results = new Result[itemSessionDatas.length];
		sd.times = new int[itemSessionDatas.length];
		sd.checks = new int[itemSessionDatas.length];
		sd.mistakes = new int[itemSessionDatas.length];
		for (int i = 0 ; i < itemSessionDatas.length ; i ++){
			if (itemSessionDatas[i] != null){
				sd.results[i] = itemSessionDatas[i].result;
				sd.times[i] = itemSessionDatas[i].getActualTime();
				sd.checks[i] = itemSessionDatas[i].checks;
				sd.mistakes[i] = itemSessionDatas[i].mistakes;
			} else {
				sd.results[i] = new Result();
				sd.times[i] = 0;
				sd.checks[i] = 0;
				sd.mistakes[i] = 0;
			}
		}
		sd.resultTotal = getAssessmentTotalResult();
		sd.timeTotal = getAssessmentTotalTime();
		
		return sd;
	}

	@Override
	public void beginItemSession(int itemIndex) {
		if (itemSessionDatas[itemIndex] == null)
			itemSessionDatas[itemIndex] = new ItemSessionData();
		itemSessionDatas[itemIndex].begin();
		
	}

	@Override
	public void endItemSession(int itemIndex) {
		itemSessionDatas[itemIndex].end();
		
	}

	public int getItemsVisitedCount(){
		int count = 0;
		if (itemSessionDatas != null){
			for (int i = 0 ; i < itemSessionDatas.length ; i ++){
				if (itemSessionDatas[i] != null)
					if (itemSessionDatas[i].visited)
						count++;
			}
		}
		return count;
			
	}
	
	public int getAssessmentTotalTime(){
		if (itemSessionDatas == null)
			return 0;
		
		int count = 0;
		
		for (int i = 0 ; i < itemSessionDatas.length ; i ++){
			if (itemSessionDatas[i] != null){
				count += itemSessionDatas[i].getActualTime();
			}
		}
		
		return count;
	}
	
	public Result getAssessmentTotalResult(){
		if (itemSessionDatas == null)
			return new Result();
		
		Result r = new Result(0, 0, 0);
		
		for (int i = 0 ; i < itemSessionDatas.length ; i ++){
			if (itemSessionDatas[i] != null)
				r.merge(itemSessionDatas[i].result);
			else
				r.merge(new Result());
		}
		
		return r;
		
	}

}
