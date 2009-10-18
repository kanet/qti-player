package com.klangner.qtiplayer.client.model;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

abstract class AbstractXMLDocument {

	/** DOM with assessment data */
	private Document 	dom;
	/** url to this document */
	private String		baseUrl;
	/** Array with references to items */
	private IDocumentLoaded listener;
	/** Error string */
	private String	errorString;

	/**
	 * Create assessment from xml
	 */
	public void load(String url, IDocumentLoaded l) throws LoadException{

		listener = l;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		baseUrl = url.substring(0, url.lastIndexOf("/")+1);

		errorString = null;
		try {
		  builder.sendRequest(null, new RequestCallback() {
		    public void onError(Request request, Throwable exception) {
		       // Couldn't connect to server (could be timeout, SOP violation, etc.)    
		    	errorString = "Error" + exception.toString();
		    }

		    public void onResponseReceived(Request request, Response response){
		    	// StatusCode == 0 when loading from local file
		      if (response.getStatusCode() == 200 || response.getStatusCode() == 0) {

		  			dom = XMLParser.parse(response.getText());
		  			initData();
		      	listener.finishedLoading();
		      } else {
		        // Handle the error.  Can get the status text from response.getStatusText()
		      	errorString = "Wrong status: " + response.getText();
		      }
		    }       
		  });
		  
		} catch (RequestException e) {
		  // Couldn't connect to server    
			errorString = "Can't connect to the server: " + e.toString();
	  } catch (DOMException e) {
	  	errorString = "Could not parse file: " + url;
	  }

	  if(errorString != null)
	  	throw new LoadException(errorString);
	}
	
	protected Document getDom(){
		return dom;
	}
	
	protected String getBaseUrl(){
		return baseUrl;
	}
	
	protected void initData(){
		
	}
	
}
