package com.qtitools.player.client.model.responseprocessing;

import java.util.Vector;

public abstract class TemplateMatchCorrectMultiplePerformer {

	public static Vector<String> getDifference(Vector<String> current, Vector<String> previous){
		Vector<String> differences = new Vector<String>();

		for (int s = 0 ; s < current.size() ; s ++){
			if (previous.indexOf(current.get(s)) == -1){
				differences.add("+" + current.get(s));
			}
		}

		for (int s = 0 ; s < previous.size() ; s ++){
			if (current.indexOf(previous.get(s)) == -1){
				differences.add("-" + previous.get(s));
			}
		}
		
		return differences;
	}
}
