package com.qtitools.player.client.module.match.area;


import java.util.Vector;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.util.xml.XMLConverter;
import com.qtitools.player.client.util.xml.XMLUtils;

public class MatchElement {

	public MatchElement(Element element, MatchSide _side){
		side = _side;
		title = "asd";

		identifier = XMLUtils.getAttributeAsString(element, "identifier");
		matchMax = Integer.parseInt(XMLUtils.getAttributeAsString(element, "matchMax"));

		Vector<String> ignoredTags = new Vector<String>();
		ignoredTags.add("feedbackInline");
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM(element, ignoredTags);
		
		text = new InlineHTML();
		text.setHTML(dom.getInnerHTML());	
		if (side == MatchSide.LEFT)
			text.setStylePrimaryName("qp-match-element-left-text");
		else if (side == MatchSide.RIGHT)
			text.setStylePrimaryName("qp-match-element-right-text");
		
		textContainer = new FlowPanel();
		if (side == MatchSide.LEFT)
			textContainer.setStylePrimaryName("qp-match-element-left-text-container");
		else if (side == MatchSide.RIGHT)
			textContainer.setStylePrimaryName("qp-match-element-right-text-container");
		textContainer.add(text);
		
		slot = new FlowPanel();
		if (side == MatchSide.LEFT)
			slot.setStylePrimaryName("qp-match-element-left-slot");
		else if (side == MatchSide.RIGHT)
			slot.setStylePrimaryName("qp-match-element-right-slot");
		
		view = new HorizontalPanel();
		if (side == MatchSide.LEFT)
			view.setStylePrimaryName("qp-match-element-left-container");
		else if (side == MatchSide.RIGHT)
			view.setStylePrimaryName("qp-match-element-right-container");

		if (side == MatchSide.LEFT){
			view.add(textContainer);
			view.add(slot);
		} else if (side == MatchSide.RIGHT){
			view.add(slot);
			view.add(textContainer);
		}
		
		//view.add(labelCover, 0, 0);
	}
	
	public MatchSide side;
	public String identifier;
	public int matchMax;
	
	public String title;
	
	public HorizontalPanel view;
	public FlowPanel textContainer;
	public InlineHTML text;
	public FlowPanel slot;
	
	private int slotAnchorX;
	private int slotAnchorY;
	
	public Panel getView(){
		return view;
	}
	
	public void setSlotAnchor(int x, int y){
		slotAnchorX = x;
		slotAnchorY = y;
	}
	
	public boolean isBelongingLocation(int x, int y, int parentX, int parentY){
		int vx = view.getAbsoluteLeft() - parentX;
		int vy = view.getAbsoluteTop() - parentY;
		int vw = view.getOffsetWidth();
		int vh = view.getOffsetHeight();
		
		if (vx == 0) {
			vw += slot.getOffsetWidth();
		}else {
			vx -= slot.getOffsetWidth();
			vw += slot.getOffsetWidth();
		}
		
		return (x >= vx &&  y >= vy && x <= vx+vw &&  y <= vy+vh);
	}

	public int getSlotAnchorX(){
		return slotAnchorX;
	}

	public int getSlotAnchorY(){
		return slotAnchorY;
	}
}
