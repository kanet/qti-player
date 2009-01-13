package pl.klangner.ut2pdf.model;


public class Page extends AbstractModel{

	// ----------------------------------------------------------------------------------------------
	public Page(String name){
		title = name;
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Modules are the following subtags of <itemBody>
	 * 	* simpleText
	 *  * imagePlayer
	 *  * choiceInteraction
	 */
	public int countModules() {
		return -1;
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
