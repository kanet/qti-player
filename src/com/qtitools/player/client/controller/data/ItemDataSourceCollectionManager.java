package com.qtitools.player.client.controller.data;

import java.util.Vector;

import com.qtitools.player.client.controller.communication.ItemData;
import com.qtitools.player.client.controller.data.events.ItemDataCollectionLoaderEventListener;
import com.qtitools.player.client.util.xml.document.XMLData;

public class ItemDataSourceCollectionManager {
	public ItemDataSourceCollectionManager(ItemDataCollectionLoaderEventListener l){
		listener = l;
	}
	
	private ItemDataSource[] items;
	private ItemDataCollectionLoaderEventListener listener;
	private int itemsLoadCounter;
	
	public void initItemDataCollection(int itemsCount){
		items = new ItemDataSource[itemsCount];
		itemsLoadCounter = 0;
	}
	
	public void setItemData(int index, XMLData d){
		items[index] = new ItemDataSource(d);
		itemsLoadCounter++;
		if (itemsLoadCounter == items.length)
			listener.onItemCollectionLoaded();
	}

	public void setItemData(int index, String error){
		items[index] = new ItemDataSource(error);
		itemsLoadCounter++;
		if (itemsLoadCounter == items.length)
			listener.onItemCollectionLoaded();
	}
	
	public void setItemDataCollection(XMLData[] ds){
		items = new ItemDataSource[ds.length];
		for ( int i = 0 ; i < items.length ; i ++){
			items[i] = new ItemDataSource(ds[i]);
		}
		listener.onItemCollectionLoaded();
	}

	public ItemData getItemData(int index){
		if (!items[index].isError())
			return new ItemData(index, items[index].getItemData());
		else
			return new ItemData(index, items[index].getErrorMessage());
	}
	
	public String[] getTitlesList(){
		String[] titles = new String[items.length];
		for (int i = 0 ; i < items.length ; i ++){
			titles[i] = items[i].getTitle();
		}
		return titles;
	}
	
	public int getItemsCount(){
		if (items != null)
			return items.length;
		else
			return 0;
	}
	
	public Vector<String> getStyleLinksForUserAgent(int itemIndex, String userAgent){
		if (items != null && itemIndex<=items.length)
			return items[itemIndex].getStyleLinksForUserAgent(userAgent);
		return new Vector<String>();
	}
}
