package com.qtitools.player.client.model;

import java.io.Serializable;
import java.util.Vector;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.model.IModuleCreator;
import com.qtitools.player.client.model.internalevents.InternalEventHandlerInfo;
import com.qtitools.player.client.model.internalevents.InternalEventManager;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.ModuleFactory;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.test.TestModule;
import com.qtitools.player.client.util.xml.XMLConverter;

public class ItemBody extends Widget implements IActivity, IStateful {

	//public MultikeyHashMap<Vector<String>, IInteractionModule, String>	modules = new MultikeyHashMap<Vector<String>, IInteractionModule, String>();
	
	// debug
	public Vector<Widget>	widgets = new Vector<Widget>();
	
	// debug
	//public Vector<String> idsIB = new Vector<String>();
	
	public Vector<IInteractionModule> modules = new Vector<IInteractionModule>();
	
	public InternalEventManager eventManager;
	
	public ItemBody(Node itemBodyNode, IModuleSocket moduleSocket){
		
		eventManager = new InternalEventManager();
				
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
				//addModuleWidget(widget);
				
				
				// store widgets tag id
				//widget.getElement().setId(Document.get().createUniqueId());
				//String wid = widget.getElement().getId();
				//idsIB.add(wid);
				
				
				return widget.getElement();
			}
		});
		setElement(dom);
		setStyleName("qp-text-module");
		this.sinkEvents(Event.ONCHANGE);
		this.sinkEvents(Event.ONMOUSEDOWN);
		this.sinkEvents(Event.ONMOUSEUP);
		this.sinkEvents(Event.ONMOUSEMOVE);
		this.sinkEvents(Event.ONMOUSEOUT);
					    
		addDomHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				int asd = 3;
				asd++;
				
			}
		}, MouseMoveEvent.getType());
	}
		
	protected void addModule(IInteractionModule newModule){
		
		//modules.put(newModule.getInputsId(), newModule);
		
		modules.add(newModule);
		
		Vector<InternalEventTrigger> triggers = newModule.getTriggers();
		
		if (triggers != null)
			for (InternalEventTrigger t : triggers)
				eventManager.register(new InternalEventHandlerInfo(newModule, t));

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

		com.google.gwt.dom.client.Element element = 
			com.google.gwt.dom.client.Element.as(event.getEventTarget());

		Vector<IInteractionModule> handlers = eventManager.getHandlers(element.getId(), event.getTypeInt());
		
		for (IInteractionModule m : handlers)
			m.handleEvent(element.getId(), event);
		
		if (event.getTypeInt() == Event.ONMOUSEDOWN){
			int asd = 12;
			asd ++;
		}
		
		if (event.getTypeInt() == Event.ONLOAD){
			int asd = 23;
			asd ++;
		}
		/*
		if(event.getTypeInt() == Event.ONCHANGE){
			com.google.gwt.dom.client.Element element = 
				com.google.gwt.dom.client.Element.as(event.getEventTarget());
			
			//IBrowserEventListener handler = modules.getBySubkey(element.getId());
			
			IInteractionModule handler = eventManager.getHandler(element.getId(), event.getTypeInt());
			
			if(handler != null){
				handler.handleEvent(element.getId(), event.getTypeInt(), event);
			}
		} else if (event.getTypeInt() == Event.ONMOUSEMOVE){
			com.google.gwt.dom.client.Element element = 
				com.google.gwt.dom.client.Element.as(event.getEventTarget());
			
			String id5 = element.getId();
			
			id5 += " ";
			
		}
		*/
		
	}

	public void onAttach(){
		super.onAttach();
		if (modules.size() > 1)
			for (IInteractionModule mod : modules)
				mod.onOwnerAttached();
	}
	
	public void shown(){
		//if (modules.size() > 1)
			//if (modules.get(1) instanceof TestModule)
				//((TestModule)modules.get(1)).load();
	}
	
	public int getModuleCount(){
		return modules.size();
	}
	
	@Override
	public void markAnswers() {
		for(IActivity currModule : modules)
			currModule.markAnswers();

	}

	@Override
	public void reset() {
		for(IActivity currModule : modules)
			currModule.reset();

	}

	@Override
	public void showCorrectAnswers() {
		for(IActivity currModule : modules)
			currModule.showCorrectAnswers();

	}

	@Override
	public Serializable getState() {
	    Vector<Serializable>  state = new Vector<Serializable>();
	    
	    for(IStateful currModule : modules){
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
	    	for(IStateful currModule : modules){
	    		currModule.setState( state.elementAt(index) );
	    		index ++;
	    	}
		}

	}

}
