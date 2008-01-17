package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.adapter.ItemAdapter;
import pl.klangner.qtiplayer.model.Assessment;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;

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

//    menu.add(0, R.string.menu_options, R.string.menu_options);
    return result;
  }

	// --------------------------------------------------------------------------
  /**
   * Respond to the menu
   */
  public boolean onOptionsItemSelected(Item item) {

//  	switch (item.getId()) {
//	  	case R.string.menu_options: 
//	  		options();
//	  		break;
//
//  	}
  	
  	return super.onOptionsItemSelected(item);
  }
    
  // --------------------------------------------------------------------------
	private void loadTOC() {

		Preferences		pref = new Preferences(this);
		
		assessment = new Assessment(pref.getServer());
  	setTitle(assessment.getTitle());

    // Now create an array adapter and set it to display using our row
  	ItemAdapter adapter = new ItemAdapter(this, assessment.getItems());
    setListAdapter(adapter);    
		
	}
	
	// --------------------------------------------------------------------------
	// Private members
	private Assessment	assessment;
	
}