package com.klangner.qtiplayer.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AssessmentItemTest extends GWTTestCase {

	private AssessmentItem	item;
	
	
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
  	
  	item = new AssessmentItem();

  	item.load(GWT.getModuleBaseURL() + "items/choice.xml", new IDocumentLoaded(){

			@Override
			public void finishedLoading() {
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

  /**
   * Check if XML is loaded
   */
  public void testResponse() {

  	item = new AssessmentItem();
  	item.load(GWT.getModuleBaseURL() + "items/choice.xml", new IDocumentLoaded(){

			@Override
			public void finishedLoading() {
				IModuleSocket socket = item.getModuleSocket();
				IResponse response = socket.getResponse("RESPONSE");
				
				assertTrue( response.isCorrectAnswer("ChoiceA") );
				
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

}
