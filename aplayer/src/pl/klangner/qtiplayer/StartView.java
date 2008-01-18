package pl.klangner.qtiplayer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartView extends LinearLayout {

	// --------------------------------------------------------------------------
	public StartView(Context context, ContentManager navigation) {
		super(context);
		
		this.navigation = navigation;
		
		createView();
	}

	// --------------------------------------------------------------------------
	private void createView() {

		Context 			context = getContext();
		TextView			about_view = new TextView(context);
		LayoutParams	lp;

		setOrientation(VERTICAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = 1;
		about_view.setLayoutParams(lp);
		about_view.setPadding(10, 10, 10, 10);
		about_view.setText(navigation.getAssessment().getDescription());
		
		start_button = new Button(context);
		start_button.setText(R.string.start);
		start_button.setLayoutParams(lp);
		start_button.setOnClickListener(listener);
		
		addView(about_view);
		addView(start_button);
	}

	// --------------------------------------------------------------------------
	private View.OnClickListener listener = new View.OnClickListener(){

		public void onClick(View view) {
			navigation.goToPage(0);
		}
	};
		
	// --------------------------------------------------------------------------
	// Private members
	private ContentManager 	navigation;
	private Button					start_button;
}
