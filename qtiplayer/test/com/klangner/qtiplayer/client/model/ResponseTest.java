package com.klangner.qtiplayer.client.model;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

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
    return "com.klangner.qtiplayer.Qtiplayer";
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
