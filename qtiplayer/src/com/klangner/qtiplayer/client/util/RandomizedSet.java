package com.klangner.qtiplayer.client.util;

import java.util.Vector;

import com.google.gwt.user.client.Random;

public class RandomizedSet<T> {

	private Vector<T>	elements = new Vector<T>();
	
	/**
	 * Add element do the set
	 * @param item
	 */
	public void push(T item){
		elements.add(item);
	}
	
	/**
	 * check if there are more elements
	 * @return
	 */
	public boolean hasMore(){
		return (elements.size() > 0);
	}
	
	/**
	 * return element from the set. element will be removed
	 */
	public T pull(){
		if(elements.size() == 0)
			return null;
		
		int index = Random.nextInt(elements.size());
		T item = elements.get(index);
		elements.remove(index);

		return item;
	}
	
}
