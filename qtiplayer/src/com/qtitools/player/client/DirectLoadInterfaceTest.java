package com.qtitools.player.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.qtitools.player.client.controller.communication.FlowOptions;
import com.qtitools.player.client.controller.communication.PageItemsDisplayMode;
import com.qtitools.player.client.util.xml.XMLDocument;
import com.qtitools.player.client.util.xml.document.IDocumentLoaded;
import com.qtitools.player.client.util.xml.document.XMLData;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DirectLoadInterfaceTest implements EntryPoint {

	private XMLData assessmentData;
	private XMLData[] itemDatas;
	private int itemLoadingCounter = 0;;
	
	public void onModuleLoad() {
		loadAssessment("content/test.xml");
	}
	
	public void loadAssessment(String url){
		
		String resolvedURL;
		
		if( GWT.getHostPageBaseURL().startsWith("file://") ){

			String localURL = URL.decode( GWT.getHostPageBaseURL() );
			resolvedURL = localURL + url;
		}
		else if( url.contains("://") || url.startsWith("/") ){
			resolvedURL = url;
		}
		else{
			resolvedURL = GWT.getHostPageBaseURL() + url;
		}

		new com.qtitools.player.client.util.xml.XMLDocument(resolvedURL, new IDocumentLoaded(){

			public void finishedLoading(Document document, String baseURL) {
				assessmentData = new XMLData(document, baseURL);
				loadItems();
			}

			@Override
			public void loadingErrorHandler(String error) {
			}
		});
	}
	
	public String[] getItemUrls(){
		
		String[] itemUrls = new String[0];
		
		if (assessmentData != null){
			try {
				NodeList nodes = assessmentData.getDocument().getElementsByTagName("assessmentItemRef");
				String[] tmpItemUrls = new String[nodes.getLength()];
				for(int i = 0; i < nodes.getLength(); i++){
					Node itemRefNode = nodes.item(i);
					tmpItemUrls[i] = assessmentData.getBaseURL() + ((Element)itemRefNode).getAttribute("href");
			    }
				itemUrls = tmpItemUrls;
			} catch (Exception e) {	}
		}
		
		//TODO
		return itemUrls;
	}
	
	public void loadItems(){
		String[] urls = getItemUrls();
		
		itemDatas = new XMLData[urls.length];
		
		for (int i = 0 ; i < urls.length ; i ++){
			final int ii = i;
			
			new XMLDocument(urls[ii], new IDocumentLoaded(){

				public void finishedLoading(Document document, String baseURL) {
					itemDatas[ii] = new XMLData(document, baseURL);
					onItemLoaded();
				}

				@Override
				public void loadingErrorHandler(String error) {
				}
			});
		}
	}
	
	public void onItemLoaded(){
		itemLoadingCounter++;
		if (itemLoadingCounter == itemDatas.length){
			createPlayer();
		}
	}
	
	/**
	 * Having all the XML documents loaded (assessment & items)
	 * and put into XMLData structure the player should be
	 * loaded as follows. 
	 */
	public void createPlayer(){
		Player p = new Player("root");
		// optionally - flow options could be set
		p.setFlowOptions(new FlowOptions(true, true, PageItemsDisplayMode.ONE, true));
		p.load(assessmentData, itemDatas);
	}
}
