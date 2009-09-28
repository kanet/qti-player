package com.klangner.qtiplayer.client.model.modules;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ChoiceModule implements IModule {

	private String prompt;
	
	public ChoiceModule(Node choiceNode){
		NodeList nodes = choiceNode.getChildNodes();
		
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeName().compareTo("prompt") == 0){
				prompt = node.getFirstChild().getNodeValue();
			}
		}
	}
	
	public Widget getView() {
		VerticalPanel vp = new VerticalPanel();
		
		vp.add(new Label("Choice module"));
		vp.add(new Label(prompt));
		
		return vp;
	}

}
