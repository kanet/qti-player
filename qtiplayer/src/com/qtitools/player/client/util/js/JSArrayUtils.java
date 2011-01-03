package com.qtitools.player.client.util.js;

import com.google.gwt.core.client.JavaScriptObject;

public class JSArrayUtils {
	  
  public static native JavaScriptObject createArray(int length)/*-{
  	return new Array(length);
  }-*/;
  
  public static native JavaScriptObject fillArray(JavaScriptObject array, int index, int item)/*-{
	array[index] = item;
	return array;
  }-*/;
  
}
