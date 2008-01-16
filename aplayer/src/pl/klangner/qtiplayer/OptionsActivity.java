package pl.klangner.qtiplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OptionsActivity extends Activity {

	// --------------------------------------------------------------------------
	/**
	 * on create event
	 */
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		preferences = new Preferences(this);
		setContentView(R.layout.options);
        
    serverText = (EditText) findViewById(R.id.server_box);
    serverText.setText(preferences.getServer());
   
    save_button = (Button) findViewById(R.id.save);
    save_button.setOnClickListener(listener);

    Button button = (Button) findViewById(R.id.cancel);
    button.setOnClickListener(listener);
	}
	
	// --------------------------------------------------------------------------
	private View.OnClickListener listener = new View.OnClickListener(){

		public void onClick(View view) {
			if(save_button == view)
				preferences.setServer(serverText.getText().toString());
			
      finish();
		}
		
	};

	// --------------------------------------------------------------------------
	// Private members
	private EditText 		serverText;
	private Preferences preferences;
	private Button 			save_button;
	
}
