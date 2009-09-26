package com.klangner.qtiplayer.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Qtiplayer implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		VerticalPanel playerPanel = new VerticalPanel();
		Label 				header = new Label();
		VerticalPanel body = new VerticalPanel();
		Label 	feedback = new Label();
		Label 	footer = new Label();
		HTML		testLink = new HTML();

		playerPanel.setStyleName("qp-player");
		header.setText("Header");
		header.setStyleName("qp-header");
		playerPanel.add(header);
		
		body.setStyleName("qp-body");
		body.add(testLink);
		playerPanel.add(body);
		
		feedback.setText("feeback");
		feedback.setStyleName("qp-feedback");
		playerPanel.add(feedback);
		
		footer.setText("Footer");
		footer.setStyleName("qp-footer");
		playerPanel.add(footer);

		Element element = RootPanel.get("player").getElement();
		Node node = element.getFirstChild();
		testLink.setHTML("<a href=\"" + node.getNodeValue() +"\">Sample</a>");
		element.removeChild(node);

		RootPanel.get("player").add(playerPanel);
	}
}
