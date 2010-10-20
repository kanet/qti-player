package com.qtitools.player.client;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class FlowEventsJSCallbackCaller {

    
	public static native void onAssessmentSessionBeginJS(JavaScriptObject player) /*-{
		if(typeof player.onAssessmentSessionBegin == 'function') {
			player.onAssessmentSessionBegin();
	}
	}-*/;
	
	public static native void onAssessmentSessionFinishedJS(JavaScriptObject player) /*-{
	  	if(typeof player.onAssessmentSessionFinished == 'function') {
			player.onAssessmentSessionFinished();
		}
	}-*/;
	
	public static native void onItemSessionBeginJS(JavaScriptObject player) /*-{
		if(typeof player.onItemSessionBegin == 'function') {
			player.onItemSessionBegin();
		}
	}-*/;
	
	public static native void onItemSessionFinishedJS(JavaScriptObject player) /*-{
		if(typeof player.onItemSessionFinished == 'function') {
			player.onItemSessionFinished();
		}
	}-*/;
}
