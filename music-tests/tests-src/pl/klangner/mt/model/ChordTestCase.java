package pl.klangner.mt.model;

import java.util.List;

import junit.framework.TestCase;

public class ChordTestCase extends TestCase {

	// ----------------------------------------------------------------------------------------------
	public void testMajor() {
		Chord chord;
		
		chord = new Chord(new Note(0), Chord.CHORD_MAJOR);
		List<Note>	notes = chord.getNotes();
		assertEquals(3, chord.getNoteCount());
		assertEquals(3, notes.size());
		
		assertEquals(0, notes.get(0).getId());
		assertEquals(4, notes.get(1).getId());
		assertEquals(7, notes.get(2).getId());
	}

	// ----------------------------------------------------------------------------------------------
	public void testMinor() {
		Chord chord;
		
		chord = new Chord(new Note(5), Chord.CHORD_MINOR);
		List<Note>	notes = chord.getNotes();
		assertEquals(3, chord.getNoteCount());
		assertEquals(3, notes.size());
		
		assertEquals(5, notes.get(0).getId());
		assertEquals(8, notes.get(1).getId());
		assertEquals(0, notes.get(2).getId());
	}

	// ----------------------------------------------------------------------------------------------
	public void testChord5() {
		Chord chord;
		
		chord = new Chord(new Note(11), Chord.CHORD_5);
		List<Note>	notes = chord.getNotes();
		assertEquals(2, chord.getNoteCount());
		assertEquals(2, notes.size());
		
		assertEquals(11, notes.get(0).getId());
		assertEquals(6, notes.get(1).getId());
	}

}
