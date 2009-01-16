package pl.klangner.mt.activity.chord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import pl.klangner.mt.activity.Task;
import pl.klangner.mt.model.Chord;
import pl.klangner.mt.model.Note;


abstract class ChordActivity{

	// ----------------------------------------------------------------------------------------------
	protected ChordActivity(String instruction){
	
		this.instruction = instruction;
	}
	
	//----------------------------------------------------------------------------------------------
	public void generate(String path) throws IOException{
		
		FileWriter 	writer = new FileWriter(path + "/activity.xml");
		List<Task>	tasks = createActivity(10);
		
		writer.write("<activity>\n");
		writer.write("<prompt>" + instruction + "</prompt>");
		for(Iterator<Task> it = tasks.iterator(); it.hasNext();){
			Task task = it.next();
			writer.write("<question>" + task.getQuestion() + "</question>");
			writer.write("<answer>" + task.getAnswer() + "</answer>");
		}
		
		writer.write("</activity>");
		
		writer.close();
	}
	
	// ----------------------------------------------------------------------------------------------
	/**
	 * Generate count number of random questions
	 */
	private List<Task> createActivity(int count){
		
		Random 					random = new Random();
		List<Chord>			chords = createChordList();
		List<Task>	questions = new Vector<Task>();
		
		for( int i = 0; i < count && chords.size() > 0; i ++ ){
			int					index;
			Chord				chord;
			Task		q;
			
			index = random.nextInt(chords.size());
			chord = chords.get(index);
			chords.remove(index);
			q = createTask(chord);
			questions.add(q);
		}
		
		return questions;
	}

	// ----------------------------------------------------------------------------------------------
	/**
	 * Convert chord to question
	 */
	protected Task createTask(Chord chord) {
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
