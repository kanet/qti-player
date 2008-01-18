package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.Assessment;
import pl.klangner.qtiplayer.model.AssessmentItem;
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
	  loadAboutView();
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

	}
	
	// --------------------------------------------------------------------------
	private void loadAboutView() {
  	setTitle(assessment.getTitle());
    setContentView(new StartView(this, new ContentManagerImpl()));
	}

	// --------------------------------------------------------------------------
	private void loadPageView(int index) {
		AssessmentItem item = assessment.getItems().get(index);
		setTitle(item.getTitle());
    setContentView(new PageView(this, new ContentManagerImpl()));
	}

	// --------------------------------------------------------------------------
	// Private members
	private Assessment	assessment;
	
	
	// ==========================================================================
	// Inner class
	// ==========================================================================
	private class ContentManagerImpl implements ContentManager{

		// ---------------------------------------------------------------------
		public Assessment getAssessment() {
			return assessment;
		}

		// ---------------------------------------------------------------------
		public int getCurrentPage() {
			return index;
		}

		// ---------------------------------------------------------------------
		public void goToPage(int page) {
			index = page;
			switch(page){
				case ContentManager.ABOUT_PAGE:
					loadAboutView();
					break;
					
				case ContentManager.SUMMARY_PAGE:
					break;
					
				default:
					loadPageView(index);
					break;
			}
		}

		// ---------------------------------------------------------------------
		private int index;
	};
	
}