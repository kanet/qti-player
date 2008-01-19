package pl.klangner.qtiplayer.model;

import java.util.List;

import junit.framework.TestCase;
import pl.klangner.qtiplayer.server.NanoHTTPD;

public class ChoiceModuleTestCase extends TestCase {

	// --------------------------------------------------------------------------
	protected void setUp() throws Exception {
		super.setUp();

		// Setup server
		new NanoHTTPD(8800);
	}

	// --------------------------------------------------------------------------
	public void testText(){
		
		Assessment 		toc = new Assessment("http://localhost:8800/data/package1");
		List<Module> 	modules = toc.getItems().get(1).getModules();
		ChoiceModule		module = (ChoiceModule)modules.get(2);
		
		assertEquals("What does it say?", module.getPrompt());
	}

}
