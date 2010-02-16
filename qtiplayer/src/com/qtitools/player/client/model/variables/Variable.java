package com.qtitools.player.client.model.variables;

import java.util.Vector;

public abstract class Variable {
	
	public String identifier;

	public Cardinality cardinality;
	
	public BaseType baseType;
	
	public Vector<String> values;
		
	public abstract void reset();
}
