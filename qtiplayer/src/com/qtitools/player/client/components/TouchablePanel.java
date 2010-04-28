package com.qtitools.player.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.qtitools.player.client.module.ITouchEventsListener;

public class TouchablePanel extends FlowPanel {

	public TouchablePanel(ITouchEventsListener tel){
		super();
		touchEventsListener = tel;
		registerTouchEvents(this.getElement());
	}
	
	private ITouchEventsListener touchEventsListener;
	
	private native void registerTouchEvents(Element element)/*-{
		var instance = this;
		element.ontouchstart = function (e){
			e.preventDefault();
			if(e.touches.length==1){
				var touch=e.touches[0];
				instance.@com.qtitools.player.client.components.TouchablePanel::processOnTouchStartEvent(II)(touch.pageX, touch.pageY);
			}
		}
		
		element.ontouchmove = function (e){
			e.preventDefault();
			if(e.touches.length==1){
				var touch=e.touches[0];
				instance.@com.qtitools.player.client.components.TouchablePanel::processOnTouchMoveEvent(II)(touch.pageX, touch.pageY);
			}
		}
		
		element.ontouchend = function (e){
			e.preventDefault();
			instance.@com.qtitools.player.client.components.TouchablePanel::processOnTouchEndEvent()();
		}
			
	}-*/;

	@SuppressWarnings("unused")
	private void processOnTouchStartEvent(int x, int y){
		//alert("processOnTouchStartEvent");
		touchEventsListener.onTouchStart(this.getElement(), x,y);
	}
	
	@SuppressWarnings("unused")
	private void processOnTouchMoveEvent(int x, int y){
		//alert("processOnTouchMoveEvent");
		touchEventsListener.onTouchMove(this.getElement(), x, y);
	}
	
	@SuppressWarnings("unused")
	private void processOnTouchEndEvent(){
		//alert("processOnTouchEndEvent");
		touchEventsListener.onTouchEnd(this.getElement());
	}
	
	@SuppressWarnings("unused")
	private native void alert(String s)/*-{
		alert(s);
	}-*/;
}
