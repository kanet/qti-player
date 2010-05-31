package com.qtitools.player.client.module.mathexpr;

import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

public class MathExprInlineModule extends Widget{
	
	public MathExprInlineModule(Element e){
		contents = new InlineHTML();
		contents.setStyleName("qp-mathexpr");
		String contentsHTML = "<script type=\"math/mml\"><math display=\"inline\">" + e.getChildNodes().toString() + "</math></script>";
		contents.setHTML(contentsHTML);
		setElement(contents.getElement());
		
		try {
			//processMathJax(contents.getElement());
		} catch (Exception exc) {	}
		
		MathJaxProcessor.addMathExprElement(contents.getElement());
	}
	
	private InlineHTML contents;
	
	public native void processMathJax(com.google.gwt.dom.client.Element e)/*-{
		if (typeof $wnd.MathJax !== 'undefined'  &&  $wnd.MathJax != null ){
	  		//alert("level 1");
	  		if (typeof $wnd.MathJax.Hub !== 'undefined'  &&  $wnd.MathJax.Hub != null ){
	  			//alert("level 2");
	  			if (typeof $wnd.MathJax.Hub.Typeset == 'function' ){
	  				//alert("level 3");
	  				$wnd.MathJax.Hub.Typeset(e);
	  			}
	  		}
	  	}
	  }-*/;
	
	
}
