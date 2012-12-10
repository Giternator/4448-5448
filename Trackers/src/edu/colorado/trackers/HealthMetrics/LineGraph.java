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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

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
	
	public Intent getIntent(Context context, String database, String type, XYMultipleSeriesDataset dataset1) 
	{	
		title = type;
		Series = new TimeSeries(title);	
		
		if(database.equals("healthMetric"))
			db = new Database(context, "healthmetric15.db");
		else if(database.equals("workout"))
			db = new Database(context, "healthmetric15.db");
		if(database.equals("shopping"))
			db = new Database(context, "Shopping.db");
		
		setDataset();	
		setRender();
		Intent intent = ChartFactory.getLineChartIntent(context, dataset1, mRenderer, title);
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
    	Selector selector = db.selector("healthMetrics15");       //give your table name here
    	selector.addColumns(new String[] { "id", "reading", "date"});
    	if(!title.equals(null))
    		selector.where("type = ?", new String[] {title}); 
    	//selector.orderBy("id");
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
    			System.out.println("Data: "+ reading);
    			cursor.moveToPrevious();
    		}
    	}
    	cursor.close();
    }
	


}
