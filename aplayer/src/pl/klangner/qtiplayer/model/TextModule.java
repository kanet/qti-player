package pl.klangner.qtiplayer.model;


public class TextModule extends Module{

	// --------------------------------------------------------------------------
	public String getText(){
		return text;
	}
	
	// --------------------------------------------------------------------------
	public String toString(){
		
		return text;
	}
	
	// --------------------------------------------------------------------------
	protected void setText(String text){
		
		this.text = text;
	}
	
	// --------------------------------------------------------------------------
	// Private members
	private String text;
	
}
