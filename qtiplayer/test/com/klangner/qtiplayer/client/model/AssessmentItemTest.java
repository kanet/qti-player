package com.klangner.qtiplayer.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AssessmentItemTest extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.klangner.qtiplayer.Qtiplayer";
  }

  /**
   * Check if XML is loaded
   */
  public void testLoad() {
  	AssessmentItem	item = new AssessmentItem();

  	item.load(GWT.getModuleBaseURL() + "items/choice.xml", new IDocumentLoaded(){

			@Override
			public void finishedLoading() {
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

}
