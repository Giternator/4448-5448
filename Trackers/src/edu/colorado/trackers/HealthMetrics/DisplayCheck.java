package edu.colorado.trackers.healthmetrics

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayCheck extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_check);
        
        clear((View) findViewById(R.id.TableValue));

        EditText e = (EditText) findViewById(R.id.age);
		e.addTextChangedListener(new LocalTextWatcher());   //set watcher for age input
    }

    private boolean validateFields() 
    {
    	try 
		{
    		EditText e = (EditText) findViewById(R.id.age);
    		Integer ei = Integer.parseInt(e.getText().toString());
	
    		if (ei < 1) //if age invalid entry
    		{
    			e.setError("invalid entry");
    			return false;
    		}
    		else 
    		{
    			e.setError(null);	
    			return true; 
    		}
		}
    	catch (NumberFormatException e) 
		{
				System.out.println("Catch ");
		}
    	return false;
	}
    
    private class LocalTextWatcher implements TextWatcher {
		public void afterTextChanged(Editable s) 
		{   
			clear((View) findViewById(R.id.TableValue));
			if(validateFields())
			{
				set((View) findViewById(R.id.TableValue));
				check((View) findViewById(R.id.TextView21));	//check each metric
				check((View) findViewById(R.id.TextView31));
				check((View) findViewById(R.id.TextView41));
				check((View) findViewById(R.id.TextView51));
				check((View) findViewById(R.id.TextView61));
			}
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
	}
    
    public void clear(View view)
    {
    	view.setVisibility(View.GONE);
    }
    public void set(View view)
    {
    	view.setVisibility(View.VISIBLE);
    }
    
    public void check(View view) {
    	int iage = 0;
    	
    	TextView tage = (TextView) findViewById(R.id.age);
    	iage = Integer.parseInt(tage.getText().toString());
    	
    	Metric m = null;
    	Button b = null;
    	switch(view.getId())
    	{
    		case R.id.TextView21:
    				m = new Cholesterol();
    				b = (Button) findViewById(R.id.TextView22);
    				break;
            case R.id.TextView31:
            		m = new bloodPressure();
            		b = (Button) findViewById(R.id.TextView32);
            		break;
            case R.id.TextView41:
					m = new Sugar();
					b = (Button) findViewById(R.id.TextView42);
					break;
            case R.id.TextView51:
        			m = new Temperature();
        			b = (Button) findViewById(R.id.TextView52);
        			break;
            case R.id.TextView61:
            		m = new HeartRate();
            		b = (Button) findViewById(R.id.TextView62);
            		break;
    	}
    	int ret = m.check(this, iage);
    	b.setVisibility(View.VISIBLE);
    	if(ret == 0)
    	{
    		b.setVisibility(View.VISIBLE);
            b.setBackgroundResource(R.drawable.red);		//not normal
    	}
    	else if(ret == 1)
    	{
    		b.setVisibility(View.VISIBLE);
            b.setBackgroundResource(R.drawable.green);	   //normal
    	}
    	else if(ret == 2)
    	{
    		view.setVisibility(View.GONE);				  //no reading stored for this metric
            b.setVisibility(View.GONE);
    	}
    }

    /** Called when the user clicks the done button */
    public void done(View view) 
    {
        	Intent intent = new Intent(this, HealthMetrics.class);		//go back to metric menu
        	startActivity(intent);
    }
}
