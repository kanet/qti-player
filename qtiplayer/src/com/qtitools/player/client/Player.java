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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.qtitools.player.client.controller.DeliveryEngine;
import com.qtitools.player.client.controller.DeliveryEngineEventListener;
import com.qtitools.player.client.controller.communication.DisplayOptions;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.IAssessmentReport;
import com.qtitools.player.client.controller.flow.navigation.NavigationCommandsListener;
import com.qtitools.player.client.model.ItemVariablesAccessor;
import com.qtitools.player.client.util.js.JSArrayUtils;
import com.qtitools.player.client.util.xml.document.XMLData;
import com.qtitools.player.client.view.ViewEngine;
/**
 * Main class with player API
 * @author Krzysztof Langner
 */
public class Player implements DeliveryEngineEventListener {

  /** JavaScript object representing this java object */
  private JavaScriptObject	jsObject;
  
  /** Delivery engine do manage the assessment content */
  public DeliveryEngine deliveryEngine;
  
  /** View engine maintains the view tasks */
  private ViewEngine viewEngine;
  
  private JavaScriptResult    testResult;  

	/**
	 * constructor
	 * @param id
	 */
	public Player(String id){
		this.jsObject = JavaScriptObject.createFunction();
		testResult = new JavaScriptResult(0, 0);
		PlayerGinjector injector = GWT.create( PlayerGinjector.class );
		viewEngine = injector.getViewEngine();
		RootPanel root = RootPanel.get(id);
		viewEngine.mountView(root);
		deliveryEngine = injector.getDeliveryEngine();
		deliveryEngine.setDeliveryEngineEventsListener( this );
	}

	public Player(ComplexPanel container){
		this.jsObject = JavaScriptObject.createFunction();
		testResult = new JavaScriptResult(0, 0);
		
		PlayerGinjector injector = GWT.create( PlayerGinjector.class );
		viewEngine = injector.getViewEngine();
		viewEngine.mountView(container);
		deliveryEngine = injector.getDeliveryEngine();
		deliveryEngine.setDeliveryEngineEventsListener( this );
	}

	public void load(String url){
		deliveryEngine.load(url);
	}

	public void load(XMLData assessmentData, XMLData[] itemsData){
		deliveryEngine.load(assessmentData, itemsData);
	}
	
	public NavigationCommandsListener getNavigationCommandsListener(){
		return deliveryEngine.getNavigationListener();
	}
  
  /**
   * @return js object representing this player
   */
  public JavaScriptObject getJavaScriptObject(){
    return jsObject;
  }

  
  /**
   * Return interface to get test result
   */
  public JavaScriptResult getResult() {

    return testResult;
  }
  
  // ZOSTAJE
  
  /**
   * Return interface to get assessment session time
   */
  public JavaScriptObject getAssessmentSessionReport() {

	  IAssessmentReport report = deliveryEngine.report();
	  
	  int assessmentSessionTime = report.getAssessmentSessionTime();
	  int score = (int)(report.getAssessmentResult().getScore() - report.getAssessmentResult().getMinPoints());
	  int max = (int)(report.getAssessmentResult().getMaxPoints() - report.getAssessmentResult().getMinPoints());
	  int itemIndex = report.getCurrentItemIndex();
	  int itemsCount = report.getItemsCount();
	  int itemsVisited = report.getItemsVisitedCount();
	  boolean passed = report.getAssessmentMasteryPassed();
	  
	  String lessonStatus = "INCOMPLETE";
	  if (itemsVisited == itemsCount){
		  if (max == 0)
			  lessonStatus = "COMPLETED";
		  else if (passed)
			  lessonStatus = "PASSED";
		  else
			  lessonStatus = "FAILED";
	  }
	  
	  JavaScriptObject jsItemScores = JSArrayUtils.createArray(report.getItemsResults().length);
	  JavaScriptObject jsItemScoreMaxs = JSArrayUtils.createArray(report.getItemsResults().length);
	  for (int i = 0 ; i < report.getItemsResults().length ; i ++){
		  jsItemScores = JSArrayUtils.fillArray(jsItemScores, i, (int)(report.getItemsResults()[i].getScore() - report.getItemsResults()[i].getMinPoints()));
		  jsItemScoreMaxs = JSArrayUtils.fillArray(jsItemScoreMaxs, i, (int)(report.getItemsResults()[i].getMaxPoints() - report.getItemsResults()[i].getMinPoints()));
	  }
	  
	  JavaScriptObject jsMistakes = JSArrayUtils.createArray(itemsCount);
	  for (int i = 0 ; i < itemsCount ; i ++)
		  jsMistakes = JSArrayUtils.fillArray(jsMistakes, i, report.getItemsMistakes()[i]);
	  
	  JavaScriptObject jsChecks = JSArrayUtils.createArray(itemsCount);
	  for (int i = 0 ; i < itemsCount ; i ++)
		  jsChecks = JSArrayUtils.fillArray(jsChecks, i, report.getItemsChecks()[i]);
	  
	  JavaScriptObject jsTimes = JSArrayUtils.createArray(itemsCount);
	  for (int i = 0 ; i < itemsCount ; i ++)
		  jsTimes = JSArrayUtils.fillArray(jsTimes, i, report.getItemsTimes()[i]);
	  
	  JavaScriptObject obj = JavaScriptObject.createObject();
	  
	  initAssessmentSessionReportJS(obj, assessmentSessionTime, score, max, lessonStatus, itemIndex+1, itemsCount, 
			  jsItemScores, jsItemScoreMaxs, jsMistakes, jsChecks, jsTimes, report.getTotalMistakes(), report.getTotalChecks());
	  
	  return obj;
  }
  private native static void initAssessmentSessionReportJS(JavaScriptObject obj, int time, int score, int scoreMax, String lessonStatus, 
		  int itemIndex, int itemsCount, JavaScriptObject itemsScores, JavaScriptObject itemsScoreMaxs, JavaScriptObject itemsMistakes, 
		  JavaScriptObject itemChecks, JavaScriptObject itemTimes, int totalMistakes, int totalChecks) /*-{
	  obj.getTime = function(){
		  return time;
	  }
	  obj.getScore = function(){
		  return score;
	  }
	  obj.getScoreMax = function(){
		  return scoreMax;
	  }
	  obj.getLessonStatus = function(){
		  return lessonStatus;
	  }
	  obj.getItemIndex = function(){
		  return itemIndex;
	  }
	  obj.getItemsCount = function(){
		  return itemsCount;
	  }
	  obj.getItemScore = function(index){
	  	return itemsScores[index];
	  }
	  obj.getItemScoreMax = function(index){
	  	return itemsScoreMaxs[index];
	  }
	  obj.getItemMistakes = function(index){
	  	return itemsMistakes[index];
	  }
	  obj.getItemChecks = function(index){
	  	return itemChecks[index];
	  }
	  obj.getItemTime = function(index){
	  	return itemTimes[index];
	  }
	  obj.getTotalMistakes = function(){
	  	return totalMistakes;
	  }
	  obj.getTotalChecks = function(){
	  	return totalChecks;
	  }
  }-*/;

	public void setMasteryScore(int mastery){
		deliveryEngine.setMasteryScore(mastery);
	}

	public void setFlowOptions(FlowOptions o){
		deliveryEngine.setFlowOptions(o);
	}
	
	public void setDisplayOptions(DisplayOptions o){
		deliveryEngine.setDisplayOptions(o);
	}
	
	public ItemVariablesAccessor getItemVariablesAccessor(){
		return deliveryEngine.getItemVariablesAccessor();
	}
  
	/**
	 * Return interface to get assessment session state
	 */
	public String getState() {  
		String stateString = deliveryEngine.getStateInterface().exportState().toString();
		return stateString;
	}
  
	/**
	 * Return interface to get assessment session time
	 */
	public void setState(String obj) {
		 deliveryEngine.getStateInterface().importState(obj);
	}
  
	public JavaScriptObject getEngineMode(){
  		JavaScriptObject obj = JavaScriptObject.createObject();
  		String es = deliveryEngine.getEngineMode();
  		initEngineModeJS(obj, es);
  		return obj;
	}
  	
    private native static void initEngineModeJS(JavaScriptObject obj, String state) /*-{
		obj.toString = function(){
			return state;
		}
	}-*/;

	@Override
	public void onAssessmentLoaded() {
		FlowEventsJSCallbackCaller.onAssessmentLoadedJS(jsObject);
	}

	@Override
	public void onAssessmentStarted() {
		FlowEventsJSCallbackCaller.onAssessmentSessionBeginJS(jsObject);
	}
	
	@Override
	public void onSummary() {
		FlowEventsJSCallbackCaller.onAssessmentSessionFinishedJS(jsObject);
	}

	@Override
	public void onTestPageSwitched() {
		FlowEventsJSCallbackCaller.onTestPageSwitchedJS(jsObject);
	}

	@Override
	public void onTestPageSwitching() {
		FlowEventsJSCallbackCaller.onTestPageSwitchingJS(jsObject);
	}

}
