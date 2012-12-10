package com.example.healthmetrics;

import android.content.Context;

public class Sugar extends Metric
{
	private Database db;
	String title;
	int lastReading;
	
	public int check(Context context, int age)
	{
		db = new Database(context, "healthmetric15.db");
		title = "Sugar";
		getLatestReading();
		
		//get the latest value stored in database
		if(lastReading == 0)
			return 2;
		if(lastReading >= 80 && lastReading <= 120)
				return 1;
		else
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
    		System.out.println("cursor not empty");
    		cursor.moveToLast();
    		lastReading = cursor.getInt(1);
    		cursor.close();
    	}
    	else
    		lastReading = 0;

    	System.out.println("last reading " + lastReading);

    }
}
