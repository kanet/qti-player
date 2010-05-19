package com.qtitools.player.client.model.feedback;

import java.util.Vector;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.util.xml.XMLConverter;

public class ModalFeedback {

	public ModalFeedback(Node node){
		variable = node.getAttributes().getNamedItem("outcomeIdentifier").getNodeValue();
		
		if (node.getAttributes().getNamedItem("identifier") != null)
			value = node.getAttributes().getNamedItem("identifier").getNodeValue();
		else 
			value = "";
		
		show = (node.getAttributes().getNamedItem("showHide").getNodeValue().toLowerCase().compareTo("show") == 0);
		
		String contentsHTML = XMLConverter.getDOM((Element)node, new Vector<String>()).getInnerHTML();
		
		contents = new InlineHTML();
		contents.setStyleName("qp-feedback-modal-contents");
		contents.setHTML(contentsHTML);
		
		container = new FlowPanel();
		container.setStyleName("qp-feedback-modal");
		container.add(contents);
	}
	
	private String variable;
	private String value;
	private boolean show;
	
	private FlowPanel container;
	private InlineHTML contents;
	
	public FlowPanel getView(){
		return container;
	}

	public String getVariableIdentifier(){
		return variable;
	}

	public String getValue(){
		return value;
	}
	
	
	public boolean showOnMatch(){
		return show;
	}
	
}
