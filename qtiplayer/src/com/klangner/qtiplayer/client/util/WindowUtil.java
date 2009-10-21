package com.klangner.qtiplayer.client.util;

public class WindowUtil {

  /**
   * Show alert box
   * @param msg
   */
  public static native void alert(String msg) /*-{
    $wnd.alert(msg);
  }-*/;

}
