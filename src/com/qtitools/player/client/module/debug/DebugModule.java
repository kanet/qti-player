/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.qtitools.player.client.module.debug;

import java.util.Vector;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.module.IBrowserEventHandler;

public class DebugModule extends Composite implements IBrowserEventHandler{

	/**
	 * constructor
	 * @param node
	 */
	public DebugModule(Element node){
		HTML htmlLabel;
		
		htmlLabel = new HTML(node.toString());
		htmlLabel.setStyleName("qp-text-module");
		initWidget(htmlLabel);
	}
	
	/**
	 * Process on change event 
	 */
	public void onChange(Event event){
		
	}
	
	@Override
	public Vector<InternalEventTrigger> getTriggers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleEvent(String tagID, InternalEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
}
