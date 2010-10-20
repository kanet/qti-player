package com.qtitools.player.client.model.variables;

import java.util.HashMap;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


public class VariableManager<V extends Variable> {

	public VariableManager(NodeList responseDeclarationNodes, IVariableCreator<V> variableCreator){

		Node node;
		String currIdentifier;
		
		for(int i = 0; i < responseDeclarationNodes.getLength(); i++){
	    	node = responseDeclarationNodes.item(i);
	    	currIdentifier = node.getAttributes().getNamedItem("identifier").getNodeValue();
	    	variables.put(currIdentifier, variableCreator.createVariable(node));

	    	//currResponse = new Response((Element)node);
	    	//responses.put(currResponse.getID(), currResponse);
	    }
	}
	
	public HashMap<String, V> variables = new HashMap<String, V>();
	
	public HashMap<String, V> getVariablesMap(){
		return variables;
	}
	
	public V getVariable(String identifier){
		return variables.get(identifier);
	}
	
	public void reset(){

		for(Variable currVariable: variables.values())
			currVariable.reset();
	}
}
