package pl.klangner.qtiplayer.model;

import java.util.List;

import pl.klangner.qtiplayer.model.Assessment;
import pl.klangner.qtiplayer.model.Item;
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
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesCount(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<Item> items = toc.getItems();
		
		assertEquals(4, items.size());
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testTitle(){
		
		Assessment a = new Assessment("http://localhost:8800/data/package1");
		
		assertEquals("Example Contentpackage with QTI v2.1 items", a.getTitle());
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesId(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<Item> items = toc.getItems();
		
		assertEquals(4, items.size());
		Item item = items.get(0);
		assertEquals("RES-BCA84FC0-53F9-ABBD-C3FE-BDB5B825CA9E", item.getId());
	}

	// --------------------------------------------------------------------------
	/**
	 * Pobranie wiadomości użytkownika telefonu 
	 */
	public void testResourcesTitle(){
		
		Assessment toc = new Assessment("http://localhost:8800/data/package1");
		List<Item> items = toc.getItems();
		
		assertEquals(4, items.size());
		Item item = items.get(0);
		assertEquals("Monty Hall (Take 1)", item.getTitle());
	}

}
