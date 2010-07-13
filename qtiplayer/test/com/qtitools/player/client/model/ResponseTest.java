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

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.qtitools.player.client.model.Response;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class ResponseTest extends GWTTestCase {

	private static final String TEST_DATA = 
		"<responseDeclaration identifier='RESPONSE' cardinality='single' baseType='identifier'>" +
		"  <correctResponse>" + 
		"	  <value>ChoiceA</value>" +
		"  </correctResponse>" +
		"</responseDeclaration>";
	
	
  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.qtitools.player.Player";
  }

  /**
   * test id
   */
  public void testID() {
  	Response response = new Response(getDOM());
  	
  	assertEquals("RESPONSE", response.getID());
  }
  
  /**
   * test correct answer
   */
  public void testIsCorrectAnswer() {
  	Response response = new Response(getDOM());
  	
  	assertTrue(response.isCorrectAnswer("ChoiceA"));
  }
  
  /**
   * test correct answer
   */
  public void testSetWrongAnswer() {
  	Response response = new Response(getDOM());
  	
  	response.set("wrong key");
  	
  	assertEquals(1, response.getResult().getMaxPoints());
  	assertEquals(0, response.getResult().getScore());
  }
  
  /**
   * test correct answer
   */
  public void testSetCorrectAnswer() {
  	Response response = new Response(getDOM());
  	
  	response.set("ChoiceA");
  	
  	assertEquals(1, response.getResult().getMaxPoints());
  	assertEquals(1, response.getResult().getScore());
  }
  
  /**
   * test correct answer
   */
  public void testReset() {
  	Response response = new Response(getDOM());
  	
  	response.set("ChoiceA");
  	
  	assertEquals(1, response.getResult().getMaxPoints());
  	assertEquals(1, response.getResult().getScore());
  	
  	response.reset();
  	assertEquals(0, response.getResult().getScore());
  }
  
  /**
   * test correct answer
   */
  public void testUnset() {
  	Response response = new Response(getDOM());
  	
  	response.set("ChoiceA");
  	
  	assertEquals(1, response.getResult().getMaxPoints());
  	assertEquals(1, response.getResult().getScore());
  	
  	response.unset("wrong");
  	assertEquals(1, response.getResult().getScore());

  	response.unset("ChoiceA");
  	assertEquals(0, response.getResult().getScore());
  }
  
  /**
   * Create DOM from TEST_DATA string
   * @return
   */
  private Element getDOM(){
  	Document document;
  	
  	document = XMLParser.parse(TEST_DATA);
  	
  	return document.getDocumentElement();
  }

}
