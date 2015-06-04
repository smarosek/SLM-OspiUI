package com.slm_ospiui;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ProgramSummaryActivity extends Activity
{
	private static final String LOGTAG = "ProgramSummaryActivity";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG1 = true;
    private static final boolean DEBUG2 = true;
    
    private ProgSumListAdapter mPSListAdapter;
    
    
    List<ListItem> list = new ArrayList<ListItem>();
    
    private Context activityContext = this;  // our execution context
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        //  Called when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_summary);
        
	
	
	
		// ListView & CustomListAdapter
		mPSListAdapter = new ProgSumListAdapter(this);
	    ListView listView = (ListView) this.findViewById( R.id.ps_list_view);
	    listView.setAdapter(mPSListAdapter);
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() 
	    {
	        @Override
	        public void onItemClick (AdapterView<?> arg0, View view, int position, long id) 
	        {
	            if (DEBUG) 
	            {
	                Log.d (LOGTAG, "onItemClick(): arg0="+arg0.getClass().getSimpleName());
	                Log.d (LOGTAG, "onItemClick(): view="+view.getClass().getSimpleName());
	                Log.d (LOGTAG, "onItemClick(): position="+position);
	            }
	            
	            //  Start new Activity.
                Intent myIntent = new Intent();
                
	
	            ListItem li = (ListItem)(mPSListAdapter.getItem(position));
	            
	            if ( DEBUG2 )
	            {
	            	if ( li != null )
	            		Log.d(LOGTAG, "ListItem  "+li.toString());
	            	else
	            		Log.d(LOGTAG, "ListItem  is NULL");
	            }	
	            
	            // This is an EXPLICIT intent -- i.e., we specify the target Activity by class name.
		        myIntent.setClass (activityContext, ScheduleActivity.class);  
		       // myIntent.putExtra( "cirNum", li.getCirNum());
		       // myIntent.putExtra( "cirName", li.getCirName());
		        //  Start new Activity.
		        startActivity(myIntent);
	        }
	    });
	}
	
	public void onEvent( ChangeProgramMessage event )
	{
		
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
        

        EventBus.getDefault().register(this);
        
        // Refresh the circuit states.
        //SLM  SendCommand("/pg0");
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
        
        //SLMEB
        EventBus.getDefault().unregister(this);
        
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
