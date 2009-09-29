package com.klangner.qtiplayer.client.model;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.model.modules.ChoiceModule;
import com.klangner.qtiplayer.client.model.modules.DebugModule;
import com.klangner.qtiplayer.client.model.modules.IModule;
import com.klangner.qtiplayer.client.model.modules.TextModule;


public class AssessmentItem extends AbstractXMLDocument{

	private Vector<IModule>	modules;
	
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
	public IModule getModule(int index){
		return modules.get(index);
	}
	
	/**
	 * fix urls
	 * Load modules
	 */
	protected void initData(){

		Node			itemBody = getDom().getElementsByTagName("itemBody").item(0); 
		NodeList 	nodes;
    IModule		module;

    
    // Fix urls to images
    nodes = getDom().getElementsByTagName("img");
    for(int i = 0; i < nodes.getLength(); i++){
    	Element element = (Element)nodes.item(i);
    	element.setAttribute("src", getBaseUrl() + element.getAttribute("src"));
    }
    
    // Load modules
    nodes = itemBody.getChildNodes();
    modules = new Vector<IModule>(nodes.getLength());
    
    for(int i = 0; i < nodes.getLength(); i++){
    	if(nodes.item(i).getNodeType() == Node.ELEMENT_NODE){
    		module = createModuleFromNode((Element)nodes.item(i));

    		if(null != module)
    			modules.add(module);
    	}
    }
	}

	/**
	 * Create module based on passed xml node
	 * @param node - dom node
	 * @return new module or null if can't create module for given node
	 */
	private IModule createModuleFromNode(Element node) {

		if(node.getNodeName().compareTo("p") == 0)
			return new TextModule(node);
		else if(node.getNodeName().compareTo("choiceInteraction") == 0)
			return new ChoiceModule(node);
		else if(node.getNodeType() == Node.ELEMENT_NODE)
			return new DebugModule(node);
		else
			return null;
	}
	
}
