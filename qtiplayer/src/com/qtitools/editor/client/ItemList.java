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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;
import com.klangner.qtiplayer.client.model.Assessment;
import com.klangner.qtiplayer.client.model.IDocumentLoaded;
import com.klangner.qtiplayer.client.model.XMLDocument;

public class ItemList extends Composite {

  /** tree widget */
  private Tree  itemTree;
  /** Assessment */
  private Assessment assessment;
  
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
   * @return view with player
   */
  private Widget createView(){

    itemTree = new Tree();

    return itemTree;
  }

  /**
   * get titles from assessment
   */
  private void updateItemList() {
    for(int i = 0; i < assessment.getItemCount(); i++){
      itemTree.add(new Label(assessment.getItemTitle(i)));
    }
    
    itemTree.setSelectedItem(itemTree.getItem(0));

  }
  
  
}
