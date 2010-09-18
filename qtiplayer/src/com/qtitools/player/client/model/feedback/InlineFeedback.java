package com.qtitools.player.client.model.feedback;

import java.util.Vector;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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

		if (node.getAttributes().getNamedItem("sound") != null)
			soundAddress = node.getAttributes().getNamedItem("sound").getNodeValue();
		else 
			soundAddress = "";

		if (node.getAttributes().getNamedItem("senderIdentifier") != null)
			senderIdentifier = node.getAttributes().getNamedItem("senderIdentifier").getNodeValue();
		else 
			senderIdentifier = "";
		
		if (node.getAttributes().getNamedItem("align") != null)
			align = InlineFeedbackAlign.fromString(node.getAttributes().getNamedItem("align").getNodeValue());
				
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
	private String soundAddress;
	private String senderIdentifier;
	private boolean showHide;
	private InlineFeedbackAlign align = InlineFeedbackAlign.TOP_LEFT;

	private String baseUrl;
	
	private Widget mountingPoint;
	private Widget bodyView;
	private FlowPanel containerPanel;
	private MouseEventPanel contentsPanel;
	private Vector<com.google.gwt.dom.client.Element> mathElements;
	
	private boolean shown;
	private boolean closed;

	private final int BODY_MARGIN = 10;
	
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
		return soundAddress.length() > 0;
	}
	
	public void processSound(){
		String combinedAddress;
		if (soundAddress.startsWith("http://")  ||  soundAddress.startsWith("https://"))
			combinedAddress = soundAddress;
		else
			combinedAddress = baseUrl + soundAddress;
		SoundController ctrl = new SoundController();
		Sound sound = ctrl.createSound(Sound.MIME_TYPE_AUDIO_MPEG, combinedAddress);
		sound.play();
	}

	private void updatePosition(){
		int mountingPointX = 0;
		int mountingPointY = 0;

		if (InlineFeedbackAlign.isLeft(align)){
			mountingPointX = mountingPoint.getAbsoluteLeft();
		} else if (InlineFeedbackAlign.isRight(align)){
			mountingPointX = mountingPoint.getAbsoluteLeft() + mountingPoint.getOffsetWidth() - getOffsetWidth();
		} else {
			mountingPointX = mountingPoint.getAbsoluteLeft() + (mountingPoint.getOffsetWidth() - getOffsetWidth())/2;
		}
		
		if (InlineFeedbackAlign.isTop(align)){
			mountingPointY = mountingPoint.getAbsoluteTop() - getOffsetHeight();
		} else {
			mountingPointY = mountingPoint.getAbsoluteTop() + mountingPoint.getOffsetHeight();
		}
		
		if (bodyView != null){
			if (mountingPointX < bodyView.getAbsoluteLeft() + BODY_MARGIN){
				mountingPointX = bodyView.getAbsoluteLeft() + BODY_MARGIN;
			} else if (mountingPointX + getOffsetWidth() > bodyView.getAbsoluteLeft() + bodyView.getOffsetWidth() - BODY_MARGIN){
				mountingPointX = bodyView.getAbsoluteLeft() + bodyView.getOffsetWidth() - getOffsetWidth() - BODY_MARGIN;
			}
		}
		
		setPopupPosition(mountingPointX, mountingPointY);
	}
	
	public void setBodyContainer(Widget bodyView){
		this.bodyView = bodyView;
	}
	
	public void setBaseUrl(String bUrl){
		baseUrl = bUrl;
	}
}
