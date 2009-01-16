package pl.klangner.mt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestGenerator {

	// ----------------------------------------------------------------------------------------------
	public static void main(String[] args) throws IOException {

		FileWriter writer = new FileWriter("output/index.xml");
		
		// Create path for 1 activity
		createPath("output/activity1");
		
		writer.write("<assessment title='Chord building'>\n");
		writer.write("</activity href='activity1/activity.xml>");
		writer.write("</activity href='activity2/activity.xml>");
		writer.write("</assessment>");
		
		writer.close();
	}
	
	// ----------------------------------------------------------------------------------------------
	private static void createPath(String path){
		File file = new File(path);
		
		if( !file.isDirectory() ){
			file.mkdirs();
		}
	}
}
