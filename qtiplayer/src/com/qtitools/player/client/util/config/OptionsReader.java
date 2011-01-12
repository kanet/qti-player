package com.qtitools.player.client.util.config;

import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;
import com.qtitools.player.client.controller.messages.OperationMessage;
import com.qtitools.player.client.controller.messages.OperationMessagePoint;
import com.qtitools.player.client.controller.messages.OperationMessageType;
import com.qtitools.player.client.util.localisation.LocalePublisher;
import com.qtitools.player.client.util.localisation.LocaleVariable;

public class OptionsReader {

	public static FlowOptions getFlowOptions(){

		FlowOptions fo = new FlowOptions();

		try {
			if (checkVariable("SHOW_TOC"))
				fo.showToC = getValueBoolean("SHOW_TOC");
			if (checkVariable("SHOW_SUMMARY"))
				fo.showSummary = getValueBoolean("SHOW_SUMMARY");
			if (checkVariable("ITEMS_DISPLAY_MODE"))
				fo.itemsDisplayMode = (getValueString("ITEMS_DISPLAY_MODE").equals("ALL")) ? PageItemsDisplayMode.ALL : PageItemsDisplayMode.ONE;
			if (checkVariable("SHOW_CHECK"))
				fo.showCheck = getValueBoolean("SHOW_CHECK");
		} catch (Exception e) {
			OperationMessage om = new OperationMessage(LocalePublisher.getText(LocaleVariable.MESSAGE_READOPTIONS_ERROR), OperationMessageType.ERROR, 5000, true);
			OperationMessagePoint.showMessage(om);
		}
				
		return fo;
	}

	private static native boolean checkVariable(String varName)/*-{
		if (typeof $wnd.QtiPlayer !== 'undefined'  &&  $wnd.QtiPlayer != null ){
			if (typeof $wnd.QtiPlayer.flowoptions !== 'undefined'  &&  $wnd.QtiPlayer.flowoptions != null ){
				if (typeof $wnd.QtiPlayer.flowoptions[varName] !== 'undefined'  &&  $wnd.QtiPlayer.flowoptions[varName] != null ){
					return true;
				}
			}
		}
		return false;
	}-*/;
	
	private static native String getValueString(String varName)/*-{
		return $wnd.QtiPlayer.flowoptions[varName];
	}-*/;
	
	private static native boolean getValueBoolean(String varName)/*-{
		return $wnd.QtiPlayer.flowoptions[varName];
	}-*/;
}
