package edu.colorado.trackers.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.colorado.trackers.R;
import edu.colorado.trackers.SendEmail;
import edu.colorado.trackers.workout.CancelDialog;
import edu.colorado.trackers.db.Database;
import edu.colorado.trackers.db.Deleter;
import edu.colorado.trackers.workout.EditWorkout;
import edu.colorado.trackers.workout.MainActivity;
import edu.colorado.trackers.db.ResultSet;
import edu.colorado.trackers.db.Selector;
import edu.colorado.trackers.graph.LineGraph;

import edu.colorado.trackers.workout.CancelDialog.CancleDialogListener;

import android.os.Bundle;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
public class MainActivity extends Activity implements CancleDialogListener {

	private ListView workoutProfiles;
	private int itemSelected = -1;
	private String tableName = "workout";
	private Database db;
	
	// Variables for graph
	List<Integer> yDB = new ArrayList<Integer>();
	List<String> dateDB = new ArrayList<String>();
	int yMin = 0, yMax = 0, xMax = 0;	
	PopupWindow popupWindow;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.w_activity_main);
		db = new Database(this, "trackers.db");
		boolean b = db.tableExists(tableName);
		System.out.print("table exist = " + b);
		if(!b) {
			db.createTable(tableName, " exercise TEXT NOT NULL, weight integer, reps INTEGER, sets INTEGER, dt TEXT NOT NULL");
		}
		workoutProfiles = (ListView) findViewById(R.id.exercise_list);
		workoutProfiles.setAdapter(getWorkouts());
		
		// On item click, go to edit screen
        workoutProfiles.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View list_item, int position, long id) {
            		MainActivity.this.itemSelected = position;
            		String exer = (String) getListMap().get("exercise");
            		String dt = (String) getListMap().get("date");
            		Intent intent = new Intent(MainActivity.this, EditWorkout.class); 
         			intent.putExtra("exercise", exer);
         			intent.putExtra("date", dt);
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
        	workoutGraph();
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
					workouts += cursor.getString(0) + "---" + cursor.getString(1) + 
							   "---" + cursor.getString(2) + "---" + cursor.getString(3) + "\n";
					System.out.println("Got exercise: " + cursor.getString(0));
					
				}
				cursor.close();
			}
			
			String subject = new String("workout log");
			SendEmail s= new SendEmail(subject, workouts);
			s.show(getFragmentManager(), "Email workout");
		}
		return true;
	}
	
    public void onResume() {
        super.onResume();
		workoutProfiles.setAdapter(getWorkouts());
    }

	public SimpleAdapter getWorkouts() {

		ArrayList<HashMap<String, String>> mylistData =
                new ArrayList<HashMap<String, String>>();

		String[] columnTags = new String[] {"exercise", "weight", "date"};

		int[] columnIds = new int[] {R.id.exercise, R.id.weight, R.id.date};

		
		HashMap<String,String> map = new HashMap<String, String>();
		String[] str = {"exercise", "weight", "max(dt)"};
		Selector select = db.selector(tableName);
		select.addColumns(str);
		
		int count;
		Selector sel1 = db.selector(tableName);
		sel1.addColumns(new String[] {"distinct(exercise)"});
		int innercount = sel1.execute();
		if (innercount != 0) {
			String[] strArr = new String[innercount];
			ResultSet cur = sel1.getResultSet();
			int i = 0;
			while (cur.moveToNext()) {
				strArr[i] = cur.getString(0);
				i++;
			}
			for (i = 0; i < innercount; i++) {
				select.where("exercise = ? ", new String[] {strArr[i]});
				count = select.execute();
				if(count != 0) {
					ResultSet cursor = select.getResultSet();
			
					while (cursor.moveToNext()) {
						map = new HashMap<String, String>();	
						map.put(columnTags[0], cursor.getString(0));
						map.put(columnTags[1], cursor.getString(1));
						map.put(columnTags[2],cursor.getString(2));
						mylistData.add(map);
					}
					cursor.close();
				}
			}
		}
		else {
			count = select.execute();
			if(count != 0) {
				ResultSet cursor = select.getResultSet();
		
				while (cursor.moveToNext()) {
					map = new HashMap<String, String>();	
					map.put(columnTags[0], cursor.getString(0));
					map.put(columnTags[1], cursor.getString(1));
					map.put(columnTags[2],cursor.getString(2));
					mylistData.add(map);
				}
				cursor.close();
			}
		}
		if (map.isEmpty()) {
				map.put(columnTags[0], "No exercises");			
				map.put(columnTags[1], "");	
				map.put(columnTags[2], "");	
		} 

		SimpleAdapter arrayAdapter =
	            new SimpleAdapter(this, mylistData, R.layout.w_listrow,
	                          columnTags , columnIds);		
		return arrayAdapter;
	}

	public void remove_workout(String exercise, String date) {
        Deleter del = db.deleter(tableName);
        del.where("exercise = ? and dt = ?",  new String[] { exercise, date });
        del.execute();

        SimpleAdapter adapter = getWorkouts();
        adapter.notifyDataSetChanged();
		workoutProfiles.setAdapter(adapter);
        itemSelected = -1;
	}

	public void onDialogNegativeClick(DialogFragment dialog) {
		System.out.print("Negative Clicked");
	}	
	
	public void onDialogPositiveClick(DialogFragment dialog) {

		HashMap<String,String>  map = getListMap();
        String exer = (String) map.get("exercise");
        String date = (String) map.get("date");
        remove_workout(exer, date);		
	}

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CancelDialog(); 
        dialog.show(getFragmentManager(), "CancelDialog");
    }
    
    public void workoutGraph()
    {
    	LineGraph line = new LineGraph();
		HashMap<String,String>  map = getListMap();
        String exer = (String) map.get("exercise");
        
    	if(getDBValues(exer) == 1)
		{
    		//display graph
			Intent lineIntent = line.getIntent(this, exer, dateDB, yMin, yMax, xMax,  yDB );
			startActivity(lineIntent);
		}
		else
		{
			//no data popup
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
			Button b = (Button) findViewById(R.id.chGraph);
			b.setEnabled(false);
		}
    }
    
	@SuppressWarnings("unchecked")
	HashMap<String, String> getListMap() {
		SimpleAdapter adapter = (SimpleAdapter) workoutProfiles.getAdapter();
		return (HashMap<String,String>) adapter.getItem(itemSelected);
	}   
    
    /*get db value to be displayed on graph*/
	public int getDBValues(String exer) 
	{
		yDB.clear();
    	yMin = 0; yMax = 0; xMax = 0;
    	dateDB.clear();
    	Selector selector = db.selector(tableName);       //give your table name here
    	selector.addColumns(new String[] { "weight", "dt"});
    	if(!exer.equals(null))
    		selector.where("exercise = ?", new String[] {exer}); 
    	int count = selector.execute();
    	System.out.println("Selected (" + count + ") items");
    	ResultSet cursor = selector.getResultSet();

    	if(cursor.getCount() != 0)
    	{
    		cursor.moveToLast();
    		while (!cursor.isBeforeFirst()) 
    		{
    			Integer reading = cursor.getInt(0); 
    			String date = cursor.getString(1);
    			if(yMin == 0)							//set the ymin, ymax values to be displayed on graph
    				yMin = reading;
    			if(reading < yMin)
    				yMin = reading;
    			if(reading > yMax)
    				yMax = reading;
    			yDB.add(reading);					//add reading and date value to the dataset   
    			dateDB.add(date);
    			System.out.println("GraphActivity:   Data: "+ reading);
    			cursor.moveToPrevious();
    		}
    		cursor.close();
    		return 1;
    	}
    	else
    		return 0;
    }
       

}

