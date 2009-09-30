package com.klangner.qtiplayer.client.model;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
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

	/**
	 * Create assessment from xml
	 */
	public void load(String url, IDocumentLoaded l){

		listener = l;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
		baseUrl = url.substring(0, url.lastIndexOf("/")+1);

		try {
		  builder.sendRequest(null, new RequestCallback() {
		    public void onError(Request request, Throwable exception) {
		       // Couldn't connect to server (could be timeout, SOP violation, etc.)    
		    	Window.alert("Error" + exception.toString());
		    }

		    public void onResponseReceived(Request request, Response response) {
		      if (200 == response.getStatusCode()) {

		      	Window.alert("OK");
		  			dom = XMLParser.parse(response.getText());
		  			initData();
		      	listener.finishedLoading();
		      } else {
		        // Handle the error.  Can get the status text from response.getStatusText()
		      	Window.alert("Wrong status: " + response.getStatusCode() + " " + response.getStatusText());
		      }
		    }       
		  });
		  
		} catch (RequestException e) {
		  // Couldn't connect to server    
			Window.alert("Can't connect to the server: " + e.toString());
	  } catch (DOMException e) {
	    Window.alert("Could not parse file: " + url);
	  }

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
