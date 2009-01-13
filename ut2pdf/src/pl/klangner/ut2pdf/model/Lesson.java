package pl.klangner.ut2pdf.model;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Lesson extends AbstractModel{

	// ----------------------------------------------------------------------------------------------
	public Lesson(){
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Get Lesson name from DOM 
	 */
	public String getTitle(){

		// <assessmentTest nextpageid='5' skinid='Blue' title='Lesson name'>
		return getAttribute(dom.getFirstChild(), "title");
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Get Number of pages 
	 */
	public int getPageCount(){

		// assessmentTest/testPart/assessmentSection/assessmentItemRef
		NodeList list = dom.getElementsByTagName("assessmentItemRef");
		return list.getLength();
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Get Page reference 
	 */
	public Page getPage(int index){

		Node  		node = dom.getElementsByTagName("assessmentItemRef").item(index);
		Page			page = new Page(getAttribute(node, "title"));
		
		try {
			page.load(folder + getAttribute(node, "href"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return page;
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Load UTT file
	 */
	public void load(String filename) throws IOException{

		super.load(filename);
		File file = new File(filename);
		folder = file.getParent() + "/";
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private String		folder;
}
