package com.qtitools.player.client.model.feedback;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.module.CommonsFactory;

public class ModalFeedback {

	public ModalFeedback(Node node, String _baseUrl){
		
		baseUrl = _baseUrl;
		
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
		
		
		show = (node.getAttributes().getNamedItem("showHide").getNodeValue().toLowerCase().compareTo("show") == 0);
		
		//contentsHTML = XMLConverter.getDOM((Element)node, new Vector<String>()).getInnerHTML();
		contentWidget = CommonsFactory.getInlineTextView((Element)node, null);
		
		contents = new FlowPanel();
		//contents = new InlineHTML();
		contents.setStyleName("qp-feedback-modal-contents");
		//contents.setHTML(contentsHTML);
		contents.add(contentWidget);
		
		container = new FlowPanel();
		container.setStyleName("qp-feedback-modal");
		container.add(contents);
	}
	
	private String variable;
	private String value;
	//private String contentsHTML;
	private Widget contentWidget;
	private String soundAddress;
	private String senderIdentifier;
	private boolean show;
	
	private String baseUrl;
	
	private FlowPanel container;
	private FlowPanel contents;
	//private InlineHTML contents;
	
	public FlowPanel getView(){
		return container;
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
	
	public boolean hasHTMLContent(){
		//return contentsHTML.length() > 0;
		return contentWidget.getElement().toString().length() > 0;
	}
	
	public boolean hasSoundContent(){
		return soundAddress.length() > 0;
	}
	
	public boolean showOnMatch(){
		return show;
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
	
}
