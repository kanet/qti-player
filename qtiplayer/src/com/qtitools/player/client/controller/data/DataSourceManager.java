package com.qtitools.player.client.controller.data;

import java.util.Vector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.qtitools.player.client.controller.communication.ItemData;
import com.qtitools.player.client.controller.communication.PageData;
import com.qtitools.player.client.controller.communication.PageDataSummary;
import com.qtitools.player.client.controller.communication.PageDataTest;
import com.qtitools.player.client.controller.communication.PageReference;
import com.qtitools.player.client.controller.communication.PageDataToC;
import com.qtitools.player.client.controller.communication.PageType;
import com.qtitools.player.client.controller.data.events.AssessmentDataLoaderEventListener;
import com.qtitools.player.client.controller.data.events.DataLoaderEventListener;
import com.qtitools.player.client.controller.data.events.ItemDataCollectionLoaderEventListener;
import com.qtitools.player.client.util.xml.XMLDocument;
import com.qtitools.player.client.util.xml.document.IDocumentLoaded;
import com.qtitools.player.client.util.xml.document.XMLData;

public class DataSourceManager implements AssessmentDataLoaderEventListener, ItemDataCollectionLoaderEventListener {
	
	public DataSourceManager(DataLoaderEventListener l){
		mode = DataSourceManagerMode.NONE;
		listener = l;
		assessmentDataManager = new AssessmentDataSourceManager(this);
		itemDataCollectionManager = new ItemDataSourceCollectionManager(this);
	}
	
	private AssessmentDataSourceManager assessmentDataManager;
	private ItemDataSourceCollectionManager itemDataCollectionManager;
	private DataSourceManagerMode mode;
	private DataLoaderEventListener listener;
	
	public XMLData getAssessmentData(){
		return assessmentDataManager.getAssessmentData();
	}

	public ItemData getItemData(int index){
		return itemDataCollectionManager.getItemData(index);
	}
	
	public int getItemsCount(){
		return assessmentDataManager.getItemsCount();
	}
	
	public void loadAssessment(String url){
		
		if (mode == DataSourceManagerMode.LOADING_ASSESSMENT  ||  mode == DataSourceManagerMode.LOADING_ITEMS)
			return;
		
		mode = DataSourceManagerMode.LOADING_ASSESSMENT;

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
				assessmentDataManager.setAssessmentData(new XMLData(document, baseURL));
				loadItems();
			}

			@Override
			public void loadingErrorHandler(String error) {
				assessmentDataManager.setAssessmentLoadingError(error);
			}
		});
	}

	public void loadItems(){
		loadItems(assessmentDataManager.getItemUrls());		
	}
	
	private void loadItems(String[] urls){
		
		mode = DataSourceManagerMode.LOADING_ITEMS;
		
		itemDataCollectionManager.initItemDataCollection(urls.length);
		
		if (urls.length == 0)
			onItemCollectionLoaded();
		
		for (int i = 0 ; i < urls.length ; i ++){
			final int ii = i;
			
			new XMLDocument(urls[ii], new IDocumentLoaded(){

				public void finishedLoading(Document document, String baseURL) {
					itemDataCollectionManager.setItemData(ii, new XMLData(document, baseURL));
				}

				@Override
				public void loadingErrorHandler(String error) {
					itemDataCollectionManager.setItemData(ii, null);
				}
			});
		}
		
	}
	
	
	public void loadData(XMLData ad, XMLData[] ids){
		assessmentDataManager.setAssessmentData(ad);
		itemDataCollectionManager.setItemDataCollection(ids);
	}
	
	public PageData generatePageData(PageReference ref){
		PageData pd;
		if (ref.type == PageType.TOC){
			pd = new PageDataToC(itemDataCollectionManager.getTitlesList());
		} else if (ref.type == PageType.SUMMARY){
			pd = new PageDataSummary(itemDataCollectionManager.getTitlesList());
		} else {
			ItemData[] ids = new ItemData[ref.pageIndices.length];
			
			for (int i = 0 ; i < ref.pageIndices.length ; i ++){
				ids[i] = itemDataCollectionManager.getItemData(ref.pageIndices[i]);
			}
			
			pd = new PageDataTest(ids, ref.activityOptions);
		}
		
		return pd;
	}
	
	public Vector<String> getAssessmentStyleLinksForUserAgent(String userAgent){
		return assessmentDataManager.getStyleLinksForUserAgent(userAgent);
	}
	
	public Vector<String> getPageStyleLinksForUserAgent(PageReference ref, String userAgent){
		if (ref.pageIndices.length == 0)
			return new Vector<String>();
		
		return itemDataCollectionManager.getStyleLinksForUserAgent(ref.pageIndices[0], userAgent);
	}

	@Override
	public void onAssessmentDataLoaded() {
		
		listener.onAssessmentLoaded();
		
		mode = DataSourceManagerMode.SERVING;
		
	}
	

	@Override
	public void onItemCollectionLoaded() {
		mode = DataSourceManagerMode.SERVING;
		listener.onDataReady();
		
	}
	
	public DataSourceManagerMode getMode(){
		return mode;
	}
	
	public String getAssessmentTitle(){
		return assessmentDataManager.getAssessmentTitle();
	}
	
	public String getItemTitle(int index){
		return itemDataCollectionManager.getTitlesList()[index];
	}
}