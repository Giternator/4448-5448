package com.example.healthmetrics;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class LineGraph{

	private Database db;
	
	List<Integer> yDB = new ArrayList<Integer>();
	List<String> dateDB = new ArrayList<String>();
	int yMin = 0, yMax = 0;
	int xMax = 0;
	
	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	TimeSeries Series;
	
	String title;
	
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); 
	
	public Intent getIntent(Context context, String database, String type) 
	{	
		title = type;
		Series = new TimeSeries(title);	
		
		if(database.equals("healthMetric"))
			db = new Database(context, "healthmetric14.db");
		else if(database.equals("workout"))
			db = new Database(context, "healthmetric14.db");
		if(database.equals("shopping"))
			db = new Database(context, "Shopping.db");
		
		setDataset();	
		setRender();
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, title);
		return intent;	
	}
	
	public void setRender()
	{
		XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize line 1
		for (int i = 0; i < dateDB.size(); i++) 
	    { 
		    mRenderer.addXTextLabel(i+1,dateDB.get(i));
	    }
		mRenderer.setXLabelsAlign(Align.CENTER);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		
		mRenderer.setLabelsColor(Color.WHITE);
		mRenderer.setLabelsTextSize(15);
		
		mRenderer.setLegendTextSize(15);
		
		mRenderer.setXLabels(0);	
		mRenderer.setShowCustomTextGrid(true);
		mRenderer.addSeriesRenderer(renderer);	
		
		mRenderer.setXAxisMax(5); // set the new values
        mRenderer.setXAxisMin(0);
        mRenderer.setYAxisMax(yMax + 1); // set the new values
        mRenderer.setYAxisMin(yMin - 1);
      
        mRenderer.setPanLimits(new double[] { 0, xMax, yMin - 1, yMax + 1 });        //limits the values displayed scroll
        mRenderer.getSeriesRendererCount();
        int length = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < dateDB.size(); i++) {
            SimpleSeriesRenderer seriesRenderer = mRenderer.getSeriesRendererAt(0);
            seriesRenderer.setDisplayChartValues(true);
        }
        renderer.setColor(Color.RED);
        renderer.setPointStyle(PointStyle.SQUARE);
        renderer.setFillPoints(true);
	}
	public void setDataset()
	{
		getDBValues();
		int x = 1;
		//yDB.
		for(int yfromdb : yDB)
		{
			Series.add(x, yfromdb);
			x++;
		}	
		xMax = x;
		dataset.addSeries(Series);
		
	}
	public void getDBValues() 
	{
		yDB.clear();
    	Selector selector = db.selector("healthMetrics14");
    	selector.addColumns(new String[] { "id", "reading", "date"});
    	if(!title.equals(null))
    		selector.where("type = ?", new String[] {title}); 
    	selector.orderBy("id");
    	int count = selector.execute();
    	System.out.println("Selected (" + count + ") items");
    	ResultSet cursor = selector.getResultSet();

    	while (cursor.moveToNext()) {
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
    		System.out.println("Data: "+ reading);
    	}
    	cursor.close();
    }
}
