package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.Assessment;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartView extends LinearLayout {

	// --------------------------------------------------------------------------
	public StartView(Context context, Assessment	a) {
		super(context);
		
		this.assessment = a;
		
		createView();
	}

	// --------------------------------------------------------------------------
	private void createView() {

		Context 			context = getContext();
		TextView			about_view = new TextView(context);
		Button				start_button = new Button(context);
		LayoutParams	lp;

		setOrientation(VERTICAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = 1;
		about_view.setLayoutParams(lp);
		about_view.setPadding(10, 10, 10, 10);
		about_view.setText(assessment.getDescription());
		
		start_button.setText(R.string.start);
		start_button.setLayoutParams(lp);
		
		addView(about_view);
		addView(start_button);
	}

	// --------------------------------------------------------------------------
	// Private members
	private Assessment	assessment;
}
