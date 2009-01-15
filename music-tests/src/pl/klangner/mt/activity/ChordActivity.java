package pl.klangner.mt.activity;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import pl.klangner.mt.model.Chord;
import pl.klangner.mt.model.Note;

public class ChordActivity implements Activity {

	// ----------------------------------------------------------------------------------------------
	/**
	 * Get instruction for this type of activity
	 */
	public String getInstruction(){
		return "Write all chord notes starting from the root.";
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Generate count number of random questions
	 */
	public List<Question> getQuestions(int count){
		
		Random 					random = new Random();
		List<Question>	questions = new Vector<Question>();
		
		for( int i = 0; i < count; i ++ ){
			Note 				note;
			Chord				chord;
			Question		q;
			String			answer = new String();
			List<Note>	notes;
			
			note = new Note(random.nextInt(Note.MAX_NOTE));
			chord = new Chord(note, random.nextInt(Chord.MAX_CHORD_TYPE));
			notes = chord.getNotes();
			for(int j = 0 ; j < notes.size(); j++){
				if(j == 0)
					answer = notes.get(j).getName();
				else
					answer = answer + ", " + notes.get(j).getName();
			}
			q = new Question(chord.getName(), answer);
			questions.add(q);
		}
		
		return questions;
	}
	
}
