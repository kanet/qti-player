package com.qtitools.player.client.model.variables;

public abstract class BaseTypeConverter {

	public static Float tryParseFloat(String s){
		
		Float f = null;
		
		try{
			f = new Float(s);
		} catch (Exception e) {}
		
		return f;
	}
}
