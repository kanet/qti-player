package pl.klangner.mt;

import java.io.File;
import java.io.IOException;

import pl.klangner.mt.activity.chord.ChordNotesActivity;
import pl.klangner.mt.activity.chord.Notes2ChordActivity;

public class TestGenerator {

	// ----------------------------------------------------------------------------------------------
	public static void main(String[] args) throws IOException {

		ChordNotesActivity cna = new ChordNotesActivity();
		Notes2ChordActivity nca = new Notes2ChordActivity();
		
		// Create path for 1 activity
		createPath("output/chord2notes");
		createPath("output/notes2chord");
		
		cna.generate("output/chord2notes");
		nca.generate("output/notes2chord");
	}
	
	// ----------------------------------------------------------------------------------------------
	private static void createPath(String path){
		File file = new File(path);
		
		if( !file.isDirectory() ){
			file.mkdirs();
		}
	}
}
