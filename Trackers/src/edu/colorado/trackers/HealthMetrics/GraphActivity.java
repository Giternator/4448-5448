package com.example.healthmetrics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GraphActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
    }
    
    public void CholesterolGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(this, "healthMetric", "Cholesterol");
        startActivity(lineIntent);
    }
    
    public void BloodPressureGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(this, "healthMetric", "BloodPressure");
        startActivity(lineIntent);
    }
    
    public void SugarGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(this, "healthMetric", "Sugar");
        startActivity(lineIntent);
    }
    public void TemperatureGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(this, "healthMetric", "Temperature");
        startActivity(lineIntent);
    }
    public void HeartRateGraph (View view)
    {
    	LineGraph line = new LineGraph();
    	Intent lineIntent = line.getIntent(this, "healthMetric", "HeartRate");
        startActivity(lineIntent);
    }
    /** Called when the user clicks the done button */
    public void done(View view) {
    	Intent intent = new Intent(this, HealthMetrics.class);
        String message = "more data";
       	intent.putExtra("from Graph", message);
       	startActivity(intent);
    }
    
}