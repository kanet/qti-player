package com.qtitools.player.client.model.variables;

public enum BaseType {
	IDENTIFIER, BOOLEAN, INTEGER, FLOAT, STRING, POINT, PAIR, DIRECTED_PAIR, DURATION, FILE, URL;
	
	public static BaseType fromString(String key){
		if (key.toLowerCase() == "identifier"){
			return IDENTIFIER;
		} else if (key.toLowerCase() == "boolean"){
			return BOOLEAN;
		} else if (key.toLowerCase() == "interger"){
			return INTEGER;
		} else if (key.toLowerCase() == "float"){
			return FLOAT;
		} else if (key.toLowerCase() == "string"){
			return STRING;
		} else if (key.toLowerCase() == "point"){
			return POINT;
		} else if (key.toLowerCase() == "pair"){
			return PAIR;
		} else if (key.toLowerCase() == "directedPair"){
			return DIRECTED_PAIR;
		} else if (key.toLowerCase() == "duration"){
			return DURATION;
		} else if (key.toLowerCase() == "file"){
			return FILE;
		} else if (key.toLowerCase() == "url"){
			return URL;
		}
		
		return STRING;
	}
}
