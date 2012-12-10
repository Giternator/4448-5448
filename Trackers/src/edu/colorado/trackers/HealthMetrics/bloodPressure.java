package com.example.healthmetrics;

import android.content.Context;

public class bloodPressure extends Metric
{
	private Database db;
	String title;
	int lastReading;
	
	public int check(Context context, int age)
	{
		db = new Database(context, "healthmetric15.db");
		title = "BloodPressure";
		getLatestReading();
		
		//get the latest value stored in database
		if(lastReading == 0)   //if no record
			return 2;
		if(age >= 1 && age <= 19)
		{
				if(lastReading >= 105 && lastReading <= 120)
					return 1;
				else
					return 0;
		}
		if(age >= 20 && age <= 29)
		{
				if(lastReading >= 108 && lastReading <= 133)
					return 1;
				else
					return 0;
		}
		if(age >= 30 && age <= 39)
		{
				if(lastReading >= 110 && lastReading <= 135)
					return 1;
				else
					return 0;
		}
		if(age >= 40 && age <= 49)
		{
				if(lastReading >= 112 && lastReading <= 139)
					return 1;
				else
					return 0;
		}
		if(age >= 50 && age <= 59)
		{
				if(lastReading >= 116 && lastReading <= 144)
					return 1;
				else
					return 0;
		}
		if(age >= 60)
		{
				if(lastReading >= 121 && lastReading <= 147)
					return 1;
				else
					return 0;
		}
		return 0;
	}
	@SuppressWarnings("unused")
	public void getLatestReading() 			//get the latest reading from db 
	{
    	Selector selector = db.selector("healthMetrics15");       //give your table name here
    	selector.addColumns(new String[] { "id", "reading", "date"});
   		selector.where("type = ?", new String[] {title}); 
    	int count = selector.execute();
    	System.out.println("Selected (" + count + ") items");
    	ResultSet cursor = selector.getResultSet();
    	
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
