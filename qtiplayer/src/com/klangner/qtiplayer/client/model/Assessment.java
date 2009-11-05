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
package com.klangner.qtiplayer.client.model;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class Assessment extends XMLDocument{

	/** Array with references to items */
	private Vector<String>   itemRefs;
  /** Array with item titles */
  private Vector<String>  itemTitles;
  /** Varaibles used to load titles */
  private int             titlesLoadedCounter;
  private IDocumentLoaded titlesListener;

	/**
	 * @return number of items in assessment
	 */
	public int getItemCount(){
		return itemRefs.size();
	}
	
  /**
   * @return item ref
   */
  public String getItemRef(int index){
    return itemRefs.get(index);
  }
  
  /**
   * @return item title
   */
  public String getItemTitle(int index){
    return itemTitles.get(index);
  }
  
	/**
	 * @return assessment title
	 */
	public String getTitle(){

    Node rootNode = getDom().getElementsByTagName("assessmentTest").item(0);
    String title = ((Element)rootNode).getAttribute("title");
    
    return title;
	}
	
	/**
	 * Load titles from item XML files
	 */
	public void loadTitles(IDocumentLoaded listener){
	  
	  // Load only once
	  if(itemTitles == null){
	    itemTitles = new Vector<String>();
	    
      try {
        titlesLoadedCounter = itemRefs.size();
        titlesListener = listener;
  	    for(int i = 0; i < itemRefs.size(); i++){
  	        ItemReference  item = new ItemReference();
  	        itemTitles.add(itemRefs.get(i));
  	        item.load(itemRefs.get(i), new TitleLoader(i));
  	    }
      } catch (LoadException e) {
      }
	  }
	  else{
	    listener.finishedLoading(this);
	  }
	}
	
	
  /**
   * Move item to new position
   * @param index - old position
   * @param newPosition - new position
   */
  public void moveItem(int index, int newPosition){
    String temp;

    // move DOM nodes
    NodeList  nodes = getDom().getElementsByTagName("assessmentItemRef");
    Element   assessmentSection = 
      (Element)getDom().getElementsByTagName("assessmentSection").item(0); 
    
    // change XML modes
    Node      node = nodes.item(index);
    Node      nodeRef;
    
    assessmentSection.removeChild(node);
    if(index < newPosition){
      nodeRef = nodes.item(newPosition);
      assessmentSection.insertBefore(node, nodeRef);
    }
    else if(newPosition >= nodes.getLength()){
      assessmentSection.appendChild(node);
    }
    else{
      nodeRef = nodes.item(newPosition);
      assessmentSection.insertBefore(node, nodeRef);
    }
    
    // move references
    temp = itemRefs.get(index);
    itemRefs.remove(index);
    itemRefs.insertElementAt(temp, newPosition);
    
    // move titles
    temp = itemTitles.get(index);
    itemTitles.remove(index);
    itemTitles.insertElementAt(temp, newPosition);

  }
  
  
	/**
	 * Load item refs into array
	 */
	protected void initData(){

		NodeList 	nodes = getDom().getElementsByTagName("assessmentItemRef");
    Node			itemRefNode;

    itemRefs = new Vector<String>();
    
    for(int i = 0; i < nodes.getLength(); i++){
    	itemRefNode = nodes.item(i);
    	itemRefs.add( getBaseUrl() + ((Element)itemRefNode).getAttribute("href") );
    }
	}

	/**
	 * Send event when all titles loaded
	 */
	private synchronized void  titleLoaded(){
	
	  titlesLoadedCounter --;
	  if(titlesLoadedCounter == 0){
	    titlesListener.finishedLoading(this);
	  }
	}
	
	/**
	/* inner class for loading item title
	 */
	private class TitleLoader implements IDocumentLoaded{
	  
	  private int index;
	  
	  public TitleLoader(int index){
	    this.index = index;
	  }
	  
    public void finishedLoading(XMLDocument document) {
      itemTitles.set( index, ((ItemReference)document).getTitle() );
      titleLoaded();
    }
  };
}
