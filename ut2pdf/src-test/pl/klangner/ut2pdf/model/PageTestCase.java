package pl.klangner.ut2pdf.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class PageTestCase {

	// ----------------------------------------------------------------------------------------------
	@Test
	public void testGetTitle() throws IOException{
		Page		page = loadPage(0);
			
		assertEquals("Page 1", page.getTitle());
	}

	// ----------------------------------------------------------------------------------------------
	@Test
	public void testModuleCount() throws IOException{
		Page		page = loadPage(0);
			
		assertEquals(5, page.countModules());
	}

	// ----------------------------------------------------------------------------------------------
	private Page loadPage(int index) throws IOException{
		Lesson 	lesson = new Lesson();
		Page		page;
			
		lesson.load("samples/Lesson1/script_00001.utt");
		page = lesson.getPage(index);

		return page;
	}

}
