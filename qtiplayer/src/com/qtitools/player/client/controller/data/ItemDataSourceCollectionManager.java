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
	
	public void setItemDataCollection(XMLData[] ds){
		items = new ItemDataSource[ds.length];
		for ( int i = 0 ; i < items.length ; i ++){
			items[i] = new ItemDataSource(ds[i]);
		}
		listener.onItemCollectionLoaded();
	}

	public ItemData getItemData(int index){
		return new ItemData(index, items[index].getItemData());
	}
	
	public String[] getTitlesList(){
		String[] titles = new String[items.length];
		for (int i = 0 ; i < items.length ; i ++){
			titles[i] = items[i].getTitle();
		}
		return titles;
	}
	
	public int getItemsCount(){
		return items.length;
	}
	
	public Vector<String> getStyleLinksForUserAgent(int itemIndex, String userAgent){
		return items[itemIndex].getStyleLinksForUserAgent(userAgent);
	}
}
