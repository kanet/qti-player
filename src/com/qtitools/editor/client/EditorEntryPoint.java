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
package com.qtitools.editor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

public class EditorEntryPoint implements EntryPoint {

	/** Editor controller */
	private static Editor       editor;
	
  /**
   * Init Javascript API
   */
  private static native void initJavaScriptAPI() /*-{
     
    // Create Editor
    $wnd.qpCreateEditor = function(id) {
      var editor = @com.qtitools.editor.client.EditorEntryPoint::createEditor(Ljava/lang/String;)(id);
      editor.load = function(url){
          @com.qtitools.editor.client.EditorEntryPoint::load(Ljava/lang/String;)(url);
        }
      
      return editor;
    }

    // Call App loaded function
    if(typeof $wnd.qpOnEditorLoaded == 'function') {
      $wnd.qpOnEditorLoaded();
    }
  }-*/;

	/**
	 * createPlayer js interface
	 * @param node_id wrap this node
	 */
	public static JavaScriptObject createEditor(String node_id) {
	  editor = new Editor(node_id);
	  
	  return editor.getJavaScriptObject(); 
	}

	/**
	 * Load assessment from this url
	 * @param url
	 */
  public static void load(String url) {
    editor.loadAssessment(url);
  }
   
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Define js API
		initJavaScriptAPI();
	}
}
