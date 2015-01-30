package com.slm_ospiui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import com.slm.ospiui.model.Circuit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
public class MainActivity extends Activity implements OspiResultsListener
{
    private static final String LOGTAG = "MainActivity";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG1 = true;
    private static final boolean DEBUG2 = true;
    
    private static final int MAX_NUM_CIRCUITS = 8;
    
    private static final String GET_ALL_CIRCUITS = "/sn0";
    
    // Set controller to manual mode => http://x.x.x.x/cv?pw=opendoor&mm=1
    
    // For now make this a static final string (updated 1/15/15)
    static final String URL = "http://192.168.1.41:8080";
    
    static final String CMD_URL = "http://192.168.1.41";
    static final String CONTROLLER_VALUES = "cv?";
    static final String PASSWD = "pw=opendoor";
    static final String SET_MANUAL_MODE = "&mm=";
    		
    
    private CustomListAdapter listAdapter;
    private Button 		mGETBtn;
    private EditText 	mCommandET;
    private TextView	mResultTV;
    
    private String		mResult;
    private String 		mCommand = "";
    
    private Circuit		maCircuits[];
    
    
    
    ProgressDialog pD;
 
    List<ListItem> list = new ArrayList<ListItem>();
   
    private Context activityContext = this;  // our execution context
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        //  Called when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Define progress dialog
        pD = new ProgressDialog(this);
        pD.setMessage(getResources().getText(R.string.str_loading_data));
        pD.setIndeterminate(true);
        pD.setCancelable(true);
        
        mCommandET = (EditText) this.findViewById( R.id.command_et);
        mResultTV = (TextView) this.findViewById(R.id.result_tv);
        
        mGETBtn = (Button)this.findViewById( R.id.get_button); 
        
        // GET FORECAST Button handler
        mGETBtn.setOnClickListener(new Button.OnClickListener() 
        {                	
            @Override
            public void onClick (final View v) 
            {	
            	Log.d(LOGTAG, "GET button clicked");
            	String command = mCommandET.getText().toString();
            	// Send command that is in mCommandET text field to the 
            	// Open Sprinkler Web App
            	SendCommand( command );
        	}
        });
        
        
        
        // ListView & CustomListAdapter
        listAdapter = new CustomListAdapter(this);
        ListView listView = (ListView) this.findViewById( R.id.list_view);
        listView.setAdapter(listAdapter);
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

                ListItem li = (ListItem)(listAdapter.getItem(position));
                
                if ( DEBUG2 )
                {
                	if ( li != null )
                		Log.d(LOGTAG, "ListItem  "+li.toString());
                	else
                		Log.d(LOGTAG, "ListItem  is NULL");
                }		        
            }
        });
        
        // Setup / Build list of Circuits for Manual screen
        BuildCircuitList();
//SLM1-12        new GetDataTask().execute ();
	}
	

	private void BuildCircuitList()
	{
		Log.d(LOGTAG, "IN BuildCircuitList()");
		
		maCircuits = new Circuit[MAX_NUM_CIRCUITS];

		// Build circuit list with default names
		BuildGeneralCircuitList();
		
		listAdapter.setList(list);
		listAdapter.notifyDataSetChanged();
		
	}
	

	/**
	 *  BuildGeneralCircuitList - Build circuit list with default names.
	 */
	private void BuildGeneralCircuitList()
	{
		int j = 0;
		// Temporarily set up circuits
		for ( int i=0; i < MAX_NUM_CIRCUITS; i++ )
		{
			
			j= i+1;
			maCircuits[i] = new Circuit();
			maCircuits[i].setCircNum(j);
			
			// Temporary - set circuit 1 as Master Circuit 
			if ( i == 0 )
			{
				Log.d(LOGTAG, "MASTER");
				
				maCircuits[i].setIsMaster(true);
				maCircuits[i].setCircName("Master Circuit");
			}
			else
			{
				maCircuits[i].setCircName("Circuit"+j);
			}
			maCircuits[i].setCircOn(false);	
		
			
			ListItem lItem = new ListItem ( "Circuit "+j,
					maCircuits[i].getCircNum(), maCircuits[i].getCircName(),  
					( maCircuits[i].isMaster() ? 1: 0), i );
			
			Log.d(LOGTAG, lItem.toString());
			
			list.add ( lItem );	
		
		}	
	}
	
	
	/**
	 *  BuildGeneralCircuitList - Build circuit list from  
	 *  circuit_description file in assets directory.
	 */
	private void BuildCircuitListFromFile()
	{
		new GetDataTask().execute ();
	}
	
	
 
	
	/**
	 * SendCommand - Send command to the Open Sprinkler Web App using an
	 * HttpAsynTask background task.
	 * 
	 * @param command - the command text to be sent to Open Sprinkler
	 */
	public void SendCommand( String command )
	{
		mCommand = command;
		
		Log.d(LOGTAG, "URL = "+URL+"  command = "+command);
    	
    	// call AsynTask to perform network operation on separate thread
		// new OspiGetResultsAsyncTask(this).execute
		//    ("http://192.168.0.41:8080"+command);
		new OspiGetResultsAsyncTask(activityContext).execute(URL+command);
	}
	
	
	public void SetCircuitStatus( String command )
	{
		
	}
	



	@Override
	public void onResults(String result) {
		 mResult = result;
         
         if ( mCommand.equals( GET_ALL_CIRCUITS ))
         {
         	Log.d(LOGTAG, "true");
         	//SetCircuitStatus( mResult );
         }
         else
         {
         	Log.d(LOGTAG, "false");
         }
         ShowResult( mResult );
	}
	public void ShowResult( String result )
	{
		mResultTV.setText(result);
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
        
        // Refresh the circuit states.
        SendCommand("/sn0");
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
  
   
//************* NOT CURRENTLY USING *******************************************
    
   /*
    * Currently reads Circuit data from file in assets directory, 
    * circuit_description 
    */
	private class GetDataTask extends AsyncTask<Void, Void, List<ListItem>>
	{
		private final String LOGTAG = GetDataTask.class.getSimpleName();
		
		//  Runs on Main thread so we can manipulate the UI.
		@Override
		protected void onPreExecute() 
		{
			//  1.  Display indeterminate progress indicator
			if ( DEBUG )
				Log.d (LOGTAG, "show dialog");
			pD.show();
		}
		
		//  Do all expensive operations here off the main thread.
		@Override
		protected List<ListItem> doInBackground (Void... params) 
		{
		    if (DEBUG ) 
		    	Log.d(LOGTAG, "**** doInBackground() SLM STARTING");
	    
	        String line = "";
	        String splitDelim = ",";
	        BufferedReader br = null;
	        
	        //  1.  Display indeterminate progress indicator
	        //  2.  Load data from a file placed in the assets directory
	        //  3.  Delay for 3 seconds.  We wan't to see the progress indicator.
	        //  4.  Load the data into the listAdapter.  Hint:  Use a handler.
	        //  5.  Cancel indeterminate progress indicator 
	            
	            try
	            {
		            InputStream is;
		            AssetManager assetManager = getAssets();
		            is = assetManager.open("circuit_description");
		            
		            // 2. Load data from a file 
		            br = new BufferedReader(new InputStreamReader(is));
		            int i = 0;
		            int listPos = 0;
		            while (( line = br.readLine()) != null) 
		            {
		            	// use comma as separator
		    			String[] tokens = line.split(splitDelim);
		    			if ( tokens[i] != null )
		    			{
		    				ListItem lItem = new ListItem ( tokens[0], tokens[1], 
		    									Integer.parseInt(tokens[2]), listPos);
		    				list.add ( lItem );	
		    				listPos++;
		    			}
		            }
		           
		            //  3.  Delay for 2 seconds.  We want to see the progress indicator.
		           // try { Thread.sleep(2000); }
		           // catch (InterruptedException e) { /* ignore */ }
		          
	            }
	            catch (FileNotFoundException e) 
	            {	e.printStackTrace();	} 
	            catch (IOException e) 
	            {	e.printStackTrace();	}
	            finally 
	            {
	        		if (br != null) 
	        		{
	        			try 
	        			{	br.close();	}
	        			catch (IOException e) 
	        			{	e.printStackTrace(); 	}
	        		}
	        	}
	            if ( DEBUG )
	            	Log.d (LOGTAG, "Done reading");     
	            
	            return list;
		}
		
		//  Runs on Main thread so we can manipulate the UI.
		@Override
		protected void onPostExecute(final List<ListItem> list) 
		{
			//  4.  Load the data into the listAdapter.  Hint:  Use a handler.
			if ( DEBUG  )
				Log.d (LOGTAG, "listAdapter.setList(list)");
    		listAdapter.setList(list);
    		listAdapter.notifyDataSetChanged();
    	
    		//  5.  Cancel indeterminate progress indicator 
    		if ( DEBUG1 )
    			Log.d (LOGTAG, "dismiss pD");
    	    pD.dismiss();
		}
	}
}
