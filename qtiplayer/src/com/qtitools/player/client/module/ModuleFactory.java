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
package com.qtitools.player.client.module;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.components.EmptyWidget;
import com.qtitools.player.client.module.audioplayer.AudioPlayerModule;
import com.qtitools.player.client.module.choice.ChoiceModule;
import com.qtitools.player.client.module.debug.DebugModule;
import com.qtitools.player.client.module.dragdrop.DragDropModule;
import com.qtitools.player.client.module.selection.SelectionModule;
import com.qtitools.player.client.module.identification.IdentificationModule;
import com.qtitools.player.client.module.match.MatchModule;
import com.qtitools.player.client.module.math.inline.MathInlineModule;
import com.qtitools.player.client.module.math.interaction.MathModule;
import com.qtitools.player.client.module.mathexpr.MathExprInlineModule;
import com.qtitools.player.client.module.object.ObjectModule;
import com.qtitools.player.client.module.order.OrderModule;
import com.qtitools.player.client.module.test.TestModule;
import com.qtitools.player.client.module.text.InlineChoiceModule;
import com.qtitools.player.client.module.text.TextEntryModule;
import com.qtitools.player.client.module.vocabox.VocaboxModule;

public abstract class ModuleFactory {
	
	protected static String[] SUPPORTED_MODULES ={"choiceInteraction", 
												"inlineChoiceInteraction", 
												"textEntryInteraction",
												"orderInteraction",
												"matchInteraction",
												"selectionInteraction",
												"identificationInteraction",
												"testInteraction",
												"object",
												"math",
												"mathInline",
												"mathInteraction",
												"dragDropInteraction",
												"qy:comment",
												"audioPlayer",
												"vocabox"};

	public static boolean isSupported(String test){
		for (int s= 0 ; s <SUPPORTED_MODULES.length ; s++)
			if (SUPPORTED_MODULES[s].compareTo(test) == 0)
				return true;
		
		return false;
	}
	
	public static Widget createWidget(Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener){
		Widget	widget = null;

		if(element.getNodeName().compareTo("choiceInteraction") == 0)
			widget = new ChoiceModule(element, moduleSocket, moduleEventsListener);
	    else if(element.getNodeName().compareTo("object") == 0)
	    	widget = new ObjectModule(element, moduleSocket, moduleEventsListener);
	    else if(element.getNodeName().compareTo("inlineChoiceInteraction") == 0)
			widget = new InlineChoiceModule(element, moduleSocket, moduleEventsListener);	
		else if(element.getNodeName().compareTo("textEntryInteraction") == 0)
			widget = new TextEntryModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("testInteraction") == 0)
			widget = new TestModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("orderInteraction") == 0)
			widget = new OrderModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("matchInteraction") == 0)
			widget = new MatchModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("selectionInteraction") == 0)
			widget = new SelectionModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("identificationInteraction") == 0)
			widget = new IdentificationModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("math") == 0)
			widget = new MathExprInlineModule(element);
		else if(element.getNodeName().compareTo("mathInteraction") == 0)
			widget = new MathModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("dragDropInteraction") == 0)
			widget = new DragDropModule(element, moduleSocket, moduleEventsListener);
		else if(element.getNodeName().compareTo("mathInline") == 0)
			widget = new MathInlineModule(element);
		else if(element.getNodeName().compareTo("audioPlayer") == 0)
			widget = new AudioPlayerModule(element);
		else if(element.getNodeName().compareTo("vocabox") == 0)
			widget = new VocaboxModule(element);
		else if(element.getNodeName().compareTo("qy:comment") == 0)
			widget = new EmptyWidget();
		else if(element.getNodeType() == Node.ELEMENT_NODE)
	    	widget = new DebugModule(element);
		
		return widget;
	}
	
}
