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

public class TableOfContent {

	// --------------------------------------------------------------------------
	public TableOfContent(String url){
		
		load(url);
	}
	
	
	// --------------------------------------------------------------------------
	public List<TocItem> getItems(){
		return items;
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

			items = new ArrayList<TocItem>();
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

		public void startElement (String uri, String name, String qName, Attributes atts){
			if (name.compareTo("resource") == 0){
				item = new TocItem(atts.getValue("identifier"), atts.getValue("href"));
				items.add(item);
			} 
			else if (name.compareTo("title") == 0){
				title = true;
			}
			else if (name.compareTo("langstring") == 0){
				langstring = new String();
			}
		}
		
		public void characters (char ch[], int start, int length){

			if(langstring != null){
				langstring = langstring + new String(ch, start, length);
			}
		}

		public void endElement (String uri, String name, String qName){
			if (name.compareTo("resource") == 0){
				item = null;
			}
			else if (name.compareTo("title") == 0){
				title = false;
			}
			else if (name.compareTo("langstring") == 0){
				if(item != null && title){
					item.setTitle(langstring);
				}
				
				langstring = null;
			}
		}
		
		private TocItem	item = null;
		private boolean	title = false;
		private String	langstring = null;
	};
	
	// --------------------------------------------------------------------------
	// Private members
	private List<TocItem>	items;
	
}
