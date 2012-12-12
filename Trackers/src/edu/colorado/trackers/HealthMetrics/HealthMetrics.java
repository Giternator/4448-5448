package edu.colorado.trackers.HealthMetrics;

import edu.colorado.trackers.MainActivity;
import edu.colorado.trackers.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//healthMetrics Menu activity
public class HealthMetrics extends Activity 
{
	public final static String EXTRA_MESSAGE = "com.example.healthmetrics.MESSAGE";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_metrics);  //healthMetrics Main menu
    }
    /** Called when the user clicks any metric button */
    public void selectMetric(View view) {
    	Intent intent = new Intent(this, DisplayMetric.class);
    	Button b = (Button)view;
        String buttonText = b.getText().toString();       //find out which button was clicked
    	intent.putExtra(EXTRA_MESSAGE, buttonText);       //send the button text to the next activity
    	startActivity(intent);                            //start the Display metric activity
    }
   
    /** Called when the user clicks the Check button */
    public void goMainMenu(View view) {
    	Intent intent = new Intent(this, MainActivity.class);
    	String message = "HealthMetrics";
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);                            //start the  metric activity
    }
    /** Called when the user clicks the Check button */
    public void callCheck(View view) {
    	Intent intent = new Intent(this, DisplayCheck.class);
    	String message = "Check";
    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);                            //start the check metric activity
    }
    /** Called when the user clicks the Graph button */
    public void lineGraphHandler (View view)
    {
    	Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);                           //start the Graph activity
    }
}
