package edu.colorado.trackers.workout;


import edu.colorado.trackers.workout.CancelDialog.CancleDialogListener;

import android.os.Bundle;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;

public class MainActivity extends Activity implements CancleDialogListener {

	private ListView profiles;
	private int itemSelected = -1;
	private String tableName = "workout";
	private Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new Database(this, "profiles.db");
		boolean b = db.tableExists(tableName);
		if(!b) {
			db.createTable(tableName, _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, exercise TEXT NOT NULL, reps INTEGER, sets INTEGER, date TEXT NOT NULL");
		}
		profiles = (ListView) findViewById(R.id.user_list);
		profiles.setAdapter(getCurrentUsers());
        profiles.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> list, View list_item, int position, long id) {
            		MainActivity.this.itemSelected = position;
            		MainActivity.this.invalidateOptionsMenu();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem delete = menu.findItem(R.id.menu_delete_profile);
		MenuItem sendmail = menu.findItem(R.id.menu_send_mail);		
		MenuItem edit = menu.findItem(R.id.menu_activity_test);
		if (itemSelected != -1) {
			delete.setEnabled(true);
			edit.setEnabled(true);
			sendmail.setEnabled(true);
		} else {
			delete.setEnabled(false);
			edit.setEnabled(false);
			sendmail.setEnabled(false);
		}
		return true;
	}

	public boolean onOptionsItemSelected (MenuItem item) {
		Intent intent = new Intent(this, EditWorkout.class);
		if (item.getItemId() == R.id.menu_new_profile) {
			startActivity(intent);
		}
		if (item.getItemId() == R.id.menu_activity_test) {
			TextView text = (TextView) profiles.getChildAt(itemSelected);
			intent.putExtra("exercise", text.getText().toString());
			startActivity(intent);
			itemSelected = -1;
			invalidateOptionsMenu();
		}
		if (item.getItemId() == R.id.menu_delete_profile) {
			showNoticeDialog();
		}
		else if (item.getItemId() == R.id.menu_send_mail)
		{
			Intent intent_mail = new Intent(this, SendEmail.class);
			startActivity(intent_mail);
		}
		return true;
	}
	
    public void onResume() {
        super.onResume();
		profiles.setAdapter(getCurrentUsers());
    }

	public ArrayAdapter<String> getCurrentUsers() {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice);

		String[] str = {"exercise"};
		Selector select = db.selector(tableName);
		select.addColumns(str);
		int count = select.execute();
		if(count != 0) {
			ResultSet cursor = select.getResultSet();
		
			while (cursor.moveToNext()) {
				adapter.add(cursor.getString(0));
				System.out.println("Got exercise: " + cursor.getString(0));
			}
			cursor.close();
		
			if (adapter.getCount() == 0) {
				adapter.add("No exercises.");			
				profiles.setChoiceMode(ListView.CHOICE_MODE_NONE);
			} else {
				profiles.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
		}
		else {
			adapter.add("No exercises.");			
			profiles.setChoiceMode(ListView.CHOICE_MODE_NONE);
		}

		return adapter;
	}

	public void remove_name(String name) {
        Deleter del = db.deleter(tableName);
        del.where("exercise = ?",  new String[] { name });
        del.execute();

		profiles.setAdapter(getCurrentUsers());
        itemSelected = -1;
        invalidateOptionsMenu();
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		System.out.print("Negative Clicked");
	}	
	
	public void onDialogPositiveClick(DialogFragment dialog) {
		TextView text = (TextView) profiles.getChildAt(itemSelected);
		remove_name(text.getText().toString());		
	}
	
	
	
	public void showEmailSendActivity() {
	}

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CancelDialog(); 
        dialog.show(getFragmentManager(), "CancelDialog");
    }

}
