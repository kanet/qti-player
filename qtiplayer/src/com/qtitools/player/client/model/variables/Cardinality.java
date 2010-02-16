package com.qtitools.player.client.model.variables;

public enum Cardinality {
		
	SINGLE, MULTIPLE, ORDERED, RECORD;
	
	public static Cardinality fromString(String key){
		if (key.toLowerCase() == "single"){
			return SINGLE;
		} else if (key.toLowerCase() == "multiple"){
			return MULTIPLE;
		} else if (key.toLowerCase() == "ordered"){
			return ORDERED;
		} else if (key.toLowerCase() == "record"){
			return RECORD;
		}
		
		return SINGLE;
	}
}
