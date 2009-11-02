package com.klangner.qtieditor.client;

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
      var editor = @com.klangner.qtieditor.client.EditorEntryPoint::createEditor(Ljava/lang/String;)(id);
      editor.load = function(url){
          @com.klangner.qtieditor.client.EditorEntryPoint::load(Ljava/lang/String;)(url);
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
