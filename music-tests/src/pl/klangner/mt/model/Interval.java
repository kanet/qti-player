package pl.klangner.mt.model;


public class Interval {

	public static final int MAX_NOTE = 12;
	
	// ----------------------------------------------------------------------------------------------
	public Interval(int index){
		this.id = index;
	}
	
	// ----------------------------------------------------------------------------------------------
	public int getId(){
		return id;
	}
	
	// ----------------------------------------------------------------------------------------------
	public String getName(){
		return intervalNames[id];
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private int 		id;
	
	private static final String[] intervalNames = {
		"	Perfect Unison", "Minor second", "Major second", "Minor third", "Major third", 
		"Perfect fourth", "Tritone", "Perfect fifth", "Minor sixth", "Major sixth", 
		"Minor seventh", "Major seventh", "Perfect octave"
	};

}
