package com.qtitools.player.client.control;

public interface DeliveryEngineEventListener {
	
	void onItemSessionBegin(int currentAssessmentItemIndex);
	
	void onItemSessionFinished(int currentAssessmentItemIndex);

	void onAssessmentSessionFinished();
	
	void onAssessmentSessionBegin();

	void onAssessmentLoadingError(String errorMessage);

	void onAssessmentItemLoadingError(String errorMessage);
}
