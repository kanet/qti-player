package com.qtitools.player.client.model;

import java.util.Vector;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventHandlerInfo;
import com.qtitools.player.client.model.internalevents.InternalEventManager;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IModuleSocket;
import com.qtitools.player.client.module.IStateChangedListener;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.ModuleFactory;
import com.qtitools.player.client.util.xml.XMLConverter;

public class ItemBody extends Widget implements IActivity, IStateful {

	// debug
	public Vector<Widget>	widgets = new Vector<Widget>();
	
	// debug
	//public Vector<String> idsIB = new Vector<String>();
	
	public Vector<IInteractionModule> modules = new Vector<IInteractionModule>();
	
	public InternalEventManager eventManager;
	
	private JSONArray stateAsync;
	private boolean attached;
	
	//private Label traceLabel;
	
	public ItemBody(Node itemBodyNode, IModuleSocket moduleSocket, final IStateChangedListener stateChangedListener){
		
		attached = false;
		
		eventManager = new InternalEventManager();
		
		IModuleEventsListener moduleEventsListener = new IModuleEventsListener() {
			
			@Override
			public void onTouchStart(com.google.gwt.dom.client.Element target,
					int pageX, int pageY) {
				processEvent(new InternalEvent(target, Event.ONMOUSEDOWN, pageX, pageY));
				
			}
			
			@Override
			public void onTouchMove(com.google.gwt.dom.client.Element target,
					int pageX, int pageY) {
				processEvent(new InternalEvent(target, Event.ONMOUSEMOVE, pageX, pageY));
				
			}
			
			@Override
			public void onTouchEnd(com.google.gwt.dom.client.Element target) {
				processEvent(new InternalEvent(target, Event.ONMOUSEUP));
				
			}
			
			@Override
			public void onStateChanged() {
				stateChangedListener.onStateChanged();
				
			}
		};
		
				
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM((Element)itemBodyNode, moduleSocket, moduleEventsListener, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return ModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element, IModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener) {
				Widget widget = ModuleFactory.createWidget(element, moduleSocket, moduleEventsListener);

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
		
		//traceLabel = new Label();
		//dom.appendChild(traceLabel.getElement());
		
		setElement(dom);
		setStyleName("qp-text-module");
		this.sinkEvents(Event.ONCHANGE);
		this.sinkEvents(Event.ONMOUSEDOWN);
		this.sinkEvents(Event.ONMOUSEUP);
		this.sinkEvents(Event.ONMOUSEMOVE);
		this.sinkEvents(Event.ONMOUSEOUT);
		
		
		

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

		processEvent(new InternalEvent(event));

	}
	
	public void processEvent(InternalEvent event){
		
		com.google.gwt.dom.client.Element element = event.getEventTargetElement();
		
		@SuppressWarnings("unused")
		String tmpId = element.getId();
		int evtType= event.getTypeInt();
/*
		if (tmpId.length() > 1)
			traceLabel.setText(tmpId);
		else
			traceLabel.setText("---");
		
		if (evtType == Event.ONMOUSEDOWN){
			traceLabel.setText(traceLabel.getText() + " DOWN");
		} else if (evtType == Event.ONMOUSEUP){
			traceLabel.setText(traceLabel.getText() + " UP");
		} else if (evtType == Event.ONMOUSEMOVE){
			traceLabel.setText(traceLabel.getText() + " MOVE");
		}
*/		
		Vector<IInteractionModule> handlers = eventManager.getHandlers(element.getId(), event.getTypeInt());
		
		if (handlers.size() > 0){
			int dasd = 343;
			dasd++;
		}
		
		for (IInteractionModule m : handlers)
			m.handleEvent(element.getId(), event);
		
	}
	
	public native void alert(String s)/*-{
		alert(s);
	}-*/; 

	public void onAttach(){
		super.onAttach();
		if (modules.size() > 0)
			for (IInteractionModule mod : modules)
				mod.onOwnerAttached();

		attached = true;
		
		setState(stateAsync);
		
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
	public void unmark() {
		for(IActivity currModule : modules)
			currModule.unmark();
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
	public JSONArray getState() {
		
		JSONArray states = new JSONArray();
			    
	    for(IStateful currModule : modules){
	    	states.set(states.size(), currModule.getState());
	    }
	    
	    return states;
	}

	@Override
	public void setState(JSONArray newState){
		if (!attached){
			stateAsync = newState;
		} else {
			if(newState instanceof JSONArray){
	
		    	for (int i = 0 ; i < newState.size()  &&  i < modules.size() ; i ++){
			    	if (newState.get(i) != null){
			    		JSONArray stateArr = newState.get(i).isArray();
			    		modules.get(i).setState(stateArr);
		    		}
		    	}
		    	
			}
		}

	}
	
	

}
