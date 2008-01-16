package pl.klangner.qtiplayer.model;

import java.util.List;

import pl.klangner.qtiplayer.model.TableOfContent;
import pl.klangner.qtiplayer.model.TocItem;
import pl.klangner.qtiplayer.server.NanoHTTPD;
import junit.framework.TestCase;

public class TocTestCase extends TestCase {

	// --------------------------------------------------------------------------
	protected void setUp() throws Exception {
		super.setUp();

		// Setup server
		new NanoHTTPD(8800);
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesCount(){
		
		TableOfContent toc = new TableOfContent("http://localhost:8800/data/package1");
		List<TocItem> items = toc.getItems();
		
		assertEquals(4, items.size());
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesId(){
		
		TableOfContent toc = new TableOfContent("http://localhost:8800/data/package1");
		List<TocItem> items = toc.getItems();
		
		assertEquals(4, items.size());
		TocItem item = items.get(0);
		assertEquals("RES-BCA84FC0-53F9-ABBD-C3FE-BDB5B825CA9E", item.getId());
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesTitle(){
		
		TableOfContent toc = new TableOfContent("http://localhost:8800/data/package1");
		List<TocItem> items = toc.getItems();
		
		assertEquals(4, items.size());
		TocItem item = items.get(0);
		assertEquals("Monty Hall (Take 1)", item.getTitle());
	}

}
