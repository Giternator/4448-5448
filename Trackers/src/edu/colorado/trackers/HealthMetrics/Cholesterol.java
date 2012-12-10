package com.example.healthmetrics;

import android.content.Context;

public class Cholesterol extends Metric
{
	private Database db;
	String title;
	int lastReading;
	
	public int check(Context context, int age)
	{
		db = new Database(context, "healthmetric15.db");
		title = "Cholesterol";
		getLatestReading();
		
		//get the latest value stored in database
		if(lastReading == 0)
			return 2;
		if(age >= 20 && age <= 29)
		{
				if(lastReading <= 180)
					return 1;
				else
					return 0;
		}
		if(age >= 30)
		{
				if(lastReading <= 200)
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
