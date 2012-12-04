package edu.colorado.trackers.shoppinglist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.colorado.trackers.R;

public class SLEditItem extends Activity {
	
	private ShoppingListDB shoppingListDB = new ShoppingListDB(this);
	private Button cancelButton;
	private Button okButton;
	private Button minusButton;
	private Button plusButton;
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
        itemId = getIntent().getIntExtra("item_id", -1);
        System.out.println("Edit Item onCreate: item id = " + itemId);
        
        cancelButton = (Button) findViewById(R.id.cancel_button);
        okButton = (Button) findViewById(R.id.ok_button);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.plus_button);
        
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
				value -= (value > 0) ? 1 : 0;
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
    	
    	EditText editName = (EditText) findViewById(R.id.sl_name);
        EditText editPrice = (EditText) findViewById(R.id.sl_price);
        TextView textQuantity = (TextView) findViewById(R.id.sl_quantity);
    	
    	if (itemId != -1) {
        	SQLiteDatabase db = shoppingListDB.getReadableDatabase();
        	Cursor cursor = db.query("shopping_list_items", 
        			new String[] {"id", "name", "price", "quantity"}, 
        			"id = ?", new String[] { itemId.toString() }, null, null, null);
        	cursor.moveToFirst();
        	itemId = cursor.getInt(0);
        	itemName = cursor.getString(1);
        	itemPrice = cursor.getDouble(2);
        	itemQuantity = cursor.getInt(3);
        	cursor.close();
    	}
    	
        editName.setText(itemName);
        editPrice.setText(itemPrice.toString());
        textQuantity.setText(itemQuantity.toString());
    }
    
    @Override
	public void onPause() {
		super.onPause();
		
		if (saveData) {
			SQLiteDatabase db = shoppingListDB.getWritableDatabase();
			
			EditText nameEdit = (EditText) findViewById(R.id.sl_name);
			EditText priceEdit = (EditText) findViewById(R.id.sl_price);
			TextView quantityText = (TextView) findViewById(R.id.sl_quantity);
			
			String name = nameEdit.getText().toString();
			String priceString = priceEdit.getText().toString();
			String quantityString = quantityText.getText().toString();
			Double price = Double.parseDouble(priceString);
			Integer quantity = Integer.parseInt(quantityString);
			
			ContentValues values = new ContentValues();
			values.put("name", name);
			values.put("price", price);
			values.put("quantity", quantity);
			
			if (itemId != -1) {
				db.update("shopping_list_items", values, "id = ?", new String[] { itemId.toString() });
				System.out.println("Item (id=" + itemId + ") updated");
			} else {
				db.insertOrThrow("shopping_list_items", null, values);
				System.out.println("Item added");
			}
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		shoppingListDB.close();
	}

}
