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


public class AssessmentItem {

	// --------------------------------------------------------------------------
	protected AssessmentItem(String url, String id, String href){
		
		this.id = id;
		this.title = id;
		this.href = href;
		this.url = url;
	}
	
	// --------------------------------------------------------------------------
	public String getDescription(){
		return description;
	}
	
	// --------------------------------------------------------------------------
	public String getHref(){
		return href;
	}
	
	// --------------------------------------------------------------------------
	public String getId(){
		return id;
	}
	
	// --------------------------------------------------------------------------
	public List<Module> getModules(){
		if(!is_loaded)
			load();
		
		return modules;
	}
	
	// --------------------------------------------------------------------------
	public String getTitle(){
		return title;
	}
	
	// --------------------------------------------------------------------------
	public String toString(){
		return title;
	}
	
	// --------------------------------------------------------------------------
	protected void setDescritpion(String desc) {
		this.description = desc;
	}

	// --------------------------------------------------------------------------
	protected void setTitle(String new_title){
		this.title = new_title;
	}
	
	
	// --------------------------------------------------------------------------
	private void load() {
		
		
  	HttpClient 			client = new HttpClient();
  	HttpMethod 			method = new GetMethod(url + "/" + href);
		
		try {
			modules = new ArrayList<Module>();
      int statusCode = client.executeMethod(method);

      if (statusCode == HttpStatus.SC_OK) {
      	SAXParserFactory 	factory = SAXParserFactory.newInstance();
      	factory.setNamespaceAware(true);
      	SAXParser	parser = factory.newSAXParser(); 

      	parser.parse(method.getResponseBodyAsStream(), handler);
    		is_loaded = true;
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
	private DefaultHandler handler  = new DefaultHandler(){

		// ------------------------------------------
		public void startElement (String uri, String name, String qName, Attributes atts){
			
			chars = new String();
			if(name.compareTo("p") == 0){
				setModule(new TextModule());
			}
			else if(name.compareTo("choiceInteraction") == 0){
				setModule(new ChoiceModule());
			}
		}
		
		// ------------------------------------------
		public void characters (char ch[], int start, int length){
			chars = chars + new String(ch, start, length);
		}

		// ------------------------------------------
		public void endElement (String uri, String name, String qName){
			
			if(name.compareTo("p") == 0 && module instanceof TextModule) {
				((TextModule)module).setText(chars);
			}
			else if(name.compareTo("prompt") == 0 && module instanceof ActivityModule) {
				((ActivityModule)module).setPrompt(chars);
			}
		}
		
		// ------------------------------------------
		private void setModule(Module m){
			module = m;
			modules.add(module);
		}
		
		private String	chars = null;
		private Module	module = null;
	};
	
	// --------------------------------------------------------------------------
	// Private members
	private String 				description;
	private String 				href;
	private String 				id;
	private boolean				is_loaded = false;
	private List<Module>	modules = new ArrayList<Module>();
	private String				url;
	private String 				title;
	
}
