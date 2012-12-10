package com.example.healthmetrics;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;


public class GraphActivity extends Activity {
	
	private Database db;
	List<Integer> yDB = new ArrayList<Integer>();
	List<String> dateDB = new ArrayList<String>();
	int yMin = 0, yMax = 0;
	int xMax = 0;
	
	XYMultipleSeriesDataset Cholesteroldataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesDataset BPdataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesDataset Sugardataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesDataset Tempdataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesDataset HRdataset = new XYMultipleSeriesDataset();
	TimeSeries Series;
	String title;
	PopupWindow popupWindow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        db = new Database(this, "healthmetric15.db");
    }
    
    public void CholesterolGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	title = "Cholesterol";
    	if(getDBValues() == 1)
		{
    		Cholesteroldataset = setDataset();
			Intent lineIntent = line.getIntent(this, "healthMetric", "Cholesterol", Cholesteroldataset);
			startActivity(lineIntent);
		}
		else
		{
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
			Button b = (Button) findViewById(R.id.chGraph);
			b.setEnabled(false);
		}
    }
    
    public void BloodPressureGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	title = "BloodPressure";
		if(getDBValues() == 1)
		{
			BPdataset = setDataset();
			Intent lineIntent = line.getIntent(this, "healthMetric", "BloodPressure", BPdataset);
			startActivity(lineIntent);
		}
		else
		{
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
			Button b = (Button) findViewById(R.id.bpGraph);
			b.setEnabled(false);
		}
    }
    
    public void SugarGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	title = "Sugar";
    	if(getDBValues() == 1)
		{
    		Sugardataset = setDataset();
			Intent lineIntent = line.getIntent(this, "healthMetric", "Sugar", Sugardataset);
			startActivity(lineIntent);
		}
		else
		{
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
			Button b = (Button) findViewById(R.id.suGraph);
			b.setEnabled(false);
		}
    }
    public void TemperatureGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	title = "Temperature";
    	if(getDBValues() == 1)
		{
    		Tempdataset = setDataset();
			Intent lineIntent = line.getIntent(this, "healthMetric", "Temperature", Tempdataset);
			startActivity(lineIntent);
		}
		else
		{
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
			Button b = (Button) findViewById(R.id.tmGraph);
			b.setEnabled(false);
		}
    }
    public void HeartRateGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	title = "HeartRate";
    	if(getDBValues() == 1)
		{
    		HRdataset = setDataset();
			Intent lineIntent = line.getIntent(this, "healthMetric", "HeartRate", HRdataset);
			startActivity(lineIntent);
		}
		else
		{
			Button b = (Button) findViewById(R.id.hrGraph);
			b.setEnabled(false);
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.activity_popup, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		    popupWindow.showAtLocation(this.findViewById(R.id.hrGraph), Gravity.CENTER, 0, 0); 
		}
    }
    /** Called when the user clicks the done button */
    public void done(View view) {
    	Intent intent = new Intent(this, HealthMetrics.class);
        String message = "more data";
       	intent.putExtra("from Graph", message);
       	startActivity(intent);
    }
    /** Called when the user clicks the close button on popup window*/
    public void close(View view) {
    	popupWindow.dismiss();
    }
    
    public XYMultipleSeriesDataset setDataset()
	{
    	XYMultipleSeriesDataset dataset =  new XYMultipleSeriesDataset();
    	Series = new TimeSeries(title);	

		int x = 1;
		for(int yfromdb : yDB)
		{
			Series.add(x, yfromdb);
			x++;
		}	
		xMax = x;
		
		dataset.addSeries(Series);
		return dataset;
	}
	public int getDBValues() 
	{
		yDB.clear();
    	Selector selector = db.selector("healthMetrics15");       //give your table name here
    	selector.addColumns(new String[] { "id", "reading", "date"});
    	if(!title.equals(null))
    		selector.where("type = ?", new String[] {title}); 
    	int count = selector.execute();
    	System.out.println("Selected (" + count + ") items");
    	ResultSet cursor = selector.getResultSet();

    	//cursor = null;
    	if(cursor.getCount() != 0)
    	{
    		cursor.moveToLast();
    		while (!cursor.isBeforeFirst()) 
    		{
    			Integer reading = cursor.getInt(1); 
    			String date = cursor.getString(2);
    			if(yMin == 0)
    				yMin = reading;
    			if(reading < yMin)
    				yMin = reading;
    			if(reading > yMax)
    				yMax = reading;
    			yDB.add(reading);   
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