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
package com.qtitools.player.client.module.object;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.module.object.impl.AudioImpl;
import com.qtitools.player.client.module.object.impl.VideoImpl;
import com.qtitools.player.client.util.xml.XMLUtils;

public class ObjectModule extends Composite{

	/**
	 * constructor
	 * @param node
	 */
	public ObjectModule(Element node, ModuleSocket  moduleSocket, ModuleStateChangedEventsListener stateChangedListener){
		
	  String html;
	  String src = XMLUtils.getAttributeAsString(node, "data");
	  String type = XMLUtils.getAttributeAsString(node, "type");
	  
	  if(type.startsWith("video")){

	    VideoImpl video = GWT.create(VideoImpl.class);
  		html = video.getHTML(src);
	  }
	  else{
	    
      AudioImpl audio = GWT.create(AudioImpl.class);
      html = audio.getHTML(src);
	  }
		
		initWidget(new HTML(html));
	}
	
}
