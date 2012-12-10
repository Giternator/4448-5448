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
	List<Integer> yDB     =   new ArrayList<Integer>();
	List<String> dateDB   =   new ArrayList<String>();
	int yMin = 0, yMax = 0, xMax = 0;
	TimeSeries Series;
	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer gRenderer = new XYMultipleSeriesRenderer(); 

	public Intent getIntent(Context context, String type, List<String> dateDB, int yMin, int yMax, int xMax, List<Integer> yDB ) 
	{	
		this.dateDB = dateDB;    //set the values set by the caller
		this.yDB = yDB;
		this.yMax = yMax;
		this.yMin = yMin;
		this.xMax = xMax;
		
		Series = new TimeSeries(type);	//series for graph	
		setDataset();					//set dataset series
		setRender();					//set renderer
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, gRenderer, type);
		return intent;	
	}
	
	public void setRender()
	{
		XYSeriesRenderer renderer = new XYSeriesRenderer(); 
		for (int i = 0; i < dateDB.size(); i++) 
	    { 
		    gRenderer.addXTextLabel(i+1,dateDB.get(i));
	    }
		gRenderer.setXLabelsAlign(Align.CENTER);
		gRenderer.setYLabelsAlign(Align.RIGHT);
		
		gRenderer.setLabelsColor(Color.WHITE);
		gRenderer.setLabelsTextSize(15);
		
		gRenderer.setLegendTextSize(15);
		
		gRenderer.setXLabels(0);	
		gRenderer.setShowCustomTextGrid(true);
		gRenderer.addSeriesRenderer(renderer);	
		
		gRenderer.setXAxisMax(5); // set the max, min for x & y 
        gRenderer.setXAxisMin(0);
        gRenderer.setYAxisMax(yMax + 1); 
        gRenderer.setYAxisMin(yMin - 1);
      
        gRenderer.setPanLimits(new double[] { 0, xMax, yMin - 1, yMax + 1 });        //limits the scroll values
        gRenderer.getSeriesRendererCount();

        for (int i = 0; i < dateDB.size(); i++) {
            SimpleSeriesRenderer seriesRenderer = gRenderer.getSeriesRendererAt(0);
            seriesRenderer.setDisplayChartValues(true);
        }
        renderer.setColor(Color.RED);
        renderer.setPointStyle(PointStyle.SQUARE);
        renderer.setFillPoints(true);
	}
	public void setDataset()
	{ 	 	
		int x = 1;
		for(int yfromdb : yDB)
		{
			Series.add(x, yfromdb);
			x++;
		}	
		xMax = x;		
		dataset.addSeries(Series);
		
	}
}
