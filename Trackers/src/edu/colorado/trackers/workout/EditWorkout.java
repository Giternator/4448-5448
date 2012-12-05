package edu.colorado.trackers.workout;

import edu.colorado.trackers.R;
import edu.colorado.trackers.db.*;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


@TargetApi(11)
public class EditWorkout extends Activity implements OnDateSetListener {

	private String exercise;
	private boolean saveData = true;
	private Database db;
	private String tableName = "workout";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.w_activity_edit_item);
		exercise = getIntent().getStringExtra("exercise");
		db = new Database(this, "profiles.db");
		
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDatePickerDialog(v);
            }
        });		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.w_activity_edit_item, menu);
		return true;
	}

	public boolean onOptionsItemSelected (MenuItem item) {
		if (item.getItemId() == R.id.menu_ok) {
			saveData = true;
		} else if (item.getItemId() == R.id.menu_cancel) {
			saveData = false;
		}
		finish();
		return true;
	}

	public void onResume() {
		super.onResume();
		saveData = true;

		if (exercise != null) {

			EditText exer = (EditText) findViewById(R.id.exercise);
			EditText reps  = (EditText) findViewById(R.id.reps);
			EditText sets      = (EditText) findViewById(R.id.sets);
			EditText dt      = (EditText) findViewById(R.id.date);
			
			Selector sel = db.selector(tableName);
			sel.addColumns(new String[] { "exercise", "reps", "sets", "date" });
			sel.where("exercise == ?", new String[] {exercise});
			sel.execute();
			ResultSet cursor = sel.getResultSet();
			while (cursor.moveToNext()) {
				exer.setText(cursor.getString(0));
				reps.setText(cursor.getString(1));
				sets.setText(cursor.getString(2));
				dt.setText(cursor.getString(3));				
			}
			cursor.close();
		}

	}
	
	public void onPause() {
		super.onPause();
		if (saveData) {

			EditText exercise_e = (EditText) findViewById(R.id.exercise);
			EditText reps_e  = (EditText) findViewById(R.id.reps);
			EditText sets_e = (EditText) findViewById(R.id.sets);
			EditText date_e = (EditText) findViewById(R.id.date);			

			String exer = exercise_e.getText().toString();	
			String reps = reps_e.getText().toString();
			String sets = sets_e.getText().toString();
			String date = date_e.getText().toString();

			if (exer != null) {
				Deleter del = db.deleter(tableName);
				del.where("exercise = ?", new String[] { exercise });
				del.execute();
			}

			ContentValues values = new ContentValues();
			values.put("exercise", exer);
			values.put("reps", reps);
			values.put("sets", sets);
			values.put("date", date);
			Inserter ins = db.inserter(tableName);
			ins.columnNameValues(values);
			ins.execute();

		}
	}

	public void insert(ContentValues values) {
		Inserter ins = db.inserter(tableName);
		ins.columnNameValues(values);
		ins.execute();
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		db.close();
	}
	
	@SuppressLint("NewApi")
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePicker();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	public void onDialogPositiveClick(DialogFragment dialog) {
		System.out.print("Positive Clicked");	
		
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		System.out.print("Negative Clicked");		
	}

	public void onDateSet(android.widget.DatePicker view, int year,
			int monthOfYear, int dayOfMonth) {
		EditText dt      = (EditText) findViewById(R.id.date);
		String date = monthOfYear + "/" + dayOfMonth + "/" + year;
		dt.setText(date);
	}	

}
