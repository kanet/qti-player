package com.klangner.qtiplayer.client.model;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.module.DebugWidget;
import com.klangner.qtiplayer.client.module.IModuleSocket;
import com.klangner.qtiplayer.client.module.IResponse;
import com.klangner.qtiplayer.client.module.choice.ChoiceModule;
import com.klangner.qtiplayer.client.module.text.TextModule;


public class AssessmentItem extends AbstractXMLDocument{

	/** check result for this item */
	private HashMap<String, ResponseProcessing> responsesMap = new HashMap<String, ResponseProcessing>();
	private Vector<Widget>			modules;
	
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
		
		for(ResponseProcessing response: responsesMap.values()){
			DebugWidget.alert(response.toString());
			result.merge(response.getResult());
		}
		
		return result;
	}
	
	/** 
	 * Reset score
	 */
	public void reset() {

		for(ResponseProcessing response: responsesMap.values())
			response.reset();
	}
	
	
	/**
	 * fix urls
	 * Load modules
	 */
	protected void initData(){

		Node			itemBody = getDom().getElementsByTagName("itemBody").item(0); 
		ResponseProcessing	responseProcessing;
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

    // Create response processing
    nodes = getDom().getElementsByTagName("responseDeclaration");
    for(int i = 0; i < nodes.getLength(); i++){
    	node = nodes.item(i);
    	responseProcessing = new ResponseProcessing((Element)node);
    	responsesMap.put(responseProcessing.getID(), responseProcessing);
    }

    // Load modules
    nodes = itemBody.getChildNodes();
    modules = new Vector<Widget>(nodes.getLength());
    
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
	private Widget createModuleFromNode(Element node) {

		String nodeName = node.getNodeName();
		
		if(nodeName.compareTo("p") == 0 || nodeName.compareTo("blockquote") == 0)
			return new TextModule(node, moduleSocket);
		else if(nodeName.compareTo("choiceInteraction") == 0)
			return new ChoiceModule(node, moduleSocket);
//		else if(nodeName.compareTo("orderInteraction") == 0)
//			return new OrderModule(node, responseProcessing);
		else if(node.getNodeType() == Node.ELEMENT_NODE)
			return new DebugWidget(node);
		else
			return null;
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

	
	private IModuleSocket	moduleSocket = new IModuleSocket(){

		@Override
		public IResponse getResponse(String id) {
			return responsesMap.get(id);
		}
		
	};
}
