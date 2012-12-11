package edu.colorado.trackers.shoppinglist;

import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.colorado.trackers.R;
import edu.colorado.trackers.db.Database;
import edu.colorado.trackers.db.Inserter;
import edu.colorado.trackers.db.ResultSet;
import edu.colorado.trackers.db.Selector;
import edu.colorado.trackers.db.Updater;

public class SLEditItem extends Activity {
	
	private Database db;
	private String tableName = "shopping_list_items";
	private Button cancelButton;
	private Button okButton;
	private Button minusButton;
	private Button plusButton;
	private EditText nameEditText;
	private EditText priceEditText;
	private TextView quantityTextView;
	private boolean saveData = true;
	private Integer itemId = -1;
	private String itemName = "";
	private Double itemPrice = 0.0;
	private Integer itemQuantity = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_activity_edit_item);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        db = new Database(this, "shopping_list.db");
        
        itemId = getIntent().getIntExtra("item_id", -1);
        System.out.println("Edit Item onCreate: item id = " + itemId);
        
        cancelButton = (Button) findViewById(R.id.cancel_button);
        okButton = (Button) findViewById(R.id.ok_button);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.plus_button);
        nameEditText = (EditText) findViewById(R.id.sl_name);
        priceEditText = (EditText) findViewById(R.id.sl_price);
        
        okButton.setEnabled(validateFields());
        nameEditText.addTextChangedListener(new LocalTextWatcher());
        priceEditText.addTextChangedListener(new LocalTextWatcher());
        quantityTextView.addTextChangedListener(new LocalTextWatcher());
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveData = false;
				finish();
			}
		});
        okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveData = true;
				finish();
			}
		});
        minusButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView quantity = (TextView) findViewById(R.id.sl_quantity);
				Integer value = Integer.parseInt(quantity.getText().toString());
				value -= (value > 1) ? 1 : 0;
				quantity.setText(value.toString());
			}
		});
        plusButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView quantity = (TextView) findViewById(R.id.sl_quantity);
				Integer value = Integer.parseInt(quantity.getText().toString());
				value += (value < 99) ? 1 : 0;
				quantity.setText(value.toString());
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sl_activity_edit_item, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	saveData = true;
    	
    	nameEditText = (EditText) findViewById(R.id.sl_name);
        priceEditText = (EditText) findViewById(R.id.sl_price);
        quantityTextView = (TextView) findViewById(R.id.sl_quantity);
    	
    	if (itemId != -1) {
    		Selector selector = db.selector(tableName);
    		selector.addColumns(new String[] { "id", "name", "price", "quantity" });
    		selector.where("id = ?", new String[] { itemId.toString() });
    		selector.execute();
    		ResultSet cursor = selector.getResultSet();
    		
    		if (cursor.moveToNext()) {
	        	itemId = cursor.getInt(0);
	        	itemName = cursor.getString(1);
	        	itemPrice = cursor.getDouble(2);
	        	itemQuantity = cursor.getInt(3);
    		}
        	cursor.close();
    	}
        nameEditText.setText(itemName);
        priceEditText.setText(itemPrice.toString());
        quantityTextView.setText(itemQuantity.toString());
    }
    
    @Override
	public void onPause() {
		super.onPause();
		
		if (saveData) {
			nameEditText = (EditText) findViewById(R.id.sl_name);
			priceEditText = (EditText) findViewById(R.id.sl_price);
			quantityTextView = (TextView) findViewById(R.id.sl_quantity);
			
			if (!validateFields()) {
				return;
			}
			
			String name = nameEditText.getText().toString();
			String priceString = priceEditText.getText().toString();
			String quantityString = quantityTextView.getText().toString();
			Double price = Double.parseDouble(priceString);
			Integer quantity = Integer.parseInt(quantityString);
			
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("price", price);
			values.put("quantity", quantity);
			
			if (itemId != -1) {
				Updater updater = db.updater(tableName);
				updater.columnNameValues(values);
				updater.where("id = ?", new String[] { itemId.toString() });
				int count = updater.execute();
				System.out.println("Updated " + count + " rows");
			} else {
				Inserter inserter = db.inserter(tableName);
				inserter.columnNameValues(values);
				inserter.execute();
				System.out.println(String.format("Inserted: (%d) %s %.2f", quantity, name, price));
			}
			
			// Insert into records table for graph data
			values.remove("quantity");
			values.put("time", new Date().toString());
			Inserter inserter = db.inserter("sl_record");
			inserter.columnNameValues(values);
			inserter.execute();
			System.out.println(String.format("Inserted: %s %.2f into records", name, price));
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		db.close();
	}
	
	private boolean validateFields() {
		nameEditText = (EditText) findViewById(R.id.sl_name);
		priceEditText = (EditText) findViewById(R.id.sl_price);
		quantityTextView = (TextView) findViewById(R.id.sl_quantity);
		
		String nameString = nameEditText.getText().toString();
		String priceString = priceEditText.getText().toString();
		String quantityString = quantityTextView.getText().toString();
		
		if (nameString.length() == 0) {
			return false;
		}
		
		try {
			Double price = Double.parseDouble(priceString);
			Integer quantity = Integer.parseInt(quantityString);
			
			if (price < 0.0) {
				return false;
			}
			if (quantity < 1 || quantity > 99) {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private class LocalTextWatcher implements TextWatcher {

		public void afterTextChanged(Editable s) {
			okButton.setEnabled(validateFields());
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	}
}
