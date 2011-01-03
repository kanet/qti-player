package com.qtitools.player.client.controller.communication;


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
public interface IAssessmentReport {
	
	public String getAssessmentTitle();
	public String getCurrentItemTitle();
	public int getItemsCount();
	public int getItemsVisitedCount();
	public int getCurrentItemIndex();
	public Result getAssessmentResult();
	public Result getCurrentItemResult();
	public int getAssessmentSessionTime();
	public boolean getAssessmentMasteryPassed();
	public int getCurrentItemModulesCount();
	public Result[] getItemsResults();
	public int[] getItemsMistakes();
	public int[] getItemsChecks();
	public int[] getItemsTimes();
}
