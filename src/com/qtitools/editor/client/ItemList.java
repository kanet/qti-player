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
package com.qtitools.editor.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.qtitools.player.client.model.Assessment;
import com.qtitools.player.client.model.IDocumentLoaded;
import com.qtitools.player.client.model.XMLDocument;

public class ItemList extends Composite {

  /** tree widget */
  private Tree  itemsTree;
  /** Assessment */
  private Assessment assessment;
  /** TreeItem -> item id mapping */
  private Map<TreeItem, Integer>  treeItemsId = new HashMap<TreeItem, Integer>();
  
  /**
   * Constructor
   */
  public ItemList(Assessment assessment){

    this.assessment = assessment;
    
    initWidget(createView());
    setStyleName("qe-item-list");
    assessment.loadTitles(new IDocumentLoaded(){

      public void finishedLoading(XMLDocument document) {
        updateItemList();
      }

    });

  }
  
  /**
   * Add selection handler for this list
   * @return
   */
  public HandlerRegistration addSelectionHandler(ItemSelectedHandler handler){
    
    return itemsTree.addSelectionHandler(new SelectionAdapter(handler));
  }
  
  /**
   * @return view with player
   */
  private Widget createView(){

    itemsTree = new Tree();

    return itemsTree;
  }

  /**
   * get titles from assessment
   */
  private void updateItemList() {
    
    TreeItem treeItem;
    TreeItem root;
    
    root = itemsTree.addItem( assessment.getTitle() );
    
    for(int i = 0; i < assessment.getItemCount(); i++){
      treeItem = root.addItem( assessment.getItemTitle(i) );
      treeItemsId.put(treeItem, new Integer(i));
    }
    
    root.setState(true);
    if(treeItemsId.size() > 0)
      itemsTree.setSelectedItem(root.getChild(0), false);

  }
  
  /** 
   * Selection handler wrapper
   */
  class SelectionAdapter implements SelectionHandler<TreeItem>{

    /** item index */
    private ItemSelectedHandler handler;
    
    /** Constructor */
    public SelectionAdapter(ItemSelectedHandler handler) {
      this.handler = handler;
    }
    
    /** Fire event */
    public void onSelection(SelectionEvent<TreeItem> event) {
      
      if(treeItemsId.containsKey(event.getSelectedItem()))
        handler.itemSelected( treeItemsId.get(event.getSelectedItem()) );
    }
    
  };
  
}
