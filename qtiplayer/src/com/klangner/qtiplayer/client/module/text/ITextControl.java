package com.klangner.qtiplayer.client.module.text;

public interface ITextControl {

  /** get control id */
  public String getID();
  
	/** Send on change event */
	public void onChange();
	
}
