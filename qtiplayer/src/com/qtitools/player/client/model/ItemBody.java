package com.qtitools.player.client.model;

import java.io.Serializable;
import java.util.Vector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IBrowserEventListener;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.ModuleFactory;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.util.MultikeyHashMap;
import com.qtitools.player.client.util.xml.XMLConverter;

public class ItemBody extends Widget implements IActivity, IStateful {

	public MultikeyHashMap<Vector<String>, IInteractionModule, String>	modules = new MultikeyHashMap<Vector<String>, IInteractionModule, String>();
	
	public Vector<Widget>	widgets = new Vector<Widget>();
	
	public Vector<String> idsIB = new Vector<String>();
	
	public ItemBody(Node itemBodyNode, IModuleSocket moduleSocket){
				
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM((Element)itemBodyNode, moduleSocket, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return ModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element, IModuleSocket moduleSocket) {
				Widget widget = ModuleFactory.createWidget(element, moduleSocket);

				if (widget instanceof IInteractionModule)
					addModule((IInteractionModule)widget);
				
				// debug
				addModuleWidget(widget);
				
				
				widget.getElement().setId(Document.get().createUniqueId());
				String wid = widget.getElement().getId();
				idsIB.add(wid);
				
				
				return widget.getElement();
			}
		});
		setElement(dom);
		setStyleName("qp-text-module");
		this.sinkEvents(Event.ONCHANGE);
					    
	}
		
	protected void addModule(IInteractionModule newModule){
		
		modules.put(newModule.getInputsId(), newModule);

	}

	
	protected void addModuleWidget(Widget newModule){
		
		widgets.add(newModule);

	}
	
	// ------------------------- EVENTS --------------------------------
	
	/**
	 * Catch inner controls events.
	 * Since events are not fired for internal Widgets. All events should be handled in this function
	 */
	public void onBrowserEvent(Event event){
		super.onBrowserEvent(event);
		
		
		if(event.getTypeInt() == Event.ONCHANGE){
			com.google.gwt.dom.client.Element element = 
				com.google.gwt.dom.client.Element.as(event.getEventTarget());
			
			IBrowserEventListener handler = modules.getBySubkey(element.getId());
			if(handler != null){
				handler.onChange(event);
			}
		}
	}
	
	public int getModuleCount(){
		return modules.size();
	}
	
	@Override
	public void markAnswers() {
		for(IActivity currModule : modules.values())
			currModule.markAnswers();

	}

	@Override
	public void reset() {
		for(IActivity currModule : modules.values())
			currModule.reset();

	}

	@Override
	public void showCorrectAnswers() {
		for(IActivity currModule : modules.values())
			currModule.showCorrectAnswers();

	}

	@Override
	public Serializable getState() {
	    Vector<Serializable>  state = new Vector<Serializable>();
	    
	    for(IStateful currModule : modules.values()){
	        state.add( currModule.getState() );
	    }
	    
	    return state;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setState(Serializable newState) {

		if(newState instanceof Vector){
	    	Vector<Serializable> state = (Vector<Serializable>)newState;

	    	int index = 0;
	    	for(IStateful currModule : modules.values()){
	    		currModule.setState( state.elementAt(index) );
	    		index ++;
	    	}
		}

	}

}
