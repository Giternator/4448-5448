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
	XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); 

	public Intent getIntent(Context context, String type, List<String> dateDB, int yMin, int yMax, int xMax, List<Integer> yDB ) 
	{	
		this.dateDB = dateDB;
		this.yDB = yDB;
		this.yMax = yMax;
		this.yMin = yMin;
		this.xMax = xMax;
		
		Series = new TimeSeries(type);	
		setDataset();
		setRender();
		Intent intent = ChartFactory.getLineChartIntent(context, dataset, mRenderer, type);
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
