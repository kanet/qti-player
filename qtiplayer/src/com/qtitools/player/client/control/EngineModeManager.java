package com.qtitools.player.client.control;

public class EngineModeManager {

	private EngineMode state;
	
	EngineModeManager(){
		state = EngineMode.NONE;
	}
	
	public boolean canBeginAssessmentLoading(){
		return (state == EngineMode.NONE);
	}

	public void beginAssessmentLoading(){
		state = EngineMode.ASSESSMENT_LOADING;
	}

	public boolean canEndAssessmentLoading(){
		return (state == EngineMode.ASSESSMENT_LOADING);
	}
	
	public void endAssessmentLoading(){
		state = EngineMode.ASSESSMENT_LOADED;
	}

	public boolean canBeginItemLoading(){
		return (state == EngineMode.ASSESSMENT_LOADED  ||  
				state == EngineMode.RUNNING);
	}
	
	public void beginItemLoading(){
		state = EngineMode.ITEM_LOADING;
	}

	public boolean canEndItemLoading(){
		return (state == EngineMode.ITEM_LOADING);
	}
	
	public void endItemLoading(){
		state = EngineMode.ITEM_LOADED;
	}
	
	public boolean canRun(){
		return (state == EngineMode.ITEM_LOADED);
	}
	
	public void run(){
		state = EngineMode.RUNNING;
	}

	public boolean canFinish(){
		return (state == EngineMode.RUNNING);
	}
	
	public void finish(){
		state = EngineMode.FINISHED;
	}
	
	public boolean isAssessmentLoaded(){
		return (state == EngineMode.ASSESSMENT_LOADED  ||  
				state == EngineMode.ITEM_LOADING  ||
				state == EngineMode.ITEM_LOADED  ||
				state == EngineMode.FINISHED  ||
				state == EngineMode.RUNNING);
		
	}
	
	public boolean canNavigate(){
		return (state == EngineMode.RUNNING);
	}
	
	public String toString(){
		return state.toString();
	}
	
}
