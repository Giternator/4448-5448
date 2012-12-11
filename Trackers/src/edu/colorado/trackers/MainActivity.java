package edu.colorado.trackers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button shoppingListButton;
	private Button healthMetricsButton;
	private Button workoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        shoppingListButton = (Button) findViewById(R.id.shopping_list_button);
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("Button pressed: shopping list");
				Intent intent = new Intent(MainActivity.this, edu.colorado.trackers.shoppinglist.SLMainActivity.class);
				startActivity(intent);
			}
		});
        
        healthMetricsButton = (Button) findViewById(R.id.health_metrics_button);
        healthMetricsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				System.out.println("Button pressed: health metrics");
				Intent intent = new Intent(MainActivity.this, edu.colorado.trackers.HealthMetrics.HealthMetrics.class);
				startActivity(intent);
			}
		});
        
        workoutButton = (Button) findViewById(R.id.workout_button);
        workoutButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("Button pressed: workout");
				Intent intent = new Intent(MainActivity.this, edu.colorado.trackers.workout.MainActivity.class);
				startActivity(intent);
			}
		});       
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
