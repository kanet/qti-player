package com.klangner.qtiplayer.client.model;

import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.klangner.qtiplayer.client.widget.ChoiceWidget;
import com.klangner.qtiplayer.client.widget.DebugWidget;
import com.klangner.qtiplayer.client.widget.TextWidget;


public class AssessmentItem extends AbstractXMLDocument{

	/** check result for this item */
	private ResponseProcessing	responseProcessing;
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
	 * @return response processing
	 */
	public ResponseProcessing getResponseProcesing(){
		return responseProcessing;
	}
	
	/**
	 * fix urls
	 * Load modules
	 */
	protected void initData(){

		Node			itemBody = getDom().getElementsByTagName("itemBody").item(0); 
		NodeList 	nodes;
    Widget		module;

    
    // Fix urls to images
    nodes = getDom().getElementsByTagName("img");
    fixLinks(nodes, "src");

    // Fix urls to embed resources
    nodes = getDom().getElementsByTagName("embed");
    fixLinks(nodes, "src");

    // Fix urls to internal links
    nodes = getDom().getElementsByTagName("a");
    fixLinks(nodes, "href");

    // Create response processing
    nodes = getDom().getElementsByTagName("responseDeclaration");
    if(nodes.getLength() > 0)
    	responseProcessing = new ResponseProcessing((Element)nodes.item(0));

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
			return new TextWidget(node, responseProcessing);
		else if(nodeName.compareTo("choiceInteraction") == 0)
			return new ChoiceWidget(node, responseProcessing);
//		else if(nodeName.compareTo("orderInteraction") == 0)
//			return new OrderModule(node, responseProcessing);
		else if(node.getNodeType() == Node.ELEMENT_NODE)
			return new DebugWidget(node);
		else
			return null;
	}

	
	/**
	 * Fix links relative to xml file
	 * @param nodes nodes to fix
	 * @param attrName attribute name with link
	 */
	private void fixLinks(NodeList nodes, String attrName){

    for(int i = 0; i < nodes.getLength(); i++){
    	Element element = (Element)nodes.item(i);
    	String link = element.getAttribute(attrName);
    	if(link != null && !link.startsWith("http")){
    		element.setAttribute(attrName, getBaseUrl() + link);
    	}
    	// Links open in new window
    	if(element.getNodeName().compareTo("a") == 0){
    		element.setAttribute( "target", "_blank");
    	}
    }
	}
	
}
