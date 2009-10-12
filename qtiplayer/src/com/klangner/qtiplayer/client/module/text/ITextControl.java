package com.klangner.qtiplayer.client.module.text;

public interface ITextControl {

	/** Send on change event */
	public void onChange();
	
	/** Enable or disable control */
	public void setEnabled(boolean state);
}
