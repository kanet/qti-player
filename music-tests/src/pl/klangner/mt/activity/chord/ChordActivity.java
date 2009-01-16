package pl.klangner.mt.activity.chord;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import pl.klangner.mt.activity.Activity;
import pl.klangner.mt.activity.Question;
import pl.klangner.mt.model.Chord;
import pl.klangner.mt.model.Note;

abstract class ChordActivity implements Activity{

	// ----------------------------------------------------------------------------------------------
	protected ChordActivity(String instruction){
	
		this.instruction = instruction;
	}
	
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Get instruction for this type of activity
	 */
	public String getInstruction(){
		return instruction;
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Generate count number of random questions
	 */
	public List<Question> getQuestions(int count){
		
		Random 					random = new Random();
		List<Chord>			chords = createChordList();
		List<Question>	questions = new Vector<Question>();
		
		for( int i = 0; i < count && chords.size() > 0; i ++ ){
			int					index;
			Chord				chord;
			Question		q;
			
			index = random.nextInt(chords.size());
			chord = chords.get(index);
			chords.remove(index);
			q = chord2question(chord);
			questions.add(q);
		}
		
		return questions;
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Convert chord to question
	 */
	protected Question chord2question(Chord chord) {
		return null;
	}


	// ----------------------------------------------------------------------------------------------
	/**
	 * Create list with all possible chords
	 */
	private List<Chord>	createChordList(){
		List<Chord>	chords = new Vector<Chord>();
		
		for(int i = 0; i < Note.MAX_NOTE; i ++){
			for(int j = 0; j < Chord.MAX_CHORD_TYPE; j ++){
				chords.add(new Chord(new Note(i), j));
			}
		}
		
		return chords;
	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private String instruction;
}
