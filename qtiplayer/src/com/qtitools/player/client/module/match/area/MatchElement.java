package com.qtitools.player.client.module.match.area;


import java.util.Vector;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Circle;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
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
		
		view = new AbsolutePanel();
		if (side == MatchSide.LEFT)
			view.setStylePrimaryName("qp-match-element-left-container");
		else if (side == MatchSide.RIGHT)
			view.setStylePrimaryName("qp-match-element-right-container");
		
		textContainer = new FlowPanel();
		if (side == MatchSide.LEFT)
			textContainer.setStylePrimaryName("qp-match-element-left-text-container");
		else if (side == MatchSide.RIGHT)
			textContainer.setStylePrimaryName("qp-match-element-right-text-container");
		textContainer.add(text);
		
		view.add(textContainer, 0, 0);
		
		outerCircle = new Circle(0, 0, 10);
		outerCircleId = Document.get().createUniqueId();
		outerCircle.getElement().setId(outerCircleId);
		outerCircle.setStyleName("qp-match-slot-outer");
		outerCircle.setStrokeWidth(0);
		outerCircle.setFillColor("gray");

		innerCircle = new Circle(0, 0, 5);
		innerCircleId = Document.get().createUniqueId();
		innerCircle.getElement().setId(innerCircleId);
		innerCircle.setStyleName("qp-match-slot-inner");
		innerCircle.setStrokeWidth(0);
		innerCircle.setFillColor("white");

		group = new Group();
		group.add(outerCircle);
		group.add(innerCircle);
		
		slotCover = new AbsolutePanel();
		slotCoverId = Document.get().createUniqueId();
		slotCover.getElement().setId(slotCoverId);
		slotCover.setStyleName("qp-match-slot-cover");
		
		labelCover = new Rectangle(0, 0, 0, 0);
		labelCoverId = Document.get().createUniqueId();
		labelCover.getElement().setId(labelCoverId);
		labelCover.setStyleName("qp-match-element-cover");
		labelCover.setStrokeWidth(0);
		labelCover.setFillColor("green");
		labelCover.setFillOpacity(0);
		
		//view.add(labelCover, 0, 0);
	}
	
	public MatchSide side;
	public String identifier;
	public int matchMax;
	
	public String title;
	
	public AbsolutePanel view;
	public FlowPanel textContainer;
	public InlineHTML text;
	
	public Circle outerCircle;
	public Circle innerCircle;
	public Group group;

	public String outerCircleId;
	public String innerCircleId;
	
	
	private int slotAnchorX;
	private int slotAnchorY;
	
	private AbsolutePanel slotCover;
	public String slotCoverId;
	
	private Rectangle labelCover;
	public String labelCoverId;
	
	public AbsolutePanel getView(){
		return view;
	}
	
	public Group getSlot(int x, int y, int r){
		slotAnchorX = x;
		slotAnchorY = y;

		outerCircle.setStrokeWidth(0);
		outerCircle.setFillColor("gray");
		innerCircle.setStrokeWidth(0);
		innerCircle.setFillColor("white");
		
		innerCircle.setX(x);
		innerCircle.setY(y);
		innerCircle.setRadius(r/2);
		outerCircle.setX(x);
		outerCircle.setY(y);
		outerCircle.setRadius(r);
		
		return group;
	}
	
	public AbsolutePanel getSlotCover(){
		return slotCover;
	}
	
	public Rectangle getLabelCover(int cX, int cY, int cWidth, int cHeight){
		labelCover.setX(cX);
		labelCover.setY(cY);
		labelCover.setWidth(cWidth);
		labelCover.setHeight(cHeight);
		return labelCover;
	}
	
	public boolean isBelongingId(String id){
		return (id.compareTo(innerCircleId) == 0  ||  id.compareTo(outerCircleId) == 0   ||  
				id.compareTo(slotCoverId) == 0   ||  id.compareTo(labelCoverId) == 0  );
	}

	public int getSlotAnchorX(){
		return slotAnchorX;
	}

	public int getSlotAnchorY(){
		return slotAnchorY;
	}
}
