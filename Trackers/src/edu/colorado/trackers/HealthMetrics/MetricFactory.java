package com.example.healthmetrics;

public class MetricFactory 
{
	public static Metric createMetric (String metric) 
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
	              return new bloodPressure();
	       }
	       else if(metric. equalsIgnoreCase ("Temperature"))
	       {
	              return new bloodPressure();
	       }
	       else if(metric. equalsIgnoreCase ("HeartRate"))
	       {
	              return new bloodPressure();
	       }
	       throw new IllegalArgumentException("No such metric");
	}
}
