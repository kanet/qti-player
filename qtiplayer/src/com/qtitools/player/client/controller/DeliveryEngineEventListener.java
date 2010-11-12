package com.qtitools.player.client.controller;

public interface DeliveryEngineEventListener {
	
	public void onAssessmentLoaded();
	public void onAssessmentStarted();
	public void onSummary();
	public void onTestPageSwitching();
	public void onTestPageSwitched();
}
