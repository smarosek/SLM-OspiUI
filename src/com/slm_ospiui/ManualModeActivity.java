package com.slm_ospiui;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.slm.ospiui.model.Circuit;
import com.slm.ospiui.model.CircuitList;
import com.slm.ospiui.model.OspiMessageHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @author Susan L Marosek
 * @version 0.1
 * ManualModeActivity - Currently displays a crude screen layout to provide
 * 						manual operation of individual sprinkler circuits. 
 */

/*
 * Modification History:
 * 	
 * 	02/25/15 Created. 
 * 	05/28/15 Modified to use new OspiMessageHandler rather than implement 
 *				SendMessage and EventBus onEvent() in each Activity.
 *	06/11/15 Mods to incorporate new CircuitList class.
 *	06/17/15 Added code to restore circuit data from new CircuitList singleton
 *
 */
public class ManualModeActivity extends Activity implements OspiResultsListener
{
    private static final String LOGTAG = "ManualModeActivity";
    private static final String SAVEDIN = "SLMSavedInstance";
    
    // Use DEBUG to keep Log.d statements available but unused
    private static final boolean DEBUG = false; 
    // Use DEBUGLC to print Life Cycle related info
    private static final boolean DEBUGLC = true;
    // Use DEBUG1 to print ...
    private static final boolean DEBUG1 = true;
    // Use DEBUG2 to print ...
    private static final boolean DEBUG2 = true;
    
    
    private static final int MAX_NUM_CIRCUITS = 8;
    
    private static final String GET_ALL_CIRCUITS = "/sn0";
    
    // Set controller to manual mode => http://x.x.x.x/cv?pw=opendoor&mm=1
    
    // Turn on circuit => http://x.x.x.x/sn1=1 (turn on the first station)
    
    
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
    
//SLM0615    private Circuit		maCircuits[];
    
    private CircuitList sCircuitList;
    
    private OspiMessageHandler mOspiMsgHandler;
    
    ProgressDialog pD;
 
    List<ListItem> list = new ArrayList<ListItem>();
   
    private Context activityContext = this;  // our execution context
    
    
	@Override
	protected void onCreate(final Bundle savedInstanceState) 
	{
		Log.d(SAVEDIN, "In onCreate");
		
        //  Called when the activity is first created.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_mode);
        
        // Define progress dialog
        pD = new ProgressDialog(this);
        pD.setMessage(getResources().getText(R.string.str_loading_data));
        pD.setIndeterminate(true);
        pD.setCancelable(true);
        
        mCommandET = (EditText) this.findViewById( R.id.command_et);
        mResultTV = (TextView) this.findViewById(R.id.result_tv);
        
        Log.d(LOGTAG, "Creating OspiMessageHandler");
        // SLM0601 Moved from onStart BUT NOT SURE I SHOULD HAVE
        // Register as a subscriber
        mOspiMsgHandler = new OspiMessageHandler( 
        		activityContext, OspiGetResultsAsyncTask.OSPI_GET );
        
        
        mGETBtn = (Button)this.findViewById( R.id.get_button); 
        // GET FORECAST Button handler
        mGETBtn.setOnClickListener(new Button.OnClickListener() 
        {                	
            @Override
            public void onClick (final View v) 
            {	
            	Log.d(LOGTAG, "GET button clicked");
            	String command = URL+mCommandET.getText().toString();
            	// Send command that is in mCommandET text field to the 
            	// Open Sprinkler Web App
            	mOspiMsgHandler.SendSimpleGETCommand(command);
        	}
        });
        
        
        // ListView & CustomListAdapter
        listAdapter = new CustomListAdapter( this, mOspiMsgHandler );
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
                
                if ( DEBUG )
                {
                	if ( li != null )
                		Log.d(LOGTAG, "ListItem  "+li.toString());
                	else
                		Log.d(LOGTAG, "ListItem  is NULL");
                }		        
            }
        });
        
        
        Log.d(SAVEDIN,  "Building Circuit List");
        
        if( savedInstanceState != null )
        {
        	Log.d(SAVEDIN,  "HERE1  savedInstanceState != null");
        	list = (ArrayList<ListItem>)savedInstanceState.getSerializable("mmList");
        	
        	
        	

        }
        // savedInstanceState == null, 
        //		recreate sCircuitList and restore ListView items
        else
        {
        	Log.d(SAVEDIN,  "HERE2 list == null");
	        // Setup / Build list of Circuits for Manual screen
//SLM0615 Move to CircuitList	        BuildCircuitList();
//SLM1-12       BuildCircuitListFromFile();
        	
        	sCircuitList = CircuitList.getInstance();
        	
        	if ( DEBUG )
        	{
        		Log.d(SAVEDIN, "NULL #############");
        		// Prints ALL items in CircuitList
        		sCircuitList.PrintCircuitList();
        		Log.d(SAVEDIN, "NULL #############");
        	}
        	
        	int j;
        	// SLM0618 Obtain data for displayed list from Singleton CircuitList
    		for ( int i=0; i < MAX_NUM_CIRCUITS; i++ )
    		{
    			j = i + 1;
    			Circuit c = sCircuitList.get(i);
    			
    			
				ListItem lItem = new ListItem ( "C"+j,
					c.getCircNum(), c.getCircName(),  
					( c.isMaster() ? 1: 0), i );
			
				
				lItem.setDisplayDuration(c.getDuration(Circuit.CIR_MANUAL_MODE));
				lItem.setStartTime(c.getStartTime(Circuit.CIR_MANUAL_MODE));
				lItem.setStopTime(c.getStopTime(Circuit.CIR_MANUAL_MODE));
				long temp = c.getStopTime(Circuit.CIR_MANUAL_MODE) - c.getStartTime(Circuit.CIR_MANUAL_MODE);
				lItem.setLogDuration(temp);
				Log.d(LOGTAG, lItem.toString());
			
				list.add ( lItem );	
        	
        	
    		}
        }
        listAdapter.setList(list);
		listAdapter.notifyDataSetChanged();	
	}
	
//SLM0615 Move BuildCircuit List to CircuitList class

	
	public void SetCircuitStatus( String command )
	{
		
	}
	


	/* 
	 * (non-Javadoc)
	 * @see com.slm_ospiui.OspiResultsListener#onOspiResults(java.lang.String)
	 */
	@Override
	public void onOspiResults(String result) 
	{
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
        if (DEBUGLC) Log.d (SAVEDIN, "onStart");
        //SLMEB
        /*SLM0601  Moving to onCreate BUT NOT SURE I SHOULD
        Log.d(LOGTAG, "Creating mOspiMsg");
        mOspiMsg = new OspiMessageHandler( this, OspiGetResultsAsyncTask.OSPI_GET );
        */
        
        // Get the circuit states.
        mOspiMsgHandler.SendSimpleGETCommand(URL+"/sn0");
    }

    @Override
    public void onRestart() 
    {
        //  Called after onStop() but process has not been killed.
        if (DEBUGLC) Log.d (SAVEDIN, "onRestart");
        super.onRestart();
    }
    
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() 
    {
        //  Called after onStart() as Activity comes to foreground.
        if (DEBUGLC) Log.d (SAVEDIN, "onResume");
        super.onResume();
    }
    
    
    /* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     * 
     * The system calls this method when the user is leaving your activity and 
     * passes it the Bundle object that will be saved in the event that your 
     * activity is destroyed unexpectedly.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  Called before an Activity is killed.
    	if ( DEBUGLC )
    	{
    		Log.d (SAVEDIN, "************************************************()");
    		Log.d (SAVEDIN, "In onSaveInstanceState()");
    		Log.d (SAVEDIN,  "outState = "+outState.toString());
    	}
 //SLM0606   	
    	outState.putSerializable( "mmList", (Serializable) list );
    	
    	Log.d(SAVEDIN,  "list = "+ list.toString());
    	
    	// Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
        Log.d (SAVEDIN, "************************************************");
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    public void onRestoreInstanceState( Bundle outState )
    {
    	Log.d (SAVEDIN, "************************************************");
    	Log.d (SAVEDIN, "In onRestoreInstanceState()");
    	
    	// Always call the superclass so it can save the view hierarchy state
        super.onRestoreInstanceState(outState);
      
    	Log.d(SAVEDIN,  "HERE1  outState != null");
    	list = (ArrayList<ListItem>)outState.getSerializable("mmList");
    	
        Log.d (SAVEDIN, "************************************************");
    	
    }
    
    @Override
    public void onPause() 
    {
        //  Called when Activity is placed in background
        if (DEBUGLC) Log.d (SAVEDIN, "onPause"); 
        super.onPause();
        
        Log.d (SAVEDIN, "Saving sCircuitList"); 
        int i = 0;
        for ( ListItem li:list )
        {
        	sCircuitList.get(i).storeManualModeInfo(li.getStartTime(), li.getStopTime(), li.getDisplayDuration());
        	i++;
        	
        }
    }
    
    @Override
    protected void onStop() 
    {
    	//  The Activity is no longer visible
        if (DEBUGLC) Log.d (SAVEDIN, "onStop");
        
        //SLMEB
        Log.d(LOGTAG, "unregistering mOspiMsgHandler (& this from EventBus)");
        mOspiMsgHandler.Unregister();
        
        super.onStop();
    }
    
    @Override
    public void onDestroy() 
    {
    	//  The Activity is finishing or being destroyed by the system
        if (DEBUGLC) Log.d (SAVEDIN, "onDestroy");
        super.onDestroy();
    }
  
   
//************* NOT CURRENTLY USING *******************************************
    
   /*
    * Currently reads Circuit data from file in assets directory, 
    * circuit_description 
    */
/*    
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
		           // catch (InterruptedException e) { / ignore  }
		          
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
	
	*/
	
}
