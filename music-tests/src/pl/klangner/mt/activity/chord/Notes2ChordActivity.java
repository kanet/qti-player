package pl.klangner.mt.activity.chord;

import java.util.List;
import java.util.Random;

import pl.klangner.mt.activity.Task;
import pl.klangner.mt.model.Chord;
import pl.klangner.mt.model.Note;

public class Notes2ChordActivity extends ChordActivity {

	// ----------------------------------------------------------------------------------------------
	public Notes2ChordActivity(){
		super("Guess chord name from given notes. Notes are in random order");
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Show chord notes in random order. Guess chord name
	 */
	protected Task createTask(Chord chord){
		
		Random 			random = new Random();
		String			question = new String();
		List<Note>	notes;
			
		notes = chord.getNotes();
		while(notes.size() > 0){
			int index = random.nextInt(notes.size());
			if(question.length() > 0)
				question = question + ", " + notes.get(index).getName();
			else
				question = notes.get(index).getName();
			notes.remove(index);
		}

		return new Task(question, chord.getName());
	}

}
