package com.qtitools.player.client.controller.data.events;

public interface DataLoaderEventListener {

	public void onAssessmentLoaded();
	public void onAssessmentLoadingError();
	public void onDataReady();
}
