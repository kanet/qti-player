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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlayerEntryPoint implements EntryPoint {

	/** Player object */
	private static Player       player;
	
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

      player.getResult = function(){
        return @com.qtitools.player.client.PlayerEntryPoint::getResult()();
      }
      
      player.getAssessmentSessionReport = function(){
        return @com.qtitools.player.client.PlayerEntryPoint::getAssessmentSessionReport()();
      }
      
      return player;
    }

    // Call App loaded function
    if(typeof $wnd.qpOnAppLoaded == 'function') {
      $wnd.qpOnAppLoaded();
    }
  }-*/;

	/**
	 * createPlayer js interface
	 * @param node_id wrap this node
	 */
	public static JavaScriptObject createPlayer(String node_id) {
	  player = new Player(node_id);
	  
	  return player.getJavaScriptObject(); 
	}

	/**
	 * Returns array with result info
	 * @param url
	 */
  public static JavaScriptObject getResult() {
    return player.getResult().getJSObject();
  }
  
	/**
	 * Returns array with result info
	 * @param url
	 */
	public static JavaScriptObject getAssessmentSessionReport() {
	  return player.getAssessmentSessionReport();
	}
   
  /**
   * Load assessment from this url
   * @param url
   */
  public static void load(String url) {
    player.loadAssessment(url);
  }
   
}
