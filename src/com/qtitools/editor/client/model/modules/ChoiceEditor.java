package com.qtitools.editor.client.model.modules;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.qtitools.player.client.util.XMLUtils;

public class ChoiceEditor extends Composite{

	/** choice element */
	private Element choiceElement;
	
	
	/**
	 * Constructor. Add contenteditable
	 * 
	 * @param choiceNode
	 * @param moduleSocket
	 */
	public ChoiceEditor(Element choiceElement) {
		
		this.choiceElement = choiceElement;

		initWidget(createView());
	}

	
	/**
	 * Create editor view
	 * @return widget with view
	 */
	private Widget createView() {
		
		Element prompt = XMLUtils.getFirstElementWithTagName(choiceElement, "prompt");
		Panel vp = new VerticalPanel();
		Panel hp;
		Label	label;
		
		hp = new HorizontalPanel();
		hp.add(new Label("Choice interaction"));
		hp.add(new TextBox());
		vp.add(hp);
		
		hp = new HorizontalPanel();
		hp.add(new Label("Prompt: "));
		label = new Label( prompt.getFirstChild().getNodeValue() );
		label.getElement().setAttribute("contenteditable", "true");
		hp.add(label);
		vp.add(hp);

		return vp;
	}

}
