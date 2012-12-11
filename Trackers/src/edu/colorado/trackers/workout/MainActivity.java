package edu.colorado.trackers.workout;

import edu.colorado.trackers.R;
import edu.colorado.trackers.SendEmail;
import edu.colorado.trackers.workout.CancelDialog;
import edu.colorado.trackers.db.Database;
import edu.colorado.trackers.db.Deleter;
import edu.colorado.trackers.workout.EditWorkout;
import edu.colorado.trackers.workout.MainActivity;
import edu.colorado.trackers.db.ResultSet;
import edu.colorado.trackers.db.Selector;

import edu.colorado.trackers.workout.CancelDialog.CancleDialogListener;

import android.os.Bundle;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
public class MainActivity extends Activity implements CancleDialogListener {

	private ListView workoutProfiles;
	private int itemSelected = -1;
	private String tableName = "workout";
	private Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.w_activity_main);
		db = new Database(this, "trackers.db");
		boolean b = db.tableExists(tableName);
		System.out.print("table exist = " + b);
		if(!b) {
			db.createTable(tableName, _ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, exercise TEXT NOT NULL, weight integer, reps INTEGER, sets INTEGER, date TEXT NOT NULL");
		}
		workoutProfiles = (ListView) findViewById(R.id.exercise_list);
		workoutProfiles.setAdapter(getWorkouts());
		
		// On item click, go to edit screen
        workoutProfiles.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View list_item, int position, long id) {
            		MainActivity.this.itemSelected = position;
        			TextView text = (TextView) workoutProfiles.getChildAt(itemSelected);
            		Intent intent = new Intent(MainActivity.this, EditWorkout.class); 
         			intent.putExtra("exercise", text.getText().toString());
         			startActivity(intent);             		
            }
        });

        // On item long click, show context menu
        workoutProfiles.setLongClickable(true);
        workoutProfiles.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View list_item, int position, long id) {
            	itemSelected = position;
    			registerForContextMenu(workoutProfiles);
    			openContextMenu(workoutProfiles);
    			unregisterForContextMenu(workoutProfiles);
				return true;
            }
        });         
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.w_activity_main, menu);
		return true;
	} 
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);    
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.w_context_menu, menu);
	}	

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.context_menu_delete:
        	showNoticeDialog();
        	return true;
        case R.id.context_menu_graph:
        }
    	return true;
    	
    }
	
	public boolean onOptionsItemSelected (MenuItem item) {
		Intent intent = new Intent(this, EditWorkout.class);
		if (item.getItemId() == R.id.menu_new_profile) {
			startActivity(intent);
		}
		else if (item.getItemId() == R.id.menu_send_mail)
		{			
			Selector select = db.selector(tableName);
			String workouts = new String("Workout " + "Exercise " + "Weight " + "Reps " + "Date " + "\n\n");
			int count = select.execute();
			if(count != 0) {
				ResultSet cursor = select.getResultSet();
				
				while (cursor.moveToNext()) {
					workouts += cursor.getString(1) + "---" + cursor.getString(2) + 
							   "---" + cursor.getString(3) + "---" + cursor.getString(4) + "\n";
					System.out.println("Got exercise: " + cursor.getString(0));
					
				}
				cursor.close();
			}
			
			String subject = new String("workout log");
			SendEmail s= new SendEmail(subject, workouts);
			s.show(getFragmentManager(), "Email workout");

			//intent_mail.putExtra("content", workouts);
			//startActivity(intent_mail);
		}
		return true;
	}
	
    public void onResume() {
        super.onResume();
		workoutProfiles.setAdapter(getWorkouts());
    }

	public ArrayAdapter<String> getWorkouts() {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item);

		String[] str = {"exercise", "weight", "date"};
		Selector select = db.selector(tableName);
		select.addColumns(str);
		int count = select.execute();
		if(count != 0) {
			ResultSet cursor = select.getResultSet();
		
			String workoutText ="";
			while (cursor.moveToNext()) {
				workoutText = cursor.getString(0);
				adapter.add(workoutText);
			}
			cursor.close();
		
			if (adapter.getCount() == 0) {
				adapter.add("No exercises.");			
			} 
		}
	    else {
			adapter.add("No exercises.");			
		}

		return adapter;
	}

	public void remove_name(String name) {
        Deleter del = db.deleter(tableName);
        del.where("exercise = ?",  new String[] { name });
        del.execute();

        ArrayAdapter<String> adapter = getWorkouts();
        adapter.notifyDataSetChanged();
		workoutProfiles.setAdapter(adapter);
        itemSelected = -1;
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		System.out.print("Negative Clicked");
	}	
	
	public void onDialogPositiveClick(DialogFragment dialog) {
        String selectedFromList =(String) (workoutProfiles.getItemAtPosition(itemSelected));

		//TextView text = (TextView) workoutProfiles.getChildAt(itemSelected);
		remove_name(selectedFromList);		
	}

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CancelDialog(); 
        dialog.show(getFragmentManager(), "CancelDialog");
    }

}

