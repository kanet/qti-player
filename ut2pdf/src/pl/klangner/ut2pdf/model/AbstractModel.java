package pl.klangner.ut2pdf.model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

abstract class AbstractModel{

	// ----------------------------------------------------------------------------------------------
	/**
	 * Load XML file into DOM
	 */
	public void load(String filename) throws IOException{
		
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			//parse using builder to get DOM representation of the XML file
			File file = new File(filename);
			dom = db.parse(file);

		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}
	}
	
	// ----------------------------------------------------------------------------------------------
	protected String getAttribute(Node node, String name){
		return node.getAttributes().getNamedItem(name).getNodeValue();
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	protected Document 	dom;
}
