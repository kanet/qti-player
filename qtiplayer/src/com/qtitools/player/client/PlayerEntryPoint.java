/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */
package com.qtitools.player.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlayerEntryPoint implements EntryPoint {

	/** Player object */
	public static Player player;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Define js API
		initJavaScriptAPI();
	}

	/**
	 * Init Javascript API
	 */
	private static native void initJavaScriptAPI() /*-{
		// CreatePlayer
		$wnd.qpCreatePlayer = function(id) {
		  var player = @com.qtitools.player.client.PlayerEntryPoint::createPlayer(Ljava/lang/String;)(id);
		  player.load = function(url){
		    @com.qtitools.player.client.PlayerEntryPoint::load(Ljava/lang/String;)(url);
		  }
		  player.setMasteryScore = function(mastery){
		    @com.qtitools.player.client.PlayerEntryPoint::setMasteryScore(I)(mastery);
		  }

		  player.getResult = function(){
		    return @com.qtitools.player.client.PlayerEntryPoint::getResult()();
		  }

		  player.getAssessmentSessionReport = function(){
		    return @com.qtitools.player.client.PlayerEntryPoint::getAssessmentSessionReport()();
		  }

		  player.getState = function(){
		    return "";
		  }
		  player.getStateString = function(){
		    return @com.qtitools.player.client.PlayerEntryPoint::getState()();
		  }

		  player.setState = function(obj){
		  }
		  player.setStateString = function(obj){
		    @com.qtitools.player.client.PlayerEntryPoint::setState(Ljava/lang/String;)(obj);
		  }

		  player.navigateNextItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateNextItem()();
		  }

		  player.navigatePreviousItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigatePreviousItem()();
		  }

		  player.navigateFinishItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateFinishItem()();
		  }

		  player.navigateFinishAssessment = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateFinishAssessment()();
		  }
		  
		  player.navigateSummaryAssessment = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateSummaryAssessment()();
		  }

		  player.navigateResetItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateResetItem()();
		  }

		  player.navigateCheckItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateCheckItem()();
		  }
		  player.navigateContinueItem = function(){
		  	 @com.qtitools.player.client.PlayerEntryPoint::navigateContinueItem()();
		  }

		  player.getEngineMode = function(){
		  	 return @com.qtitools.player.client.PlayerEntryPoint::getEngineMode()();
		  }
		  
		  player.setFlowOptions = function(obj){
		  	@com.qtitools.player.client.PlayerEntryPoint::setFlowOptions(Lcom/google/gwt/core/client/JavaScriptObject;)(obj);
		  }

		  //player.addStyle = function(){
		  //	 @com.qtitools.player.client.PlayerEntryPoint::addStyle()();
		  //}


		  return player;
		}

		// Call App loaded function
		if(typeof $wnd.qpOnAppLoaded == 'function') {
		  $wnd.qpOnAppLoaded();	
		}
	}-*/;

	/**
	 * createPlayer js interface
	 * 
	 * @param node_id
	 *            wrap this node
	 */
	public static JavaScriptObject createPlayer(String node_id) {
		player = new Player(node_id);
		return player.getJavaScriptObject();
	}

	/**
	 * Returns array with result info
	 * 
	 * @param url
	 */
	public static JavaScriptObject getResult() {
		return player.getResult().getJSObject();
	}

	/**
	 * Returns array with result info
	 * 
	 * @param url
	 */
	public static JavaScriptObject getAssessmentSessionReport() {
		return player.getAssessmentSessionReport();
	}

	/**
	 * Returns array with result info
	 * 
	 * @param url
	 */
	public static String getState() {
		return player.getState();
	}

	/**
	 * Returns array with result info
	 * 
	 * @param url
	 */
	public static void setState(String obj) {
		player.setState(obj);
	}

	public static void setMasteryScore(int mastery) {
		player.setMasteryScore(mastery);
	}

	public static JavaScriptObject getEngineMode() {
		return player.getEngineMode();
	}

	public static void navigateNextItem() {
		player.getNavigationCommandsListener().nextPage();
	}

	public static void navigatePreviousItem() {
		player.getNavigationCommandsListener().previousPage();
	}

	public static void navigateFinishItem() {
		// compatibility issue
		player.getNavigationCommandsListener().checkPage();
	}

	public static void navigateFinishAssessment() {
		player.getNavigationCommandsListener().gotoSummary();
	}

	public static void navigateSummaryAssessment() {
		player.getNavigationCommandsListener().gotoSummary();
	}

	public static void navigateResetItem() {
		player.getNavigationCommandsListener().resetPage();
	}
	
	public static void navigateContinueItem() {
		player.getNavigationCommandsListener().continuePage();
	}
	
	public static void navigateCheckItem() {
		player.getNavigationCommandsListener().checkPage();
	}

	public static void navigateResetAssessment() {
		
	}

	/**
	 * Load assessment from this url
	 * 
	 * @param url
	 */
	public static void load(String url) {
		player.load(url);
	}
	
	public static void setFlowOptions(JavaScriptObject o){
		FlowOptions fo = new FlowOptions();
		try {
			fo.showToC = decodeFlowOptionsObjectShowToC(o);
			fo.showSummary = decodeFlowOptionsObjectShowSummary(o);
			fo.itemsDisplayMode = (decodeFlowOptionsObjectPageItemsDisplayMode(o).compareTo(PageItemsDisplayMode.ALL.toString()) == 0) ? PageItemsDisplayMode.ALL : PageItemsDisplayMode.ONE;
		} catch (Exception e) {}
		player.setFlowOptions(fo);
	}
	
	public native static boolean decodeFlowOptionsObjectShowToC(JavaScriptObject obj)/*-{
		return obj.showToC;
	}-*/;
	
	public native static boolean decodeFlowOptionsObjectShowSummary(JavaScriptObject obj)/*-{
		return obj.showSummary;
	}-*/;

	public native static String decodeFlowOptionsObjectPageItemsDisplayMode(JavaScriptObject obj)/*-{
		return obj.itemsDisplayMode;
	}-*/;

}
