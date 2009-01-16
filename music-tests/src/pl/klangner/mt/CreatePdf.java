package pl.klangner.mt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import pl.klangner.mt.activity.Activity;
import pl.klangner.mt.activity.Question;
import pl.klangner.mt.activity.chord.ChordNotesActivity;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class CreatePdf {

	// ----------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		
		Activity				activity = new ChordNotesActivity();
		java.util.List<Question>	questions;
		Iterator<Question>				it;
		Chapter 				chapter;
		List 						list;
		Paragraph				p;
		

		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("output/ChordsWorkbook.pdf"));
			document.open();

			// Create first page
			p = new Paragraph("Chord Notes Exercises", h1Font);
			p.setAlignment(Paragraph.ALIGN_CENTER);
			chapter = new Chapter(p, 0);
			chapter.setNumberDepth(0);
			document.add(chapter);
			p = new Paragraph("Created with Music-Tests");
			p.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(p);
			
			// Add questions
			p = new Paragraph("Chord building questions", h2Font);
			p.setSpacingAfter(20);
			chapter = new Chapter(p, 1);
			document.add(chapter);
			p = new Paragraph(activity.getInstruction(), promptFont);
			p.setSpacingAfter(10);
			document.add(p);

			questions = activity.getQuestions(QUESTION_COUNT);
			list = new List(true, 20);
			for(it = questions.iterator(); it.hasNext();){
				Question q = it.next();
				list.add(new ListItem(q.getQuestion() + "\n _________________________________"));
			}
			document.add(list);

			// Add answers
			p = new Paragraph("Answers", h2Font);
			p.setSpacingAfter(20);
			chapter = new Chapter(p, 1);
			document.add(chapter);
			list = new List(true, 20);
			for(it = questions.iterator(); it.hasNext();){
				Question q = it.next();
				list.add(new ListItem(q.getAnswer()));
			}
			document.add(list);
			
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();		

	}
	
	// ----------------------------------------------------------------------------------------------
	// Private members
	private static final Font h1Font = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
	private static final Font h2Font = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
//	private static final Font h3Font = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
	private static final Font promptFont = FontFactory.getFont(FontFactory.HELVETICA, 14);
//	private static final Font pFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
	private static final int QUESTION_COUNT = 10;


}
