package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.Assessment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Menu.Item;

public class PlayerActivity extends Activity {
	
	// --------------------------------------------------------------------------
  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
	  loadAssessment();
    setContentView(new StartView(this, assessment));
  }

	// --------------------------------------------------------------------------
  /**
   * Create menu
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    boolean result = super.onCreateOptionsMenu(menu);

    menu.add(0, R.string.menu_toc, R.string.menu_toc);
    return result;
  }

	// --------------------------------------------------------------------------
  /**
   * Respond to the menu
   */
  public boolean onOptionsItemSelected(Item item) {

  	switch (item.getId()) {
  	case R.string.menu_toc: 
  		showToc();
  		break;

  	}
  	
  	return super.onOptionsItemSelected(item);
  }
    
	// --------------------------------------------------------------------------
  private void showToc() {
    
    Intent i = new Intent(Intent.VIEW_ACTION, getIntent().getData(), this, TocActivity.class);
	  startActivity(i);
	}

	// --------------------------------------------------------------------------
	private void loadAssessment() {

		Preferences		pref = new Preferences(this);
		
		assessment = new Assessment(pref.getServer());
  	setTitle(assessment.getTitle());

	}
	
	// --------------------------------------------------------------------------
	// Private members
	private Assessment	assessment;
	
}