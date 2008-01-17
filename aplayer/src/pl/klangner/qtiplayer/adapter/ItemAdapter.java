package pl.klangner.qtiplayer.adapter;

import java.util.List;

import pl.klangner.qtiplayer.model.Item;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ItemAdapter extends BaseAdapter {
	
	// --------------------------------------------------------------------------
  public ItemAdapter(Context c, List<Item> items) {
  	
  	this.context = c;
  	this.items = items;
  }

  // --------------------------------------------------------------------------
  public View getView(int position, View convertView, ViewGroup parent) {
    
  	Item 	item  = items.get(position);
  	TextView 	view;

  	view = new TextView(context);
  	view.setText(item.getTitle());
    return view;
	}
	
	
  // --------------------------------------------------------------------------
	public final int getCount() {
	    return items.size();
	}
	
  // --------------------------------------------------------------------------
	public final Object getItem(int position) {
	    return items.get(position);
	}
	
  // --------------------------------------------------------------------------
	public final long getItemId(int position) {
	    return position;
	}
	
	// --------------------------------------------------------------------------
  // Private members
  private Context 			context;
  private List<Item> items;

}