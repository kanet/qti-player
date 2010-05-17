package com.qtitools.player.client.module.match.area;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.VectorObject;
import org.vaadin.gwtgraphics.client.shape.Rectangle;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.components.TouchablePanel;
import com.qtitools.player.client.module.ITouchEventsListener;

public class MatchArea {
	
	public MatchArea(int canvasWidth, int canvasHeight, ITouchEventsListener touchListener){
		canvas = new DrawingArea(canvasWidth, canvasHeight);
		canvasId = Document.get().createUniqueId();
		canvas.getElement().setId(canvasId);
		canvas.setStylePrimaryName("qp-match-area");
		
		fillRect = new Rectangle(0, 0, canvasWidth,canvasHeight);
		fillRect.setFillColor("red");
		fillRect.setFillOpacity(0);
		fillRect.setStrokeWidth(0);
		fillId = Document.get().createUniqueId();
		fillRect.getElement().setId(fillId);
		fillRect.setStyleName("qp-match-area-fill");
		canvas.add(fillRect);
		
		areaCover = new TouchablePanel(touchListener);
		areaCoverId = Document.get().createUniqueId();
		areaCover.getElement().setId(areaCoverId);
		areaCover.setStyleName("qp-match-area-container-cover");

		areaContainer = new AbsolutePanel();
		areaContainerId = Document.get().createUniqueId();
		areaContainer.getElement().setId(areaContainerId);
		areaContainer.setStyleName("qp-match-area-container2");
		areaContainer.add(canvas, 0, 0);
		areaContainer.add(areaCover, 0, 0);
		
		dragLine = new Line(0, 0, 0, 0);
		lineId = Document.get().createUniqueId();
		dragLine.getElement().setId(lineId);
		dragLine.setStyleName("qp-match-area-dragline");
		
	}

	public AbsolutePanel areaContainer;
	public TouchablePanel areaCover;
	public DrawingArea canvas;
	public Rectangle fillRect;
	public Line dragLine;
	
	public String canvasId;
	public String fillId;
	public String areaContainerId;
	public String lineId;
	public String areaCoverId;
	
	
	public Widget getView(){
		return areaContainer;
	}
	
	public void setCanvasSize(int w, int h){
		canvas.setWidth(w);
		canvas.setHeight(h);
		fillRect.setWidth(w);
		fillRect.setHeight(h);
		areaContainer.setSize(String.valueOf(w), String.valueOf(h));
		//areaCover.setSize(String.valueOf(w), String.valueOf(h));
	}
	
	public void addSlot(Group g){
		canvas.add(g);
	}
	
	public void removeAllSlots(){
		canvas.clear();
		fillRect.setFillOpacity(0);
		fillRect.setStrokeWidth(0);
		canvas.add(fillRect);
	}
	
	public void showDragLine(){
		try {
			canvas.add(dragLine);
		}catch (Exception e) {	}
	}
	
	public void hideDragLine(){
		canvas.remove(dragLine);
	}
	
	public void processDragLine(int x1, int y1, int x2, int y2){
			
		dragLine.setX1(x1);
		dragLine.setY1(y1);
		dragLine.setX2(x2);
		dragLine.setY2(y2);
	}

	public void addLine(Line l){
		canvas.add(l);
	}

	public void removeLine(Line l){
		canvas.remove(l);
	}
	
}
