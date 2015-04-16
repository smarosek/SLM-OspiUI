package com.slm_ospiui;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;


/*
 * Modification History:
 * 	
 * 	03/31/15 Modified to allow for specification of 24 hour time format.
 *
 */
public class TimePickerDialogFragment extends DialogFragment
					implements TimePickerDialog.OnTimeSetListener
{

	private final String LOGTAG = TimePickerDialogFragment.class.getSimpleName();

	boolean mIs24HourFormat = false;
	
	public TimePickerDialogFragment()
    {
		super();
		mIs24HourFormat = DateFormat.is24HourFormat(getActivity());
    }
	
	public TimePickerDialogFragment( boolean is24Hour )
    {
		super();
		mIs24HourFormat = is24Hour;
		
    }


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, 
				mIs24HourFormat );
//Was				DateFormat.is24HourFormat(getActivity()));

	}


	public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
	{
		Log.d(LOGTAG, "onTimeSet");
		Log.d(LOGTAG, hourOfDay+":"+minute);

		String padHr = "0";
		String padMin = "";

		if ( mIs24HourFormat)
		{
			if ( hourOfDay > 9 )
				padHr = "";
			
			// We set pad to null here so that in append, below, when hourOfDay == 0, 12 is 
			// appended w/o the 0 padding
		//	if ( hourOfDay == 0 )
		//		padHr = "";
	
			if ( minute < 10 )
				padMin = "0";
			
	
			
			STimeTextView timeView = (STimeTextView) getActivity().findViewById(R.id.starttime_sttv);
			timeView.setText(new StringBuilder()
				.append(padHr).append((hourOfDay==0)?12:hourOfDay).append(":")
				.append(padMin).append(minute).append(" "));
				
		}
		else
		{
			// Convert 24 hour time format to 12 hour format
			int ampm = 0;
			
	
			if ( hourOfDay >= 12)
			{
				ampm = 1;
				hourOfDay-=12;
				if ( hourOfDay > 9 )
					padHr = "";
			}
			// We set pad to null here so that in append, below, when hourOfDay == 0, 12 is 
			// appended w/o the 0 padding
			if ( hourOfDay == 0 )
				padHr = "";
	
			if ( minute < 10 )
				padMin = "0";
	
			STimeTextView timeView = (STimeTextView) getActivity().findViewById(R.id.starttime_sttv);
			timeView.setText(new StringBuilder()
				.append(padHr).append((hourOfDay==0)?12:hourOfDay).append(":")
				.append(padMin).append(minute).append(" ")
				.append((ampm==0)?"AM":"PM"));
		}

	}

}