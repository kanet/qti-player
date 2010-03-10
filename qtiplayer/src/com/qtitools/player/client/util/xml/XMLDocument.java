/*
  The MIT License
  
  Copyright (c) 2009 Krzysztof Langner
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
*/
package com.qtitools.player.client.util.xml;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.qtitools.player.client.control.IDocumentLoaded;

public class XMLDocument {

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
	public XMLDocument(String url, IDocumentLoaded l){

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
					try {
						if (response.getStatusCode() == 200 || response.getStatusCode() == 0) {
	
							dom = XMLParser.parse(response.getText());
							fix(dom, baseUrl);
							listener.finishedLoading(dom, baseUrl);
						} else {
							// Handle the error.  Can get the status text from response.getStatusText()
							errorString = "Wrong status: " + response.getText();
							listener.loadingErrorHandler(errorString);
						}
					} catch (Exception e) {
						listener.loadingErrorHandler(e.getMessage());
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
	  	listener.loadingErrorHandler(errorString);
	  
	  
	}

	private void fix(Document document, String baseUrl){

		fixLinks(document, baseUrl, "img", "src");
		fixLinks(document, baseUrl, "embed", "src");
		fixLinks(document, baseUrl, "sound", "src");
		fixLinks(document, baseUrl, "video", "src");
		fixLinks(document, baseUrl, "source", "src");
		fixLinks(document, baseUrl, "a", "href");
		fixLinks(document, baseUrl, "object", "data");
	}
	
	/**
	 * Fix links relative to xml file
	 * @param tagName tag name
	 * @param attrName attribute name with link
	 */
	private void fixLinks(Document document, String baseUrl, String tagName, String attrName){

		NodeList nodes = document.getElementsByTagName(tagName);

		for(int i = 0; i < nodes.getLength(); i++){
			Element element = (Element)nodes.item(i);
			String link = element.getAttribute(attrName);
			if(link != null && !link.startsWith("http")){
				element.setAttribute(attrName, baseUrl + link);
			}
			// Links open in new window
			if(tagName.compareTo("a") == 0){
				element.setAttribute( "target", "_blank");
			}
		}
	}
} 