package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.Assessment;
import android.content.Context;
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

		Context context = getContext();
		
		TextView	about_view = new TextView(context);
		about_view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		about_view.setText(assessment.getDescription());
		
		addView(about_view);
	}

	// --------------------------------------------------------------------------
	// Private members
	private Assessment	assessment;
}
