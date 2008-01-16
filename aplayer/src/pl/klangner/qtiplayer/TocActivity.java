package pl.klangner.qtiplayer;

import java.net.URISyntaxException;

import pl.klangner.qtiplayer.adapter.TocAdapter;
import pl.klangner.qtiplayer.model.TableOfContent;
import android.app.ListActivity;
import android.content.Intent;
import android.net.ContentURI;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ListView;

public class TocActivity extends ListActivity {
	
	// --------------------------------------------------------------------------
  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    setContentView(R.layout.home);
	  loadTOC();
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
			uri = new ContentURI("content://???/");
	    Intent i = new Intent(Intent.VIEW_ACTION, uri, this, ItemActivity.class);
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
      
  	loadTOC();
  }
  
	// --------------------------------------------------------------------------
  /**
   * edit options
   */
  private void options() {
  	Intent i = new Intent(this, OptionsActivity.class);
  	startSubActivity(i, 0);
  }
    
  // --------------------------------------------------------------------------
	private void loadTOC() {
		
		Preferences	pref = new Preferences(this);
		
  	toc = new TableOfContent(pref.getServer());
  	setTitle(pref.getServer());

    // Now create an array adapter and set it to display using our row
  	TocAdapter adapter = new TocAdapter(this, toc.getItems());
    setListAdapter(adapter);    
		
	}
	
	// --------------------------------------------------------------------------
	// Private members
	private TableOfContent	toc;
	
}