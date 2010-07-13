package com.qtitools.player.client.util.localisation;

public abstract class LocalePublisher {

	public static String getText(LocaleVariable var){

		String varString = var.toString();
		String text = "";
		
		if (checkVariable(varString)){
			text = getValue(varString);
		} else {
			text = getDefaultValue(var);
		}
		
		return text;
	}

	private static native boolean checkVariable(String varName)/*-{
		if (typeof $wnd.QtiPlayer !== 'undefined'  &&  $wnd.QtiPlayer != null ){
			if (typeof $wnd.QtiPlayer.locale !== 'undefined'  &&  $wnd.QtiPlayer.locale != null ){
				if (typeof $wnd.QtiPlayer.locale[varName] !== 'undefined'  &&  $wnd.QtiPlayer.locale[varName] != null ){
					return true;
				}
			}
		}
		return false;
	}-*/;
	
	private static native String getValue(String varName)/*-{
		return $wnd.QtiPlayer.locale[varName];
	}-*/;
	
	private static String getDefaultValue(LocaleVariable var){
		switch(var){
			case SUMMARY_PAGE:
				return "Page";
		}
		return "";
	}
	
}
