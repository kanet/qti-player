package com.qtitools.player.client.model.variables.response;

import java.util.HashMap;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class Mapping {

	public Float lowerBound;
	
	public Float upperBound;
	
	public Float defaultValue = new Float(0);
	
	public HashMap<String, Float> mapEntries;
	
	public Mapping(Node mappingNode){
		
		mapEntries = new HashMap<String, Float>();
		
		if (mappingNode == null)
			return;

		try {
		
			Node node;
			
			node = mappingNode.getAttributes().getNamedItem("lowerBound");
			if (node != null)
				lowerBound = new Float( node.getNodeValue() );
	
			node = mappingNode.getAttributes().getNamedItem("upperBound");
			if (node != null)
				upperBound = new Float( node.getNodeValue() );
	
			node = mappingNode.getAttributes().getNamedItem("defaultValue");
			if (node != null)
				defaultValue = new Float( node.getNodeValue() );
			
			NodeList entries = ((Element)mappingNode).getElementsByTagName("mapEntry");
			
			for(int i = 0; i < entries.getLength(); i++)
				mapEntries.put( entries.item(i).getAttributes().getNamedItem("mapKey").getNodeValue(), new Float(entries.item(i).getAttributes().getNamedItem("mappedValue").getNodeValue())  );
			
		} catch (Exception e) {
		}
					
		
	}
	
	
}
