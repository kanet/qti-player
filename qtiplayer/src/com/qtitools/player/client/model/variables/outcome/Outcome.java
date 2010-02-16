package com.qtitools.player.client.model.variables.outcome;

import java.util.Vector;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.qtitools.player.client.model.variables.BaseType;
import com.qtitools.player.client.model.variables.Cardinality;
import com.qtitools.player.client.model.variables.Variable;

public class Outcome extends Variable {

	public Outcome(Node responseDeclarationNode){

		values = new Vector<String>();

		identifier = ((Element)responseDeclarationNode).getAttribute("identifier");

		cardinality = Cardinality.fromString( ((Element)responseDeclarationNode).getAttribute("cardinality") );
		
		baseType = BaseType.fromString( ((Element)responseDeclarationNode).getAttribute("baseType") );
	}
	
	public String interpretation;
	
	public Float normalMaximum;

	
	@Override
	public void reset() {
		
		
	} 
}
