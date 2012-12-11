package edu.colorado.trackers.HealthMetrics;

import edu.colorado.trackers.db.Database;
import edu.colorado.trackers.db.ResultSet;
import edu.colorado.trackers.db.Selector;
import android.content.Context;

public class Temperature extends Metric
{
	private Database db;
	String title;
	int lastReading;
	
	public int check(Context context, int age)
	{
		db = new Database(context, "healthmetric15.db");
		title = "Temperature";
		getLatestReading();
		
		//get the latest value stored in database
		if(lastReading == 0)
			return 2;
		if(age >= 1 && age <= 2)
		{
				if(lastReading >= 97 && lastReading <= 100)
					return 1;
				else
					return 0;
		}
		if(age >= 3 && age <= 10)
		{
				if(lastReading >= 97 && lastReading <= 100)
					return 1;
				else
					return 0;
		}
		if(age >= 11 && age <= 65)
		{
				if(lastReading >= 98 && lastReading <= 100)
					return 1;
				else
					return 0;
		}
		if(age >= 65)
		{
				if(lastReading >= 96 && lastReading <= 98)
					return 1;
				else
					return 0;
		}
		return 0;
	}
	public void getLatestReading() //get the latest reading from db 
	{
    	Selector selector = db.selector("healthMetrics15");       //give your table name here
    	selector.addColumns(new String[] { "id", "reading", "date"});
   		selector.where("type = ?", new String[] {title}); 
    	int count = selector.execute();
    	System.out.println("Selected (" + count + ") items");
    	ResultSet cursor = selector.getResultSet();
    	
    	//cursor = null;
    	if(cursor.getCount() != 0)
    	{
    		cursor.moveToLast();
    		lastReading = cursor.getInt(1);
    		cursor.close();
    	}
    	else
    		lastReading = 0;

    	System.out.println("last reading " + lastReading);
   
    }
}
