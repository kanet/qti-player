package com.qtitools.player.client.model;

import java.util.Vector;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.model.internalevents.InternalEvent;
import com.qtitools.player.client.model.internalevents.InternalEventHandlerInfo;
import com.qtitools.player.client.model.internalevents.InternalEventManager;
import com.qtitools.player.client.model.internalevents.InternalEventTrigger;
import com.qtitools.player.client.module.IActivity;
import com.qtitools.player.client.module.IBrowserEventHandler;
import com.qtitools.player.client.module.IInteractionModule;
import com.qtitools.player.client.module.IModuleEventsListener;
import com.qtitools.player.client.module.IUnattachedComponent;
import com.qtitools.player.client.module.ModuleSocket;
import com.qtitools.player.client.module.IStateful;
import com.qtitools.player.client.module.ModuleFactory;
import com.qtitools.player.client.module.ModuleStateChangedEventsListener;
import com.qtitools.player.client.module.mathexpr.MathJaxProcessor;
import com.qtitools.player.client.util.xml.XMLConverter;

public class ItemBody extends Widget implements IActivity, IStateful {

	public Vector<Widget> modules = new Vector<Widget>();
	
	public InternalEventManager eventManager;
	
	private JSONArray stateAsync;
	private boolean attached = false;
	private boolean locked = false;
	
	//private Label traceLabel;
	
	public ItemBody(Node itemBodyNode, ModuleSocket moduleSocket, final ModuleStateChangedEventsListener stateChangedListener){
		
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
			public void onStateChanged(boolean procesFeedback, IInteractionModule sender){
				stateChangedListener.onStateChanged(procesFeedback, sender);
			}
						
		};
		
				
		com.google.gwt.dom.client.Element dom = XMLConverter.getDOM((Element)itemBodyNode, moduleSocket, moduleEventsListener, new IModuleCreator() {
			
			@Override
			public boolean isSupported(String name) {
				return ModuleFactory.isSupported(name);
			}
			
			@Override
			public com.google.gwt.dom.client.Element createModule(Element element, ModuleSocket moduleSocket, IModuleEventsListener moduleEventsListener) {
				Widget widget = ModuleFactory.createWidget(element, moduleSocket, moduleEventsListener);

				if (widget instanceof IInteractionModule)
					addModule(widget);
				
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
		
	protected void addModule(Widget newModule){
		
		//modules.put(newModule.getInputsId(), newModule);
		
		modules.add(newModule);
		
		if (newModule instanceof IBrowserEventHandler){
		
			Vector<InternalEventTrigger> triggers = ((IBrowserEventHandler)newModule).getTriggers();
			
			if (triggers != null)
				for (InternalEventTrigger t : triggers)
					eventManager.register(new InternalEventHandlerInfo(((IBrowserEventHandler)newModule), t));
		}

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

		/*
		@SuppressWarnings("unused")
		String tmpId = element.getId();
		int evtType= event.getTypeInt();
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
		Vector<IBrowserEventHandler> handlers = eventManager.getHandlers(element.getId(), event.getTypeInt());
		
		if (handlers.size() > 0){
			int dasd = 343;
			dasd++;
		}
		
		for (IBrowserEventHandler m : handlers)
			m.handleEvent(element.getId(), event);
		
	}
	
	public native void alert(String s)/*-{
		alert(s);
	}-*/; 

	public void onAttach(){
		super.onAttach();
		if (modules.size() > 0)
			for (Widget mod : modules){
				if (mod instanceof IUnattachedComponent)
					((IUnattachedComponent)mod).onOwnerAttached();
			}

		attached = true;
		
		setState(stateAsync);
		
		if (locked)
			markAnswers(true);
		
		MathJaxProcessor.pushAll();
		
	}
	
	public void onLoad(){
		//MathJaxProcessor.process();
		//alert("onLoad - after MathJaxProcessor.process()");
	}

	public int getModuleCount(){
		return modules.size();
	}
	
	@Override
	public void markAnswers(boolean mark) {
		for(Widget currModule : modules){
			if (currModule instanceof IActivity)
				((IActivity)currModule).markAnswers(mark);
		}

	}
	
	@Override
	public void reset() {
		for(Widget currModule : modules){
			if (currModule instanceof IActivity)
				((IActivity)currModule).reset();
		}

	}

	@Override
	public void lock(boolean l) {
		locked = l;
		for(Widget currModule : modules){
			if (currModule instanceof IActivity)
				((IActivity)currModule).lock(l);
		}
		
	}
	
	public boolean isLocked(){
		return locked;
	}

	@Override
	public void showCorrectAnswers(boolean show) {
		for(Widget currModule : modules){
			if (currModule instanceof IActivity)
				((IActivity)currModule).showCorrectAnswers(show);
		}

	}

	@Override
	public JSONArray getState() {
		
		JSONArray states = new JSONArray();
			    
		for(Widget currModule : modules){
			if (currModule instanceof IStateful)
				states.set(states.size(), ((IStateful)currModule).getState());
			else
				states.set(states.size(), new JSONArray());
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
			    		if (modules.get(i) instanceof IStateful)
			    			((IStateful)modules.get(i)).setState(stateArr);
		    		}
		    	}
		    	
			}
		}

	}
	
	

}
