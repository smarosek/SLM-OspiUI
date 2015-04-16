package com.slm_ospiui;

//import java.util.Calendar;

//import de.greenrobot.event.EventBus;
import com.slm_ospiui.CustomListAdapter.ViewHolder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * 
 * @author Susan Marosek TimerPickerDialogFragment - Custom Picker
 *         DialogFragment that displays two NumberPickers, one for minutes and
 *         one for seconds, for the sprinkler timer for the manual mode
 *         operation. (Chronometer only counts up). Inflates
 *         timer_picker_frag_dialog.xml
 */

/*
 * Modification History:
 * 
 * 02/19/15 Created.
 */

public class TimerPickerDialogFragment extends DialogFragment {
	private final String LOGTAG = TimerPickerDialogFragment.class
			.getSimpleName();
	private final String LOGTAG_POS = "HolderPos";
	Context context;

	NumberPicker mSecondsNP;
	NumberPicker mMinutesNP;

	final ViewHolder mHolder;

	int mMinutes = 1;
	int mSeconds = 0;

	public TimerPickerDialogFragment(ViewHolder holder) {
		mHolder = holder;
		Log.d(LOGTAG_POS,
				"Constructing TimerPickerDialogFragment - mHolder.pos = "
						+ mHolder.position);

		mMinutes = holder.cirDurationSTTV.GetMinutesValue();
		mSeconds = holder.cirDurationSTTV.GetSecondsValue();

		Log.d(LOGTAG, "mMinutes = " + mMinutes + "   mSeconds = " + mSeconds);

		// SLM2 mMinutes = mHolder.listItem.GetDDurationMin();
		// SLM2 mSeconds = mHolder.listItem.GetDDurationSec();

		// SLM2 Log.d(LOGTAG, "mMinutes = "+mMinutes + "   mSeconds = " +
		// mSeconds );

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d(LOGTAG, "In TimerPickerDialogFragment onCreateDialog");

		// Try setting time elements to 01:00 time as the default values for
		// the picker (1 minute is what I'm shooting for)
		// int minutes = 1;
		// int seconds = 0;

		// Create a new instance of TimePickerDialog and return it (we are
		// faking this for now)
		// WAS return new TimePickerDialog(getActivity(), this, minutes,
		// seconds, true);

		// get context
		context = getActivity().getApplicationContext();
		// make dialog object
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Set Timer (mm:ss)")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						mMinutes = mMinutesNP.getValue();
						mSeconds = mSecondsNP.getValue();

						// WAS 2/26 STimerView timerView = (STimerView)
						// getActivity().findViewById(R.id.chronometer);
						STimerView timerView = mHolder.cirChronoSTV;
						timerView.SetDuration(mMinutes, mSeconds);

						// This method sets the ListItem's durationDisp value
						// mHolder.SetDurationMs(mMinutes, mSeconds);

						// Log.d(LOGTAG_POS,
						// "$$$ In TimerPickerDialogFrag mHolder.listItem = "+mHolder.listItem.toStringAll());

						// This one will eventually go away
						// WAS 2/26 STimerView timerDurationView = (STimerView)
						// getActivity().findViewById(R.id.duration);
						STimeTextView timerDurationView = mHolder.cirDurationSTTV;
						timerDurationView.SetTime(mMinutes, mSeconds);

					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		// get the layout inflater
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate our custom layout for the dialog to a View
		View view = li.inflate(R.layout.timer_picker_frag_dialog, null);

		// inform the dialog it has a custom View
		builder.setView(view);
		// and if you need to call some method of the class

		// Might want to define a new widget NumberPicker for 2 digit value
		// between 0 & 59
		mMinutesNP = (NumberPicker) view.findViewById(R.id.minutesPicker);
		mMinutesNP.setMinValue(0);
		mMinutesNP.setMaxValue(59);
		mMinutesNP.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return String.format("%02d", value);
			}
		});

		mSecondsNP = (NumberPicker) view.findViewById(R.id.secondsPicker);
		mSecondsNP.setMinValue(0);
		mSecondsNP.setMaxValue(59);
		mSecondsNP.setFormatter(new NumberPicker.Formatter() {
			@Override
			public String format(int value) {
				return String.format("%02d", value);
			}
		});

		mMinutesNP.setValue(mMinutes);
		mSecondsNP.setValue(mSeconds);

		// create the dialog from the builder then show
		return builder.create();

	}

	/**
	 * @return the minutes
	 */
	/*
	 * public int getMinutes() { return mMinutes; }
	 */
	/**
	 * @return the seconds
	 */
	/*
	 * public int getSeconds() { return mSeconds; }
	 */
	/*
	 * @Override public void onTimeSet(TimePicker view, int hourOfDay, int
	 * minute) { Log.d(LOGTAG, "onTimeSet"); Log.d(LOGTAG,
	 * hourOfDay+":"+minute);
	 * 
	 * 
	 * // Convert 24 hour time format to 12 hour format int ampm = 0; String
	 * padHr = "0"; String padMin = "";
	 * 
	 * if ( hourOfDay >= 12) { ampm = 1; hourOfDay-=12; if ( hourOfDay > 9 )
	 * padHr = ""; } // We set pad to null here so that in append, below, when
	 * hourOfDay == 0, 12 is // appended w/o the 0 padding if ( hourOfDay == 0 )
	 * padHr = "";
	 * 
	 * if ( minute < 10 ) padMin = "0";
	 * 
	 * TextView timeView = (TextView)
	 * getActivity().findViewById(R.id.edit_time); timeView.setText(new
	 * StringBuilder()
	 * .append(padHr).append((hourOfDay==0)?12:hourOfDay).append(":")
	 * .append(padMin).append(minute).append(" ") .append((ampm==0)?"AM":"PM"));
	 * 
	 * }
	 * 
	 * 
	 * @Override public String format(int value) { return String.format("%02d",
	 * value); }
	 * 
	 * //}
	 */
}
