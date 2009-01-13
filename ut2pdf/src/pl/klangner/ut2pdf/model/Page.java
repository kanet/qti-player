package pl.klangner.ut2pdf.model;


public class Page extends AbstractModel{

	// ----------------------------------------------------------------------------------------------
	public Page(String name){
		title = name;
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Get Lesson name from DOM 
	 */
	public String getTitle(){
		return title;
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private String	title;
}
