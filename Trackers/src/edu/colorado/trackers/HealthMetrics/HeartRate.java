package com.example.healthmetrics;

import android.content.Context;

public class HeartRate extends Metric
{
	private Database db;
	String title;
	int lastReading;
	
	public int check(Context context, int age)
	{
		db = new Database(context, "healthmetric15.db");
		title = "HeartRate";
		getLatestReading();
		
		//get the latest value stored in database
		if(lastReading == 0)
			return 2;
		if(age >= 1 && age <= 19)
		{
				if(lastReading >= 100 && lastReading <= 164)
					return 1;
				else
					return 0;
		}
		if(age >= 20 && age <= 29)
		{
				if(lastReading >= 120 && lastReading <= 162)
					return 1;
				else
					return 0;
		}
		if(age >= 30 && age <= 39)
		{
				if(lastReading >= 114 && lastReading <= 153)
					return 1;
				else
					return 0;
		}
		if(age >= 40 && age <= 49)
		{
				if(lastReading >= 108 && lastReading <= 145)
					return 1;
				else
					return 0;
		}
		if(age >= 50 && age <= 59)
		{
				if(lastReading >= 102 && lastReading <= 136)
					return 1;
				else
					return 0;
		}
		if(age >= 60 && age <= 75)
		{
				if(lastReading >= 96 && lastReading <= 123)
					return 1;
				else
					return 0;
		}
		if(age > 75)
		{
				if(lastReading >= 87 && lastReading <= 116)
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
