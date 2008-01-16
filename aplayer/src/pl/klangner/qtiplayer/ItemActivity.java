package pl.klangner.qtiplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;


public class ItemActivity extends Activity {
	
	// --------------------------------------------------------------------------
  /** 
   * Called when the activity is first created. 
   */
  public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
//	  setContentView(R.layout.message);

  }

	// --------------------------------------------------------------------------
  /**
   * Create menu
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    boolean result = super.onCreateOptionsMenu(menu);

    return result;
  }

	// --------------------------------------------------------------------------
  /**
   * Respond to the menu
   */
  public boolean onOptionsItemSelected(Item item) {

  	return super.onOptionsItemSelected(item);
  }
    
	// ------------------------------------------------------------------------
  // Private members
  
}