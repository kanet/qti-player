package com.qtitools.player.client.control.session;

import com.qtitools.player.client.control.Result;

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
public interface IAssessmentSessionReport {
	
	public String getAssessmentTitle();
	public String getAssessmentItemTitle();
	public int getAssessmentItemsCount();
	public int getAssessmentItemsVisitedCount();
	public int getAssessmentItemIndex();
	public Result getAssessmentResult();
	public Result getItemResult();
	public int getAssessmentSessionTime();
	public boolean getAssessmentMasteryPassed();
	public int getAssessmentItemModulesCount();
}
