package pl.klangner.mt.activity.chord;

import java.util.List;

import pl.klangner.mt.model.Chord;
import pl.klangner.mt.model.Note;
import pl.klangner.mt.activity.Task;

public class ChordNotesActivity extends ChordActivity {

	// ----------------------------------------------------------------------------------------------
	public ChordNotesActivity(){
		super("Write all chord notes starting from the root.");
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Show Chord, Guess chord notes
	 */
	protected Task createTask(Chord chord){
		
		String			answer = new String();
		List<Note>	notes;
			
		notes = chord.getNotes();
		for(int j = 0 ; j < notes.size(); j++){
			if(j == 0)
				answer = notes.get(j).getName();
			else
				answer = answer + ", " + notes.get(j).getName();
		}

		return new Task(chord.getName(), answer);
	}

}
