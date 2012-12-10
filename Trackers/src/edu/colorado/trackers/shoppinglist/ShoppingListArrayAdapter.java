package edu.colorado.trackers.shoppinglist;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.colorado.trackers.R;

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
		
		ShoppingListItem item = super.getItem(position);
		
		quantityView.setText(item.getQuantity().toString());
		nameView.setText(item.getName());
		priceView.setText(String.format("$%.2f", item.getPrice()));
		
		if (item.isCrossed()) {
			nameView.setPaintFlags(nameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		
		return row;
	}
	
	public Double calculateTotalCost() {
		Double total = 0.0;
		for (int i = 0; i < super.getCount(); i++) {
			ShoppingListItem item = super.getItem(i);
			total += item.getPrice();
		}
		return total;
	}
	
	public String getListAsString() {
		String result = "";
		for (int i = 0; i < super.getCount(); i++) {
			ShoppingListItem item = super.getItem(i);
			result += (item.toString() + "\n");
		}
		return result;
	}

}
