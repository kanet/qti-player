package com.qtitools.editor.client.model.modules;

import com.google.gwt.xml.client.Node;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.util.XMLUtils;

public class ChoiceEditor extends Composite{

	/** choice element */
	private Element choiceElement;
	private FlexTable ft = null;
	private TextBox noOfChoices = null;
	
	/**
	 * Constructor. Add contentEditable
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
		Boolean shuffleBool = new Boolean(choiceElement.getAttribute("shuffle"));
		String maxChoicesString = choiceElement.getAttribute("maxChoices");
		NodeList simpleChoices = choiceElement.getElementsByTagName("simpleChoice");
		ft = new FlexTable();
		int shuffleInt = (shuffleBool)?1:0;

		Panel vp = new VerticalPanel();
		VerticalPanel main = new VerticalPanel();
		vp.setStyleName("qe-a-item");
		vp.setWidth("100%");
		HorizontalPanel hp, hp_wrapper, hp_choice;
		Label l1, l2, l3;
		TextBox maxChoices;
		
		//hp_main = new HorizontalPanel();
		main.setStyleName("qe-a-item");
		main.setWidth("100%");
		main.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		hp_wrapper = new HorizontalPanel();
		hp_wrapper.addStyleName("qe-a-item-head");
		hp_wrapper.setWidth("100%");
		hp_wrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp_wrapper.setHeight("2.3em");
		hp = new HorizontalPanel();
		hp.setHeight("100%");
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setSpacing(4);
		l1 = new Label("Choice");
		l1.setStyleName("qe-a-item-title");
		l1.addStyleDependentName("bold");
		l1.setWidth("60px");
		hp_choice = new HorizontalPanel();
		hp_choice.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp_choice.add(l1);
		hp_choice.setCellWidth(l1,"66px");
		hp_choice.setCellHeight(l1,"100%");
		//hp.add(l1);
		
		hp.add(new Label("shuffle= "));
	
		ListBox lb = new ListBox();
	    lb.insertItem("true","true",1);
	    lb.insertItem("false","false",0);
	    lb.setItemSelected(shuffleInt, true);
		hp.add(lb);

		l3 = new Label("  maxChoices = ");
		l3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hp.add(l3);
		
		maxChoices = new TextBox();
		maxChoices.setText(maxChoicesString);
		maxChoices.setVisibleLength(2);
		maxChoices.setMaxLength(2);
		maxChoices.setWidth("20");
		hp.add(maxChoices);

		hp.add(new Label("  noOfChoices = "));

		noOfChoices = new TextBox();
		noOfChoices.setText(Integer.toString(simpleChoices.getLength()));
		noOfChoices.setVisibleLength(2);
		noOfChoices.setMaxLength(2);
		noOfChoices.setWidth("20");
		hp.add(noOfChoices);
		hp_wrapper.add(hp);
		main.add(hp_wrapper);
		
		Label promptLabel = new Label("Prompt: ");
		promptLabel.addStyleName("qe-a-item-title");
		l2 = new Label( prompt.getFirstChild().getNodeValue() );
		l2.addStyleName("qe-a-item-field");
		l2.getElement().setAttribute("contentEditable", "true");
		
		ft.setWidth("100%");
		ft.setWidget(0, 0, promptLabel);
		ft.setWidget(0, 1, l2);
		ft.getColumnFormatter().setWidth(0, "60px");
		ft.getColumnFormatter().setWidth(1, "100%");
		for(int i=0;i<simpleChoices.getLength();i++)
		{
			ft.setWidget(i+1, 0, new Label("Choice "+(i+1)+":"));
			ft.getCellFormatter().setHorizontalAlignment(i+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			ft.getWidget(i+1, 0).addStyleName("qe-a-item-title");
			ft.setWidget(i+1, 1, new Label(simpleChoices.item(i).getFirstChild().toString()));
			ft.getWidget(i+1, 1).addStyleName("qe-a-item-field");
			ft.getWidget(i+1, 1).getElement().setAttribute("contentEditable", "true");
		}
		
		//vp.add(ft);
		
		// Listen for keyboard events in the noOfChoices box.
		noOfChoices.addKeyPressHandler(new KeyPressHandler() {
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          changeNoOfChoices();
	        }
	      }
	    });

		hp_choice.add(ft);
		main.add(hp_choice);
		
		return main;
	}

	private void changeNoOfChoices(){
		NodeList simpleChoices = choiceElement.getElementsByTagName("simpleChoice");
		int oldNoOfChoices = simpleChoices.getLength();
		int newNoOfChoices = Integer.parseInt(noOfChoices.getText());
		
		if(newNoOfChoices<1 || newNoOfChoices>99)return;
		if(oldNoOfChoices > newNoOfChoices)
		{
			for(int i=(oldNoOfChoices-1);i>=newNoOfChoices;i--)
			{
				ft.removeRow(i+1);
				Node tmp = simpleChoices.item(i);
				choiceElement.removeChild(tmp);
			}
		}
		else if(oldNoOfChoices < newNoOfChoices)
		{
			for(int i=(oldNoOfChoices);i<newNoOfChoices;i++)
			{
				Node tmp = simpleChoices.item(0).cloneNode(true);
				Node tmp2 = null;
				tmp.getFirstChild().setNodeValue("blank");
				//lets remove all unnecessary child nodes
				while((tmp2=tmp.getFirstChild().getNextSibling())!=null)
					tmp.removeChild(tmp2);
				choiceElement.appendChild(tmp);
				ft.setWidget(i+1, 0, new Label("Choice "+(i+1)+":"));
				ft.getCellFormatter().setHorizontalAlignment(i+1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
				ft.getWidget(i+1, 0).addStyleName("qe-a-item-title");
				ft.setWidget(i+1, 1, new Label(" "));
				ft.getWidget(i+1, 1).addStyleName("qe-a-item-field");
				ft.getWidget(i+1, 1).getElement().setAttribute("contentEditable", "true");
			}
		}
	}
}
