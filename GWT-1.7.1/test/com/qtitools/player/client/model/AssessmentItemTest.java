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
package com.qtitools.player.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.qtitools.player.client.model.AssessmentItem;
import com.qtitools.player.client.model.IDocumentLoaded;
import com.qtitools.player.client.model.LoadException;
import com.qtitools.player.client.model.RuntimeModuleFactory;
import com.qtitools.player.client.model.XMLDocument;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IResponse;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AssessmentItemTest extends GWTTestCase {

	private AssessmentItem	item;
	
	
  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.qtitools.player.Player";
  }

  /**
   * Check if XML is loaded
   * @throws LoadException 
   */
  public void testLoad() throws LoadException {
  	
  	item = new AssessmentItem(new RuntimeModuleFactory());

  	item.load(GWT.getModuleBaseURL() + "items/choice.xml", new IDocumentLoaded(){

			public void finishedLoading(XMLDocument doc) {
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

  /**
   * Check if XML is loaded
   * @throws LoadException 
   */
  public void testResponse() throws LoadException {

  	item = new AssessmentItem(new RuntimeModuleFactory());
  	item.load(GWT.getModuleBaseURL() + "items/choice.xml", new IDocumentLoaded(){

			public void finishedLoading(XMLDocument doc) {
				IModuleSocket socket = item.getModuleSocket();
				IResponse response = socket.getResponse("RESPONSE");
				
				assertTrue( response.isCorrectAnswer("ChoiceA") );
				
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

}
