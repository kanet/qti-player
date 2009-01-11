package pl.klangner.mt.model;

import java.util.Random;

public class Chord {

	public static final int CHORD_MAJOR = 1;
	public static final int CHORD_MINOR = 2;
	public static final int CHORD_5 = 3;
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Create random chord object
	 */
	public static Chord getRandom(){
		Random random = new Random();
		Chord chord = new Chord(random.nextInt(noteName.length), random.nextInt(chordTypeName.length));
		
		return chord;
	}
	
	// ----------------------------------------------------------------------------------------------
	public Chord(int rootNote, int type){
		assert( type >= 0 && type < chordTypeName.length);
		assert( rootNote >= 0 && rootNote < noteName.length);
		
		this.root = rootNote;
		this.type = type;
	}
	
	// ----------------------------------------------------------------------------------------------
	public String getName(){
		return noteName[root] + " " + chordTypeName[type];
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private int 		root;
	private int			type;
	
	private static final String[] noteName = {
		"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

	private static final String[] chordTypeName = {
		"Major",
		"Minor",
		"5"
	};
}
