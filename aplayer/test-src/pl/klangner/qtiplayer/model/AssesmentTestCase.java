package pl.klangner.qtiplayer.model;

import java.util.List;

import pl.klangner.qtiplayer.model.Assessment;
import pl.klangner.qtiplayer.model.AssessmentItem;
import pl.klangner.qtiplayer.server.NanoHTTPD;
import junit.framework.TestCase;

public class AssesmentTestCase extends TestCase {

	// --------------------------------------------------------------------------
	protected void setUp() throws Exception {
		super.setUp();

		// Setup server
		new NanoHTTPD(8800);
	}

	// --------------------------------------------------------------------------
	public void testDescritpion(){
		
		Assessment a = new Assessment("http://localhost:8800/data/package1");
		
		assertEquals("This is an example Contentpackage containing a number of QTI v2.1 items", a.getDescription());
	}

	// --------------------------------------------------------------------------
	public void testResourcesCount(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<AssessmentItem> items = toc.getItems();
		
		assertEquals(4, items.size());
	}

	// --------------------------------------------------------------------------
	public void testTitle(){
		
		Assessment a = new Assessment("http://localhost:8800/data/package1");
		
		assertEquals("Example Contentpackage with QTI v2.1 items", a.getTitle());
	}

	// --------------------------------------------------------------------------
	public void testResourcesId(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<AssessmentItem> items = toc.getItems();
		
		assertEquals(4, items.size());
		AssessmentItem item = items.get(0);
		assertEquals("RES-BCA84FC0-53F9-ABBD-C3FE-BDB5B825CA9E", item.getId());
	}

	// --------------------------------------------------------------------------
	public void testResourcesTitle(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<AssessmentItem> items = toc.getItems();
		
		assertEquals(4, items.size());
		AssessmentItem item = items.get(0);
		assertEquals("Monty Hall (Take 1)", item.getTitle());
	}

}
