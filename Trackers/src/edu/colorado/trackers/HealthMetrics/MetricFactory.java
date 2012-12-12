package edu.colorado.trackers.HealthMetrics;

public class MetricFactory 
{
	public Metric createMetric (String metric) 
	{
	       if (metric. equalsIgnoreCase ("Cholesterol"))
	       {
	              return new Cholesterol();
	       }
	       else if(metric. equalsIgnoreCase ("bloodPressure"))
	       {
	              return new bloodPressure();
	       }
	       else if(metric. equalsIgnoreCase ("Sugar"))
	       {
	              return new Sugar();
	       }
	       else if(metric. equalsIgnoreCase ("Temperature"))
	       {
	              return new Temperature();
	       }
	       else if(metric. equalsIgnoreCase ("HeartRate"))
	       {
	              return new HeartRate();
	       }
	       throw new IllegalArgumentException("No such metric");
	}
}
