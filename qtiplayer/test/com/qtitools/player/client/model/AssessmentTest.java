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
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.model.IDocumentLoaded;
import com.qtitools.player.client.model.LoadException;
import com.qtitools.player.client.model.XMLDocument;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AssessmentTest extends GWTTestCase {

	private Assessment	assessment;
	
	
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
  public void testRef() throws LoadException {
    
    assessment = new Assessment();

    assessment.load(GWT.getModuleBaseURL() + "simple_toc.xml", new IDocumentLoaded(){

      public void finishedLoading(XMLDocument doc) {
        assertTrue(assessment.getItemRef(2).endsWith("inline_choice.xml"));
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

  /**
   * test function move
   * @throws LoadException 
   */
  public void testMove() throws LoadException {
    
    assessment = new Assessment();

    assessment.load(GWT.getModuleBaseURL() + "simple_toc.xml", new IDocumentLoaded(){

      public void finishedLoading(XMLDocument doc) {
        assertEquals("Minimal Assessment Test", assessment.getTitle());
        
        assessment.loadTitles(new IDocumentLoaded(){

          public void finishedLoading(XMLDocument document) {
            NodeList  nodes;
            Element   element;

            nodes = assessment.getDom().getElementsByTagName("assessmentItemRef");
            element = (Element)nodes.item(2);
            assertTrue(element.getAttribute("href").endsWith("inline_choice.xml"));
            assertEquals("Richard III (Take 2)", assessment.getItemTitle(2));
            assertTrue(assessment.getItemRef(2).endsWith("inline_choice.xml"));

            assessment.moveItem(2, 1);
            nodes = assessment.getDom().getElementsByTagName("assessmentItemRef");
            element = (Element)nodes.item(1);
            assertTrue(element.getAttribute("href").endsWith("inline_choice.xml"));
            assertTrue(assessment.getItemRef(1).endsWith("inline_choice.xml"));
            assertEquals("Richard III (Take 2)", assessment.getItemTitle(1));
            
            assessment.moveItem(1, 2);
            nodes = assessment.getDom().getElementsByTagName("assessmentItemRef");
            element = (Element)nodes.item(2);
            assertTrue(element.getAttribute("href").endsWith("inline_choice.xml"));
            assertEquals("Richard III (Take 2)", assessment.getItemTitle(2));
            assertTrue(assessment.getItemRef(2).endsWith("inline_choice.xml"));
            
            finishTest();
          }
          
        });
        
      }
      
    });
    
    delayTestFinish(5000);
  }


  /**
   * test function move
   * @throws LoadException 
   */
  public void testMove2() throws LoadException {
    
    assessment = new Assessment();

    assessment.load(GWT.getModuleBaseURL() + "simple_toc.xml", new IDocumentLoaded(){

      public void finishedLoading(XMLDocument doc) {
        assertEquals("Minimal Assessment Test", assessment.getTitle());
        
        assessment.loadTitles(new IDocumentLoaded(){

          public void finishedLoading(XMLDocument document) {
            NodeList  nodes;
            Element   element;

            assessment.moveItem(2, 3);
            nodes = assessment.getDom().getElementsByTagName("assessmentItemRef");
            element = (Element)nodes.item(3);
            assertTrue(element.getAttribute("href").endsWith("inline_choice.xml"));
            assertTrue(assessment.getItemRef(3).endsWith("inline_choice.xml"));
            assertEquals("Richard III (Take 2)", assessment.getItemTitle(3));
            
            finishTest();
          }
          
        });
        
      }
      
    });
    
    delayTestFinish(5000);
  }


}
