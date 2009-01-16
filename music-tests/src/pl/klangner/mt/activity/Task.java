package pl.klangner.mt.activity;

public class Task {

	// ----------------------------------------------------------------------------------------------
	public Task(String q, String a){
		
		question = q;
		answer = a;
	}
	
	
	// ----------------------------------------------------------------------------------------------
	public String getQuestion(){
		return question;
	}

	// ----------------------------------------------------------------------------------------------
	public String getAnswer(){
		return answer;
	}
	
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private String question;
	private String answer;
}
