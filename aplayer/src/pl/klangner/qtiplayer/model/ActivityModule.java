package pl.klangner.qtiplayer.model;


public class ActivityModule extends Module{

	// --------------------------------------------------------------------------
	public String getPrompt(){
		return prompt;
	}
	
	// --------------------------------------------------------------------------
	protected void setPrompt(String text){
		
		this.prompt = text;
	}
	
	// --------------------------------------------------------------------------
	// Private members
	protected String prompt;
	
}
