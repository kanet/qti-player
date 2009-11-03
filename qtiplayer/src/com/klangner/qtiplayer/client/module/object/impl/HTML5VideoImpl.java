package com.klangner.qtiplayer.client.module.object.impl;


public class HTML5VideoImpl implements VideoImpl{

  public String getHTML(String src){
    return "<video src='" + src + "' class='qp-video'>Video not supported!</video>";
  }
}
