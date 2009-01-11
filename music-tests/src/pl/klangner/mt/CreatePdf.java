package pl.klangner.mt;

import java.io.FileOutputStream;
import java.io.IOException;

import pl.klangner.mt.model.Chord;

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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Chapter 	chapter;
		List 			list;
		Paragraph	p;

		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("ChordsWorkbook.pdf"));
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
			p = new Paragraph("In the following questions provide all notes starting from root note.", promptFont);
			p.setSpacingAfter(10);
			document.add(p);

			list = new List(true, 20);
			for(int i = 0; i < 10; i++){
				Chord chord = Chord.getRandom();
				list.add(new ListItem(chord.getName() + "\n _________________________________"));
			}
			document.add(list);

			// Add answers
			p = new Paragraph("Answers", h2Font);
			p.setSpacingAfter(20);
			chapter = new Chapter(p, 1);
			chapter.setNumberDepth(0);
			document.add(chapter);
			list = new List(true, 20);
			list.add(new ListItem("C A B"));
			list.add(new ListItem("D# A F"));
			list.add(new ListItem("G D F#"));
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


}
