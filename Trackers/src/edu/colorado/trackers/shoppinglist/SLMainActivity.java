package edu.colorado.trackers.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import edu.colorado.trackers.R;

public class SLMainActivity extends Activity {
	
	private ListView listItems;
	private int selectedItem = -1;
	private ShoppingListDB shoppingListDB = new ShoppingListDB(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sl_activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        listItems = (ListView) findViewById(R.id.shopping_list);
        listItems.setAdapter(getListItems());
        listItems.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> list, View view, int index, long id) {
				SLMainActivity.this.selectedItem = index;
				registerForContextMenu(listItems);
				openContextMenu(listItems);
				unregisterForContextMenu(listItems);
				SLMainActivity.this.invalidateOptionsMenu();
			}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sl_activity_main, menu);
        return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    	getMenuInflater().inflate(R.menu.sl_context_menu, menu);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                
            case R.id.sl_menu_new_item:
            	Intent intent = new Intent(this, SLEditItem.class);
            	startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	ShoppingListItem slItem = (ShoppingListItem) listItems.getItemAtPosition(selectedItem);
    	
    	if (item.getItemId() == R.id.context_menu_delete) {
    		System.out.println("Deleted item: " + slItem);
    		deleteItem(slItem.getId());
    	} else if (item.getItemId() == R.id.context_menu_edit) {
    		Intent intent = new Intent(this, SLEditItem.class);
    		intent.putExtra("item_id", slItem.getId());
    		startActivity(intent);
    	} else if (item.getItemId() == R.id.context_menu_cancel) {
    		return super.onContextItemSelected(item);
    	}
    	return true;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	listItems.setAdapter(getListItems());
    }
    
    public ArrayAdapter<ShoppingListItem> getListItems() {
    	ArrayAdapter<ShoppingListItem> adapter = new ShoppingListArrayAdapter(this, android.R.layout.simple_list_item_single_choice);
    	SQLiteDatabase db = shoppingListDB.getReadableDatabase();
    	Cursor cursor = db.query("shopping_list_items", // table
    							 new String[] {"id", "name", "price", "quantity"}, // columns
    							 null, // selection
    							 null, // selectionArgs
    							 null, // groupBy
    							 null, // having
    							 "name"); // orderBy
    	while (cursor.moveToNext()) {
    		Integer id = cursor.getInt(0);
    		String name = cursor.getString(1);
    		Double price = cursor.getDouble(2);
    		Integer quantity = cursor.getInt(3);
    		
    		ShoppingListItem item = new ShoppingListItem(id, name, price, quantity);
    		adapter.add(item);
    		System.out.println("Got item: " + item);
    	}
    	cursor.close();
    	return adapter;
    }
    
    public void deleteItem(Integer id) {
    	SQLiteDatabase db = shoppingListDB.getWritableDatabase();
    	db.delete("shopping_list_items", "id = ?", new String[] { id.toString() });
    	
    	listItems.setAdapter(getListItems());
    	selectedItem = -1;
    }

}
