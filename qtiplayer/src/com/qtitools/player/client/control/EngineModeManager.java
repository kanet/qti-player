package com.qtitools.player.client.control;

public class EngineModeManager {

	private PlayerState state;
	
	EngineModeManager(){
		state = PlayerState.NONE;
	}
	
	public boolean canBeginAssessmentLoading(){
		return (state == PlayerState.NONE);
	}

	public void beginAssessmentLoading(){
		state = PlayerState.ASSESSMENT_LOADING;
	}

	public boolean canEndAssessmentLoading(){
		return (state == PlayerState.ASSESSMENT_LOADING);
	}
	
	public void endAssessmentLoading(){
		state = PlayerState.ASSESSMENT_LOADED;
	}

	public boolean canBeginItemLoading(){
		return (state == PlayerState.ASSESSMENT_LOADED  ||  
				state == PlayerState.RUNNING);
	}
	
	public void beginItemLoading(){
		state = PlayerState.ITEM_LOADING;
	}

	public boolean canEndItemLoading(){
		return (state == PlayerState.ITEM_LOADING);
	}
	
	public void endItemLoading(){
		state = PlayerState.ITEM_LOADED;
	}
	
	public boolean canRun(){
		return (state == PlayerState.ITEM_LOADED);
	}
	
	public void run(){
		state = PlayerState.RUNNING;
	}

	public boolean canFinish(){
		return (state == PlayerState.RUNNING);
	}
	
	public void finish(){
		state = PlayerState.FINISHED;
	}
	
	public boolean isAssessmentLoaded(){
		return (state == PlayerState.ASSESSMENT_LOADED  ||  
				state == PlayerState.ITEM_LOADING  ||
				state == PlayerState.ITEM_LOADED  ||
				state == PlayerState.FINISHED  ||
				state == PlayerState.RUNNING);
		
	}
	
	public boolean canNavigate(){
		return (state == PlayerState.RUNNING);
	}
	
	public String toString(){
		return state.toString();
	}
	
}
