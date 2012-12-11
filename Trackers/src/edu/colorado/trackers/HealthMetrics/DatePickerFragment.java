package edu.colorado.trackers.HealthMetrics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
 
public class DatePickerFragment extends DialogFragment{
	int     setDay;
    int     setMonth;
    int     setYear;
    Handler msgHandler;
 
    public DatePickerFragment(Handler msg)
    {
        msgHandler = msg;	//get the value sent by caller
    }
 
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle b  = getArguments();
        setDay    = b.getInt("pickedDay");
        setMonth  = b.getInt("pickedMonth");
        setYear   = b.getInt("pickedYear");
        DatePickerDialog.OnDateSetListener listener  = new DatePickerDialog.OnDateSetListener() 
        {
            @Override
            public void onDateSet(DatePicker view, int pickedYear, int pickedMonth, int pickedDay) {
                setDay    = pickedDay;
                setMonth  = pickedMonth;
                setYear   = pickedYear;
                Bundle b  = new Bundle();		//set the return value
                b.putInt("pickedDay",   setDay);
                b.putInt("pickedMonth", setMonth);
                b.putInt("pickedYear",  setYear);
                Message m = new Message();
                m.setData(b);
                msgHandler.sendMessage(m);
            }
        };
        return new DatePickerDialog(getActivity(), listener, setYear, setMonth, setDay);
    }
}
