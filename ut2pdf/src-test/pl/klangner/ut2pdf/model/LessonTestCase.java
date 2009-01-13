package pl.klangner.ut2pdf.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class LessonTestCase {

	// ----------------------------------------------------------------------------------------------
	@Test
	public void testLoadName() throws IOException{
		Lesson lesson = new Lesson();
			
		lesson.load("samples/Lesson1/script_00001.utt");

		assertEquals("Lesson 1", lesson.getTitle());
	}

	// ----------------------------------------------------------------------------------------------
	@Test
	public void testLoadPages() throws IOException{
		Lesson lesson = new Lesson();
			
		lesson.load("samples/Lesson1/script_00001.utt");

		assertEquals(4, lesson.getPageCount());
	}

	// ----------------------------------------------------------------------------------------------
	@Test
	public void testLoadPageRef() throws IOException{
		Lesson 	lesson = new Lesson();
		Page		page;
			
		lesson.load("samples/Lesson1/script_00001.utt");
		page = lesson.getPage(1);

		assertEquals("Page 2", page.getTitle());
	}

}
