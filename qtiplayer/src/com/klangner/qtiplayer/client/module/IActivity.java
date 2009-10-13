package com.klangner.qtiplayer.client.module;

/**
 * Interface for widgets implementing interactive module functions
 * @author Krzysztof Langner
 */
public interface IActivity {

	/** Reset view */
	public void reset();
	
	/** Mark wrong and show correct answers */
	public void markAnswers();
	
	/** Show correct answers */
	public void showCorrectAnswers();
}
