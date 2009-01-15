package pl.klangner.mt.activity;

import java.util.List;

public interface Activity {

	/** Get instruction for this type of activity */
	public String getInstruction();
	
	/** Generate count number of random questions  */
	public List<Question> getQuestions(int count);
	
}
