package com.qtitools.editor.client.model.modules;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;

public class PEditor extends Composite{

	/** choice element */
	private Element pElement;

	/**
	 * Constructor for new P
	 */
	public PEditor(){
		
	}

	/**
	 * Constructor. Add contentEditable
	 * 
	 * @param choiceNode
	 * @param moduleSocket
	 */
	public PEditor(Element pElement) {

		this.pElement = pElement;
		initWidget(createView());
	}

	/**
	 * Create editor view
	 * @return widget with view
	 */
	private Widget createView() {

		VerticalPanel vp;
		HorizontalPanel hp;
		Label l0, l1, l2;

		vp = new VerticalPanel();
		vp.addStyleName("qe-a-item");
//		l0 = new Label("Text");
//		l0.setStyleName("qe-a-item-head");
//		vp.add(l0);
		hp = new HorizontalPanel();
		hp.setStyleName("qe-a-item");
		hp.setWidth("100%");
		l1 = new Label("Text:");
		l1.setStyleName("qe-a-item-title");
		hp.add(l1);
		hp.setCellWidth(l1,"60px");

		l2 = new Label( pElement.getFirstChild().getNodeValue() );
		l2.getElement().setAttribute("contentEditable","true");
		l2.setStyleName("qe-a-item");
		l2.addStyleDependentName("field");
		hp.add(l2);
		
		vp.add(hp);

		return vp;
	}

}
