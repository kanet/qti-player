package pl.klangner.qtiplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

	// --------------------------------------------------------------------------
	public Preferences(Context context){
		
		settings = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	// --------------------------------------------------------------------------
	public String getServer(){
		
		return settings.getString(SERVER_KEY, "http://localhost");
	}
	
	// --------------------------------------------------------------------------
	public void setServer(String server){
		setValue(SERVER_KEY, server);
	}
	
	// --------------------------------------------------------------------------
	private void setValue(String key, String value){
		SharedPreferences.Editor editor = settings.edit();
    editor.putString(key, value);
    editor.commit();
	}
	
	// --------------------------------------------------------------------------
	// Private members
	private static final String PREFS_NAME = "application";
	private static final String SERVER_KEY = "Server";
	private SharedPreferences settings;
}
