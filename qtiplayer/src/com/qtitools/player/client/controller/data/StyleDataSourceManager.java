package com.qtitools.player.client.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.controller.communication.PageReference;
import com.qtitools.player.client.controller.data.events.StyleDataLoaderEventListener;
import com.qtitools.player.client.style.JsCssModel;
import com.qtitools.player.client.style.StyleSocket;
import com.qtitools.player.client.util.js.JSOModel;

/**
 * Requires that jscssp.js script is added to module xml file
 * 
 * @see http://glazman.org/JSCSSP/
 * @author Micha³ Samuj³o msamujlo@ydp.com.pl
 */
public class StyleDataSourceManager implements StyleSocket {

	// style declarations for assessment
	List<JavaScriptObject> assessmentStyle;

	// style declarations for all items
	Vector<List<JavaScriptObject>> itemStyle;

	/**
	 * Style declarations that should be searched for styles. When player
	 * changes displayed page activeItemStyles should be rebuild.
	 */
	Vector<List<JavaScriptObject>> activeItemStyles;

	public StyleDataSourceManager() {
		assessmentStyle = new ArrayList<JavaScriptObject>();

		itemStyle = new Vector<List<JavaScriptObject>>();
		activeItemStyles = new Vector<List<JavaScriptObject>>();
	}
	
	public void addAssessmentStyle(String css) {
		assessmentStyle.add(parseCss(css));
	}

	public void addItemStyle(int i, String css) {
		if (i >= itemStyle.size()) {
			itemStyle.setSize(i + 1);
		}
		List<JavaScriptObject> styles = itemStyle.get(i);
		if (styles == null) {
			styles = new ArrayList<JavaScriptObject>();
			itemStyle.set(i, styles);
		}
		styles.add(parseCss(css));
	}

	public JSOModel getStyleProperties(Element element) {
		String selector = element.getNodeName();
		JSOModel result = JavaScriptObject.createObject().cast();
		for (JavaScriptObject sheet : assessmentStyle) {
			JsCssModel cssModel = sheet.cast();
			cssModel.getDeclarationsForSelector(selector, result);
		}
		for (List<JavaScriptObject> styles : activeItemStyles) {
			if (styles == null) {
				continue;
			}
			for (JavaScriptObject sheet : styles) {
				JsCssModel cssModel = sheet.cast();
				result = cssModel.getDeclarationsForSelector(selector, result);
			}
		}
		return result;
	}

	@Override
	public Map<String, String> getStyles(Element element) {
		Map<String, String> map = new HashMap<String, String>();
		JSOModel result = getStyleProperties(element);
		JsArrayString keys = result.keys();
		for (int i = 0; i < keys.length(); i++) {
			String key = keys.get(i);
			map.put(key, result.get(key));
		}
		return map;
	}
	
	@Override
	public void setCurrentPages(PageReference pr) {
		activeItemStyles = new Vector<List<JavaScriptObject>>( pr.pageIndices.length );
		for (int pageIndex : pr.pageIndices) {
			activeItemStyles.add( itemStyle.get(pageIndex) );
		}
	}

	private native JavaScriptObject parseCss(String css) /*-{
		var parser = new $wnd.CSSParser();
		return parser.parse(css, false, true);
	}-*/;

}
