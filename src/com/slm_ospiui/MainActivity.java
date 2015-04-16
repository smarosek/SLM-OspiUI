package com.slm_ospiui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.slm.ospiui.model.Circuit;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
    private static final boolean DEBUG1 = true;
    private static final boolean DEBUG2 = true;
    
    private static final int MAX_NUM_CIRCUITS = 8;
    
    private static final String GET_ALL_CIRCUITS = "/sn0";
    
    // Set controller to manual mode => http://x.x.x.x/cv?pw=opendoor&mm=1
    
    // Turn on circuit => http://x.x.x.x/sn1=1 (turn on the first station)
    
/*    
    // For now make this a static final string (updated 1/15/15)
    static final String URL = "http://192.168.1.41:8080";
    
    static final String CMD_URL = "http://192.168.1.41";
    static final String CONTROLLER_VALUES = "cv?";
    static final String PASSWD = "pw=opendoor";
    static final String SET_MANUAL_MODE = "&mm=";
    		
*/    

    private Button 		mManualModeBtn;
    private Button 		mScheduleBtn;
    
 
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
