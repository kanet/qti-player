package pl.klangner.mt.model;

import java.util.List;
import java.util.Vector;


public class Chord {

	public static final int CHORD_MAJOR = 0;
	public static final int CHORD_MINOR = 1;
	public static final int CHORD_5 = 2;
	public static final int MAX_CHORD_TYPE = 3;
	
	// ----------------------------------------------------------------------------------------------
	public Chord(Note rootNote, int type){
		assert( type >= 0 && type < MAX_CHORD_TYPE);
		
		this.root = rootNote;
		this.type = type;
	}
	
	// ----------------------------------------------------------------------------------------------
	public String getName(){
		return root.getName() + " " + chordTypes[type][0];
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Return number of notes in this chord
	 */
	public int getNoteCount(){
		return ((int[])chordTypes[type][1]).length + 1;
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Return number of notes in this chord
	 */
	public List<Note> getNotes(){
		Vector<Note>		notes = new Vector<Note>();
		int[]						intervals = (int[])chordTypes[type][1];
		
		notes.add(root);
		for(int i = 0; i < intervals.length; i++){
			notes.add(root.getIntervalNote(intervals[i]));
		}
		
		return notes;
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private Note 		root;
	private int			type;
	
	/**
	 * Check record contains name and intervals used to build chord
	 */
	private static final Object[][] chordTypes = {
		{"Major", new int[]{4, 7}},
		{"Minor", new int[]{3, 7}},
		{"5", new int[]{7}}
	};
	
}
