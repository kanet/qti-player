package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.Assessment;

public interface ContentManager {

	// Standard pages
	public static final int ABOUT_PAGE = -1;
	public static final int SUMMARY_PAGE = -2;
	
	/** @return assessment */
	public Assessment getAssessment();
	/** @return index of current page */
	public int getCurrentPage();
	/** Switch to given page */
	public void goToPage(int page);
}
