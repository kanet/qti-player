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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.IStateful;


public class AssessmentItem extends XMLDocument implements IStateful{

  /** module factory */
  private ModuleFactory moduleFactory;
	/** check result for this item */
	private HashMap<String, Response> responsesMap = new HashMap<String, Response>();
	/** Module list */
	private Vector<Widget>			modules;
	
	
	/**
	 * constructor
	 */
	public AssessmentItem(){
	  moduleFactory = new ModuleFactory();  
	}
	
	
  /**
   * @see IStateful#getState()
   */
  public Serializable getState() {
    Vector<Serializable>  state = new Vector<Serializable>();
    
    for(Widget module : modules){
      if(module instanceof IStateful){
        state.add(((IStateful)module).getState());
      }
    }
    
    return state;
  }

  /**
   * @see IStateful#setState(Serializable)
   */
  @SuppressWarnings("unchecked")
  public void setState(Serializable newState) {
    
    if(newState instanceof Vector){
      Vector<Serializable> state = (Vector<Serializable>)newState;
      
      int index = 0;
      for(Widget module : modules){
        if(module instanceof IStateful){
          ((IStateful)module).setState(state.get(index));
          index ++;
        }
      }
    }
  }
  
	/**
	 * @return item title
	 */
	public String getTitle(){

    Node rootNode = getDom().getElementsByTagName("assessmentItem").item(0);
    String title = ((Element)rootNode).getAttribute("title");
    
    return title;
	}
	
	/**
	 * @return number of modules
	 */
	public int getModuleCount(){
		return modules.size();
	}

	/**
	 * @param index
	 * @return get module at given index
	 */
	public Widget getModule(int index){
		return modules.get(index);
	}
	
	/**
	 * @return item result
	 */
	public Result getResult(){

		Result result = new Result();
		
		for(Response response: responsesMap.values()){
			result.merge(response.getResult());
		}
		
		return result;
	}
	
	
	/** 
	 * Reset score
	 */
	public void reset() {

		for(Response response: responsesMap.values())
			response.reset();
	}
	
	
	/**
	 * fix urls
	 * Load modules
	 */
	protected void initData(){

		Node			itemBody = getDom().getElementsByTagName("itemBody").item(0); 
		Response	responseProcessing;
		NodeList 	nodes;
		Node			node;
    Widget		module;

    
    // Fix urls to images
    fixLinks("img", "src");
    fixLinks("embed", "src");
    fixLinks("sound", "src");
    fixLinks("video", "src");
    fixLinks("source", "src");
    fixLinks("a", "href");
    fixLinks("object", "data");

    // Create response processing
    nodes = getDom().getElementsByTagName("responseDeclaration");
    for(int i = 0; i < nodes.getLength(); i++){
    	node = nodes.item(i);
    	responseProcessing = new Response((Element)node);
    	responsesMap.put(responseProcessing.getID(), responseProcessing);
    }

    // Load modules
    nodes = itemBody.getChildNodes();
    modules = new Vector<Widget>(nodes.getLength());
    
    for(int i = 0; i < nodes.getLength(); i++){
    	if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE){
    		module = moduleFactory.createModule((Element)nodes.item(i), moduleSocket);

    		if(null != module)
    			modules.add(module);
    	}
    }
	}

	/**
	 * Protected function to allow junit testing.
	 * @return
	 */
	protected IModuleSocket	getModuleSocket(){
		return moduleSocket;
	}
	
	
	/**
	 * Fix links relative to xml file
	 * @param tagName tag name
	 * @param attrName attribute name with link
	 */
	private void fixLinks(String tagName, String attrName){

    NodeList nodes = getDom().getElementsByTagName(tagName);

    for(int i = 0; i < nodes.getLength(); i++){
    	Element element = (Element)nodes.item(i);
    	String link = element.getAttribute(attrName);
    	if(link != null && !link.startsWith("http")){
    		element.setAttribute(attrName, getBaseUrl() + link);
    	}
    	// Links open in new window
    	if(tagName.compareTo("a") == 0){
    		element.setAttribute( "target", "_blank");
    	}
    }
	}

	
	/**
	 * Inner class for module socket implementation
	 */
	private IModuleSocket	moduleSocket = new IModuleSocket(){

		public IResponse getResponse(String id) {
			return responsesMap.get(id);
		}

	};
}
