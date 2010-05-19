package com.qtitools.player.client.model.variables;

import java.util.Vector;

public abstract class Variable {
	
	public Variable(){
		values = new Vector<String>();
		identifier = "";
		cardinality = Cardinality.SINGLE;
		baseType = BaseType.STRING;
	}
	
	public String identifier;

	public Cardinality cardinality;
	
	public BaseType baseType;
	
	public Vector<String> values;
		
	public abstract void reset();
	
	public String getValuesShort(){
		
		String output = "";
		
		for (int i = 0 ; i < values.size() ; i ++ ){
			output += values.get(i);
			if (i < values.size()-1)
				output += ";";
		}
		
		return output;
	}
}
