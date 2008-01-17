package pl.klangner.qtiplayer;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.ContentURI;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class BookmarksActivity extends ListActivity {
	
	// --------------------------------------------------------------------------
  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    setContentView(R.layout.home);
	  loadBookmarks();
  }

	// --------------------------------------------------------------------------
  /**
   * Create menu
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    boolean result = super.onCreateOptionsMenu(menu);

    menu.add(0, R.string.menu_options, R.string.menu_options);
    return result;
  }

	// --------------------------------------------------------------------------
  /**
   * Respond to the menu
   */
  public boolean onOptionsItemSelected(Item item) {
  	switch (item.getId()) {

  	case R.string.menu_options: 
  		options();
  		break;

  	}
  	
  	return super.onOptionsItemSelected(item);
  }
    
	// --------------------------------------------------------------------------
  /**
   * Show news of selected user
   */
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    ContentURI 	uri;
    
		try {
			uri = new ContentURI("content://bookmark/" + position);
	    Intent i = new Intent(Intent.VIEW_ACTION, uri, this, PlayerActivity.class);
	    startActivity(i);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
  }
  
	// --------------------------------------------------------------------------
  /**
   * Get result of activity
   */
  protected void onActivityResult(int requestCode, int resultCode, String data, Bundle extras) {
  	super.onActivityResult(requestCode, resultCode, data, extras);
      
  	loadBookmarks();
  }
  
	// --------------------------------------------------------------------------
  /**
   * edit options
   */
  private void options() {
  	Intent i = new Intent(this, EditBookmarkActivity.class);
  	startSubActivity(i, 0);
  }
    
  // --------------------------------------------------------------------------
	private void loadBookmarks() {
		
		Preferences		pref = new Preferences(this);
		List<String>	bookmarks = new ArrayList<String>();

		bookmarks.add(pref.getServer());
    // Now create an array adapter and set it to display using our row
  	BaseAdapter adapter = new ArrayAdapter<String>(this, R.layout.text_row, bookmarks);
    setListAdapter(adapter);    
		
	}
	
	// --------------------------------------------------------------------------
	// Private members
	
}