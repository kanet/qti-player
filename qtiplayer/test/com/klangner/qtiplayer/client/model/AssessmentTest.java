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
package com.klangner.qtiplayer.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AssessmentTest extends GWTTestCase {

	private Assessment	assessment;
	
	
  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.klangner.qtiplayer.Qtiplayer";
  }

  /**
   * Check if XML is loaded
   * @throws LoadException 
   */
  public void testTitle() throws LoadException {
  	
    assessment = new Assessment();

    assessment.load(GWT.getModuleBaseURL() + "simple_toc.xml", new IDocumentLoaded(){

			public void finishedLoading(XMLDocument doc) {
			  assertEquals("Minimal Assessment Test", assessment.getTitle());
				finishTest();
			}
  		
  	});
  	
  	delayTestFinish(2000);
  }

  /**
   * Check if XML is loaded
   * @throws LoadException 
   */
  public void testLoadTitles() throws LoadException {
    
    assessment = new Assessment();

    assessment.load(GWT.getModuleBaseURL() + "simple_toc.xml", new IDocumentLoaded(){

      public void finishedLoading(XMLDocument doc) {
        assertEquals("Minimal Assessment Test", assessment.getTitle());
        
        assessment.loadTitles(new IDocumentLoaded(){

          public void finishedLoading(XMLDocument document) {
            assertEquals("Richard III (Take 2)", assessment.getItemTitle(2));
            finishTest();
          }
          
        });
        
      }
      
    });
    
    delayTestFinish(5000);
  }

}
