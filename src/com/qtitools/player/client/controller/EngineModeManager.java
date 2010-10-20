package com.qtitools.player.client.controller;

public class EngineModeManager {

	private EngineMode state;
	
	private boolean finished = false;
	
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
				state == EngineMode.RUNNING  ||  
				state == EngineMode.FINISHED  ||  
				state == EngineMode.PREVIEW);
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
		return (state == EngineMode.ITEM_LOADED  &&  !finished);
	}
	
	public void run(){
		state = EngineMode.RUNNING;
	}
	
	public boolean canPreview(){
		return (state == EngineMode.ITEM_LOADED  &&  finished);
	}
	
	public void preview(){
		state = EngineMode.PREVIEW;
	}

	
	public boolean canFinish(){
		return (state == EngineMode.RUNNING  &&  !finished);
	}

	public void finish(){
		state = EngineMode.FINISHED;
		finished = true;
	}

	public boolean canSummary(){
		return (state == EngineMode.PREVIEW  &&  finished);
	}
	
	public void summary(){
		state = EngineMode.FINISHED;
	}

	public boolean canContinueAssessment(){
		return (state == EngineMode.FINISHED);
	}
	
	public void continueAssessment(){
		finished = false;
	}
	
	public boolean isAssessmentLoaded(){
		return (state == EngineMode.ASSESSMENT_LOADED  ||  
				state == EngineMode.ITEM_LOADING  ||
				state == EngineMode.ITEM_LOADED  ||
				state == EngineMode.FINISHED  ||
				state == EngineMode.RUNNING);
		
	}
	
	public boolean canNavigate(){
		return (state == EngineMode.RUNNING  ||  state == EngineMode.FINISHED  ||  state == EngineMode.PREVIEW);
	}
	
	public String toString(){
		return state.toString();
	}
	
}
