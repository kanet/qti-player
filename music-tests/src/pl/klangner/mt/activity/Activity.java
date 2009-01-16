package pl.klangner.mt.activity;

import java.io.IOException;

public interface Activity {

	/** Generate activity as XML file in a given path */
	public void generate(String path) throws IOException;
	
}
