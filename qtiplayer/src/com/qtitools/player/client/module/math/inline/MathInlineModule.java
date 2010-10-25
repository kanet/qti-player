package com.qtitools.player.client.module.math.inline;

import pl.smath.expression.model.Term;
import pl.smath.expression.parser.ExpressionParser;
import pl.smath.expression.parser.ExpressionParserException;
import pl.smath.renderer.renderer.TermRendererException;
import pl.smath.renderer.renderer.TermWidgetFactory;
import pl.smath.renderer.utils.InteractionManager;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.module.IUnattachedComponent;

public class MathInlineModule extends Widget implements IUnattachedComponent {

	public MathInlineModule(Element e, boolean autoLoad){
		this(e);
		if (autoLoad)
			onLoad();
	}
	
	public MathInlineModule(Element e){

		term = null;
		owner = new FlowPanel();
		owner.setStyleName("math-inline");
		if (e.hasAttribute("expression")){
			try {
				term = (new ExpressionParser()).parseText(e.getAttribute("expression"));
			} catch (ExpressionParserException e1) {
			}
		}
		
		setElement(owner.getElement());
	}
	
	private Panel owner;
	private Term term;
	
	public void onLoad(){

		InteractionManager im = new InteractionManager(owner);
		try {
			(new TermWidgetFactory()).createWidget(term, im);
		} catch (TermRendererException e) {
		}
	}

	@Override
	public void onOwnerAttached() {
		onLoad();
	}
	
}
