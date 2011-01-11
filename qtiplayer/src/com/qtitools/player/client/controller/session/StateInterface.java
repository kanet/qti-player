package com.qtitools.player.client.controller.session;

import com.google.gwt.json.client.JSONArray;

public interface StateInterface {

	public void importState(JSONArray state);
	public JSONArray exportState();
}
