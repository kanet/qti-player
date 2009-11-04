package com.klangner.qtiplayer.client.module.object.impl;


public class LinkAudioImpl implements AudioImpl{

  public String getHTML(String src){
    return "<a href='" + src + "'>Play</a>";
  }
}
