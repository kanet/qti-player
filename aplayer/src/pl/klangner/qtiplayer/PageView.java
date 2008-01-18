package pl.klangner.qtiplayer;

import pl.klangner.qtiplayer.model.AssessmentItem;
import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PageView extends LinearLayout {

	// --------------------------------------------------------------------------
	public PageView(Context context, ContentManager navigation) {
		super(context);
		
		this.navigation = navigation;
		item = navigation.getAssessment().getItems().get(navigation.getCurrentPage());
		
		createView();
	}

	// --------------------------------------------------------------------------
	private void createView() {

		Context 			context = getContext();
		TextView			about_view = new TextView(context);
		TextView			counter_view = new TextView(context);
		LinearLayout	navi_panel;
		LayoutParams	lp;

		setOrientation(VERTICAL);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = 1;
		about_view.setLayoutParams(lp);
		about_view.setPadding(10, 10, 10, 10);
		about_view.setText(navigation.getAssessment().getDescription());
		
		prev_button = new Button(context);
		prev_button.setText(R.string.previous);
		prev_button.setLayoutParams(lp);
		next_button = new Button(context);
		next_button.setText(R.string.next);
		next_button.setLayoutParams(lp);
		end_button = new Button(context);
		counter_view.setLayoutParams(lp);
		counter_view.setText("34/45");
		end_button.setText(R.string.finish);
		end_button.setLayoutParams(lp);

		lp.gravity = 0;
		navi_panel = new LinearLayout(context);
		navi_panel.setLayoutParams(lp);
		navi_panel.addView(prev_button);
		navi_panel.addView(counter_view);
		navi_panel.addView(next_button);
		navi_panel.addView(end_button);
		
		addView(about_view);
		addView(navi_panel);
		
	}

	// --------------------------------------------------------------------------
	// Private members
	private ContentManager 	navigation;
	private AssessmentItem	item;
	private Button					prev_button;
	private Button					next_button;
	private Button					end_button;
}
