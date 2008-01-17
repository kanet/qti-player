package pl.klangner.qtiplayer.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Assessment {

	// --------------------------------------------------------------------------
	public Assessment(String url){
		
		load(url);
	}
	
	
	// --------------------------------------------------------------------------
	public String getDescription() {
		return description;
	}
	
	// --------------------------------------------------------------------------
	public List<Item> getItems(){
		return items;
	}
	
	// --------------------------------------------------------------------------
	public String getTitle() {
		return title;
	}
	
	// --------------------------------------------------------------------------
	/**
	 * Load TOC from imsmanifest.sml file
	 * @param url - URL location of manifest (without imsmanifest.xml name)
	 */
	private void load(String url){
		
  	HttpClient 			client = new HttpClient();
  	HttpMethod 			method = new GetMethod(url + "/imsmanifest.xml");
		
		try {

			items = new ArrayList<Item>();
      int statusCode = client.executeMethod(method);

      if (statusCode == HttpStatus.SC_OK) {
      	SAXParserFactory 	factory = SAXParserFactory.newInstance();
      	factory.setNamespaceAware(true);
      	SAXParser					parser = factory.newSAXParser(); 

      	parser.parse(method.getResponseBodyAsStream(), manifestHandler);
      }
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
	
	// --------------------------------------------------------------------------
	// SAX parser for manifest file
	private DefaultHandler manifestHandler  = new DefaultHandler(){

		// ------------------------------------------
		public void startElement (String uri, String name, String qName, Attributes atts){
			
			path = path + "/" + name;
			if (name.compareTo("resource") == 0){
				item = new Item(atts.getValue("identifier"), atts.getValue("href"));
				items.add(item);
			} 
			else if (name.compareTo("langstring") == 0){
				langstring = new String();
			}
		}
		
		// ------------------------------------------
		public void characters (char ch[], int start, int length){

			if(langstring != null){
				langstring = langstring + new String(ch, start, length);
			}
		}

		// ------------------------------------------
		public void endElement (String uri, String name, String qName){
			
			path = path.substring(0, path.lastIndexOf('/'));
			if (name.compareTo("resource") == 0){
				item = null;
			}
			else if (name.compareTo("langstring") == 0){
				if(ASSESSMENT_TITLE.compareTo(path) == 0){
					title = langstring;
				}else if(ASSESSMENT_DESC.compareTo(path) == 0){
					description = langstring;
				}else if(RESOURCE_TITLE.compareTo(path) == 0){
					item.setTitle(langstring);
				}else if(RESOURCE_DESC.compareTo(path) == 0){
					item.setDescritpion(langstring);
				}

				langstring = null;
			}
		}
		
		private Item		item = null;
		private String	path = "";
		private String	langstring = null;
		private static final String ASSESSMENT_TITLE = "/manifest/metadata/lom/general/title";
		private static final String ASSESSMENT_DESC = "/manifest/metadata/lom/general/description";
		private static final String RESOURCE_TITLE = "/manifest/resources/resource/metadata/lom/general/title";
		private static final String RESOURCE_DESC = "/manifest/resources/resource/metadata/lom/general/description";
	};
	
	// --------------------------------------------------------------------------
	// Private members
	private String 			description;
	private List<Item>	items;
	private String 			title;

}
