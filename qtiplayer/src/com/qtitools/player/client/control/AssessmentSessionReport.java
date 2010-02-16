package com.qtitools.player.client.control;

import java.util.Vector;

/**
 * The report about current assessment state. 
 * 
 * AssessmentSessionReport should be delivered to Player 
 * and PlayerWidget as the source of information about
 * assessment state 
 * 
 * @author Rafal Rybacki
 *
 */
public class AssessmentSessionReport {

	private Vector<String> assessmentItemTitles;
	
	private int assessmentItemsCount;
	
	private int assessmentItemsDone;
	
	private int currentAssessmentItemsIndex;
	
	private Result assessmentResult;
	
	public AssessmentSessionReport(Vector<String> titles, int currentItem, int itemsCount, int itemsDone, Result result){
		assessmentItemTitles = titles;
		currentAssessmentItemsIndex = currentItem;
		assessmentItemsCount = itemsCount;
		assessmentItemsDone = itemsDone;
		assessmentResult = result;
	}
	
	public Vector<String> getTitles(){
		return assessmentItemTitles;
	}

	public String getCurrentAssessmentItemTitle(){
		return assessmentItemTitles.get(currentAssessmentItemsIndex);
	}
	
	public int getCurrentAssessmentItemIndex(){
		return currentAssessmentItemsIndex;
	}

	public int getAssessmentItemsCount(){
		return assessmentItemsCount;
	}

	public int getAssessmentItemsDone(){
		return assessmentItemsDone;
	}
	
	public Result getAssessmentResult(){
		return assessmentResult;
	}
	
	
	
}
