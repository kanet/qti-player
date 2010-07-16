package com.qtitools.player.client.model.feedback;

import java.util.Vector;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.controller.MouseEventPanel;
import com.qtitools.player.client.module.CommonsFactory;
import com.qtitools.player.client.module.mathexpr.MathJaxProcessor;

public class InlineFeedback extends PopupPanel implements IItemFeedback {

	public InlineFeedback(Widget _mountingPoint, Node node){
		super(false, false);
		
		mountingPoint = _mountingPoint;
		shown = false;
		closed = false;
		
		variable = node.getAttributes().getNamedItem("outcomeIdentifier").getNodeValue();
		
		if (node.getAttributes().getNamedItem("identifier") != null)
			value = node.getAttributes().getNamedItem("identifier").getNodeValue();
		else 
			value = "";

		if (node.getAttributes().getNamedItem("senderIdentifier") != null)
			senderIdentifier = node.getAttributes().getNamedItem("senderIdentifier").getNodeValue();
		else 
			senderIdentifier = "";
				
		showHide = (node.getAttributes().getNamedItem("showHide").getNodeValue().toLowerCase().compareTo("show") == 0);

		mathElements = new Vector<com.google.gwt.dom.client.Element>();
		
		containerPanel = new FlowPanel();
		containerPanel.setStyleName("qp-feedback-inline-container");
		
		contentsPanel = new MouseEventPanel();
		contentsPanel.setStyleName("qp-feedback-inline-contents");
		contentsPanel.add(CommonsFactory.getFeedbackView((Element)node, this, mathElements));
		contentsPanel.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				close();
			}
		});
		containerPanel.add(contentsPanel);
		
		setStyleName("qp-feedback-inline");
		setWidget(containerPanel);
	}
	

	private String variable;
	private String value;
	private String senderIdentifier;
	private boolean showHide;
	
	private Widget mountingPoint;
	private FlowPanel containerPanel;
	private MouseEventPanel contentsPanel;
	private Vector<com.google.gwt.dom.client.Element> mathElements;
	
	private boolean shown;
	private boolean closed;
	
	public void onAttach(){
		super.onAttach();
		for (com.google.gwt.dom.client.Element e : mathElements)
			MathJaxProcessor.addMathExprElement(e);
		
		MathJaxProcessor.pushAll();
		mathElements.clear();

		updatePosition();
	}
		
	public String getVariableIdentifier(){
		return variable;
	}
	
	public String getSenderIdentifier(){
		return senderIdentifier;
	}

	public String getValue(){
		return value;
	}

	public boolean showOnMatch(){
		return showHide;
	}

	public void show(ComplexPanel parent){
		if (!shown && !closed){
			super.show();
			shown = true;
		}
		updatePosition();
	}

	public void hide(ComplexPanel parent){
		if (shown){
			super.hide();
			shown = false;
			closed = false;
		}
	}
	
	public void close(){
		if (shown){
			super.hide();
			closed = true;
		}
	}

	@Override
	public Widget getView() {
		return null;
	}

	@Override
	public boolean hasHTMLContent() {
		return true;
	}

	@Override
	public boolean hasSoundContent() {
		return false;
	}

	private void updatePosition(){
		setPopupPosition(mountingPoint.getAbsoluteLeft(), mountingPoint.getAbsoluteTop()-getOffsetHeight());
	}
	
}
