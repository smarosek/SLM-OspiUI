package com.slm_ospiui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @author Susan L Marosek
 * @version 0.1
 * @MainActivity - Currently displays the Manual Mode screen for the SLM_OspiUI 
 *      Open Sprinkler Pi User Interface. 
 */

/*
 * Modification History:
 * 	
 * 	01/27/15 Added interface, OspiResultsListener to enable capability to 
 * 				handle the SendCommand events in the appropriate Activity (I hope:) 
 *	01/15/15 Updated URL from "http://192.168.0.41:8080" to 
 *    			"http://192.168.1.41:8080" for change from Verizon to TWC.
 *
 */
public class MainActivity extends Activity
{
    private static final String LOGTAG = "MainActivity";
    private static final boolean DEBUG = true;
   // private static final boolean DEBUG1 = true;
   // private static final boolean DEBUG2 = true;

    private Button 		mManualModeBtn;
    private Button 		mScheduleBtn;
    private Button 		mSummaryBtn;
    
 
    private Context activityContext = this;  // our execution context
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        //  Called when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
              
        mManualModeBtn = (Button)this.findViewById( R.id.manual_mode_button); 
        
        // Show ManualMode Activity Button handler
        mManualModeBtn.setOnClickListener(new Button.OnClickListener() 
        {                	
            @Override
            public void onClick (final View v) 
            {	
            	Log.d(LOGTAG, "ManualMode button clicked");
            	// This is my IMPLICIT intent with custom action
    	    	Intent myIntent = new Intent();
    	        // This is an EXPLICIT intent -- i.e., we specify the target Activity by class name.
    	        myIntent.setClass (activityContext, ManualModeActivity.class);  
    	    	startActivity(myIntent);
        	}
        });
        
        
        mScheduleBtn = (Button)this.findViewById( R.id.schedule_button); 
        
        // Show mScheduleBtn Activity Button handler
        mScheduleBtn.setOnClickListener(new Button.OnClickListener() 
        {                	
            @Override
            public void onClick (final View v) 
            {	
            	Log.d(LOGTAG, "mScheduleBtn button clicked");
            	// This is my IMPLICIT intent with custom action
    	    	Intent myIntent = new Intent();
    	        // This is an EXPLICIT intent -- i.e., we specify the target Activity by class name.
    	        myIntent.setClass (activityContext, ScheduleActivity.class);  
    	         
    	    	startActivity(myIntent);
        	}
        });
        
        
        mSummaryBtn = (Button)this.findViewById( R.id.summary_button); 
        
        // Show mScheduleBtn Activity Button handler
        mSummaryBtn.setOnClickListener(new Button.OnClickListener() 
        {                	
            @Override
            public void onClick (final View v) 
            {	
            	Log.d(LOGTAG, "mSummaryBtn button clicked");
            	// This is my IMPLICIT intent with custom action
    	    	Intent myIntent = new Intent();
    	        // This is an EXPLICIT intent -- i.e., we specify the target Activity by class name.  
    	        myIntent.setClass (activityContext, ProgramSummaryActivity.class);  
    	    	startActivity(myIntent);
        	}
        });
	}
  
    
    //**********************************************************************************************
    //  Lifecycle Methods
    //  http://developer.android.com/reference/android/app/Activity.html
    //**********************************************************************************************
    @Override
    public void onStart() 
    { 	
        //  Called after onCreate() OR onRestart()
        //  Called after onStop() but process has not been killed.
        super.onStart();
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
        //  Called after onStart() as Activity comes to foreground.
        if (DEBUG) Log.d (LOGTAG, "onResume");
        super.onResume();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  Called before an Activity is killed.
    	if ( DEBUG )
    		Log.d (LOGTAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onPause() 
    {
        //  Called when Activity is placed in background
        if (DEBUG) Log.d (LOGTAG, "onPause"); 
        super.onPause();
    }
    
    @Override
    protected void onStop() 
    {
    	//  The Activity is no longer visible
        if (DEBUG) Log.d (LOGTAG, "onStop");

        super.onStop();
    }
    
    @Override
    public void onDestroy() 
    {
    	//  The Activity is finishing or being destroyed by the system
        if (DEBUG) Log.d (LOGTAG, "onDestroy");
        super.onDestroy();
    }
}
