package edu.colorado.trackers.shoppinglist;

import edu.colorado.trackers.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShoppingListArrayAdapter extends ArrayAdapter<ShoppingListItem> {
	
	private Activity context;
	
	public ShoppingListArrayAdapter(Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		LayoutInflater inflater = context.getLayoutInflater();
		View row = inflater.inflate(R.layout.sl_row, parent, false);
		TextView quantityView = (TextView) row.findViewById(R.id.sl_row_quantity);
		TextView nameView = (TextView) row.findViewById(R.id.sl_row_name);
		TextView priceView = (TextView) row.findViewById(R.id.sl_row_price);
		
		quantityView.setText(super.getItem(position).getQuantity().toString());
		nameView.setText(super.getItem(position).getName());
		priceView.setText(String.format("$%.2f", super.getItem(position).getPrice()));
		
		return row;
	}

}
