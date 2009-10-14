package com.klangner.qtiplayer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PlayerEntryPoint implements EntryPoint {

	/** Player object */
	private static Player       player;
	
  /**
   * Init Javascript API
   */
  private static native void initJavaScriptAPI() /*-{
     
    // CreatePlayer
    $wnd.qpCreatePlayer = function(id) {
      var player = @com.klangner.qtiplayer.client.PlayerEntryPoint::createPlayer(Ljava/lang/String;)(id);
      player.load = function(url){
          @com.klangner.qtiplayer.client.PlayerEntryPoint::load(Ljava/lang/String;)(url);
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
	 * Load assessment from this url
	 * @param url
	 */
  public static void load(String url) {
    player.loadAssessment(url);
  }
   
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Define js API
		initJavaScriptAPI();
	}
	
}
