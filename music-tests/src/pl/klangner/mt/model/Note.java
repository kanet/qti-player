package pl.klangner.mt.model;


public class Note {

	public static final int MAX_NOTE = 12;
	
	// ----------------------------------------------------------------------------------------------
	public Note(int index){
		note = index;
	}
	
	// ----------------------------------------------------------------------------------------------
	public int getId(){
		return note;
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * return note which is in distance equal to the given interval
	 */
	public Note getIntervalNote(int interval){
		return new Note((note + interval) % MAX_NOTE);
	}
	
	// ----------------------------------------------------------------------------------------------
	public String getName(){
		return noteNames[note];
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private int 		note;
	
	private static final String[] noteNames = {
		"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"
	};

}
