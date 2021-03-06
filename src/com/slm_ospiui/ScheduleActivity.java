package com.slm_ospiui;


import com.slm.ospiui.model.OspiMessageHandler;


//import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Susan L Marosek
 * @version 0.1
 * @ScheduleActivity - Currently displays a crude screen layout to define a 
 * sprinkler schedule. 
 */

/*
 * Modification History:
 * 	
 * 	03/23/15 Created. 
 *	04/15/15 Added Life cycle methods which added EventBus register & 
 *				unregister calls. 
 *	04/16/15 Added handling for enabled and program id fields.
 *	05/26/15 Modified to use new OspiMessageHandler rather than implement 
 *				SendMessage and EventBus onEvent() in each Activity 
 *
 */

public class ScheduleActivity extends Activity implements OspiResultsListener
{
	private final String LOGTAG = ScheduleActivity.class.getSimpleName();
    private static final boolean DEBUG = true;
    
    private OspiMessageHandler mCPMsgHandler;
	
    // EVENTUALLY PUT THESE IN AN ARRAY
    CheckBox mMon;
    CheckBox mTue;
    CheckBox mWed;
    CheckBox mThu;
    CheckBox mFri;
    CheckBox mSat;
    CheckBox mSun;
    
 // EVENTUALLY PUT THESE IN AN ARRAY
    CheckBox mC1;
    CheckBox mC2;
    CheckBox mC3;
    CheckBox mC4;
    CheckBox mC5;
    CheckBox mC6;
    CheckBox mC7;
    CheckBox mC8;
    
    CheckBox mEnabledCB;
    EditText mPidET;
	Button mSetStartTimeBtn;
	Button mSubmitBtn;
	STimeTextView mStartTimeSTTV;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        //  Called when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_setup);
        
        
        //mCPMsgHandler = new OspiMessageHandler( this );
        
        // Define widgets
        mSetStartTimeBtn = (Button) findViewById( R.id.set_starttime_button);
        mSubmitBtn = (Button) findViewById( R.id.submit_button);
        mStartTimeSTTV = (STimeTextView) findViewById( R.id.starttime_sttv);
        mEnabledCB = (CheckBox) findViewById( R.id.enabled_cb);
        mPidET = (EditText) findViewById( R.id.prog_id_value_et);
        
        // Days of Week Checkboxes
        mMon = (CheckBox) findViewById( R.id.mon_cb);
        mTue = (CheckBox) findViewById( R.id.tue_cb);
        mWed = (CheckBox) findViewById( R.id.wed_cb);
        mThu = (CheckBox) findViewById( R.id.thu_cb);
        mFri = (CheckBox) findViewById( R.id.fri_cb);
        mSat = (CheckBox) findViewById( R.id.sat_cb);
        mSun = (CheckBox) findViewById( R.id.sun_cb);
        
        // Circuit CheckBoxes
        mC1 = (CheckBox) findViewById( R.id.c1_cb);
        mC2 = (CheckBox) findViewById( R.id.c2_cb);
        mC3 = (CheckBox) findViewById( R.id.c3_cb);
        mC4 = (CheckBox) findViewById( R.id.c4_cb);
        mC5 = (CheckBox) findViewById( R.id.c5_cb);
        mC6 = (CheckBox) findViewById( R.id.c6_cb);
        mC7 = (CheckBox) findViewById( R.id.c7_cb);
        mC8 = (CheckBox) findViewById( R.id.c8_cb);
        
        
        
        // Define Set Start Time button action
        mSetStartTimeBtn.setOnClickListener( new OnClickListener()
 		{
 			public void onClick( View view )
 			{
 				Log.d(LOGTAG, "setStartTimeBtn onClick");
 				
 				FragmentManager fm = getFragmentManager();
 				TimePickerDialogFragment newFragment = new TimePickerDialogFragment( true );
 				newFragment.show(fm, "timePicker");
 			}
 		});
        
        // Define Submit button action
        mSubmitBtn.setOnClickListener( new OnClickListener()
 		{
 			public void onClick( View view )
 			{
 				Log.d(LOGTAG, "submitBtn onClick");
 				/* Submit Start time, Days0, duration & circuits */
 				
 				// Start Time
 				/* Currently STimeTextView is programmed in terms of mm:ss.
 				 *  This needs to change but until then we compensate here
 				 *   and call GetMinutesValue and GetSecondsValue for hours
 				 *   and minutes respectively. 
 				 */
 				int starttime_hr = mStartTimeSTTV.GetMinutesValue();
 				int starttime_min = mStartTimeSTTV.GetSecondsValue();
 				//int starttime = starttime_hr*60+starttime_min;
 				
 				Log.d(LOGTAG,  "starttime_hr = "+starttime_hr );
 				Log.d(LOGTAG,  "starttime_min = "+starttime_min );
 				//Log.d(LOGTAG,  "starttime to send = "+starttime );
 				
 				// Days0
 				// THIS HAS TO CHANGE, BUT WORKS FOR NOW
 				int daysToRun = 0;
 				for ( int i=0; i<7; i++ )
 				{
 					switch (i)
 					{
 						case 0:
 							if (mMon.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 1:
 							if (mTue.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 2:
 							if (mWed.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 3:
 							if (mThu.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 4:
 							if (mFri.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 5:
 							if (mSat.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 6:
 							if (mSun.isChecked())
 								daysToRun+= Math.pow(2.0,(double)i);
 							break;
 							
 						default: 
 							break;
 					}
 				}
 				Log.d(LOGTAG,  "daysToRun to send = "+daysToRun );

 				
 				// Duration (minutes)
 				EditText durationET = (EditText) findViewById( R.id.duration_value_et);
 				int durationToSend = Integer.parseInt(durationET.getText().toString());
 				
 				// Enabled
 				int enabled = mEnabledCB.isChecked() ? 1 : 0;
 				
 				// Program ID
 				int pid = Integer.parseInt(mPidET.getText().toString());
 				
 				// Circuits 
 				// circuits NEED TO BE CHANGED TO AN ARRAY, BUT WORKS FOR NOW
 				int circuitsToRun = 0;
 				for ( int i=0; i<8; i++ )
 				{
 					switch (i)
 					{
 						case 0:
 							if (mC1.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 1:
 							if (mC2.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 2:
 							if (mC3.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 3:
 							if (mC4.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 4:
 							if (mC5.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 5:
 							if (mC6.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 6:
 							if (mC7.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						case 7:
 							if (mC8.isChecked())
 								circuitsToRun+= Math.pow(2.0,(double)i);
 							break;
 						default:
 							break;
 							
 					}
 				}
 				Log.d(LOGTAG,  "circuitsToRun to send = "+circuitsToRun );
 					
        		//SLMEB
                if ( DEBUG )
        			Log.d(LOGTAG, "Posting ChangeProgramMessage to EventBus");
                //SLM528 Have moved EventBus to OspiMessageHandler. MUST ensure to 
                // unregister when Activity is Stopped.
                // Post ChangeProgramMessage to EventBus where it will be sent
                // to Open Sprinkler Web App
                mCPMsgHandler.OspiPostChangeProgramMessage(enabled, pid,
    					daysToRun, starttime_hr, starttime_min, 
    					durationToSend, circuitsToRun);
 			}
 		});
        
        
        mStartTimeSTTV.addTimerSetListener(new TimerSetListener ()
        {
        	public void OnTimerSet( TimerSetEvent e )
        	{
        		// Get view (the STimeTextView that was changed by the 
        		// TimePickerDialogFragment.
        		View v = (View) e.getSource();
        		
        		if ( DEBUG )
        			Log.d(LOGTAG, "GOT OnTimerSet EVENT!!!  v = "+
        					v.getClass().toString());
        		
        	}
        });
	}
	
	  
	@Override
	public void onOspiResults(String result) 
	{
		// TODO Auto-generated method stub
		Toast.makeText(this, "ScheduleActivity onResult() = "+result, Toast.LENGTH_SHORT).show();
	}
	
	
    //*************************************************************************
    //  Lifecycle Methods
    //  http://developer.android.com/reference/android/app/Activity.html
    //*************************************************************************
    @Override
    public void onStart() 
    { 	
        //  Called after onCreate() OR onRestart()
        //  Called after onStop() but process has not been killed.
        super.onStart();
        Log.d(LOGTAG, "onStart");
        //SLMEB
        // Register as a subscriber
        //EventBus.getDefault().register(this);
        mCPMsgHandler = new OspiMessageHandler( this, OspiGetResultsAsyncTask.OSPI_EXEC );   
    }

    
    @Override
    public void onRestart() 
    {
        //  Called after onStop() but process has not been killed.
        if (DEBUG) Log.d (LOGTAG, "onRestart");
        super.onRestart();
    }
    
    
    @Override
    protected void onResume() 
    {
        // Called after onStart() as Activity comes to foreground.
    	// Used to initialize fields, register listeners, bind to services, etc
        if (DEBUG) Log.d (LOGTAG, "onResume");
        super.onResume();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  Called before an Activity is killed. 
    	// NOTE: this method is not called if the user presses the back button. 
    	// Don't use this approach to save data which needs to get persisted.

    	if ( DEBUG )
    		Log.d (LOGTAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onPause() 
    {
        // Called when Activity is placed in background
    	// Used to release resources or save application data. For example you 
    	// unregister listeners, intent receivers, unbind from services or 
    	// remove system service listeners.
        if (DEBUG) Log.d (LOGTAG, "onPause"); 
        super.onPause();
    }
    
    @Override
    protected void onStop() 
    {
    	// The Activity is no longer visible
    	// Time or CPU intensive shut-down operations, such as writing 
    	// information to a database should be down in the onStop() method.
    	// Guaranteed to be called as of API 11
        if (DEBUG) Log.d (LOGTAG, "onStop");
        
        //SLMEB
        Log.d(LOGTAG, "unregistering mCPMsgHandler (& this from EventBus)");
        mCPMsgHandler.Unregister();
        
        super.onStop();
    }
    
    @Override
    public void onDestroy() 
    {
    	//  The Activity is finishing or being destroyed by the system.
    	// Not guaranteed to be called.
        if (DEBUG) Log.d (LOGTAG, "onDestroy");
        super.onDestroy();
    }
    
  //********************** End Lifecycle Methods ******************************
	
/*	
	public void onCheckboxClicked(View view) 
	{
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.mon_cb:
	            if (checked)
	                // Put some meat on the sandwich
	            else
	                // Remove the meat
	            break;
	        case R.id.tue_cb:
	            if (checked)
	                // Cheese me
	            else
	                // I'm lactose intolerant
	            break;
	        // TODO: Veggie sandwich
	    }
	}
	*/	
}
