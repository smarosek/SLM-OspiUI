package com.slm_ospiui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity 
{
    private static final String LOGTAG = "MainActivity";
    private static final boolean DEBUG = true;
    private static final boolean DEBUG2 = true;
    
    private CustomListAdapter listAdapter;
    private Button 		mGETBtn;
    private EditText 	mCommandET;
    private TextView	mResultTV;
    
    ProgressDialog pD;
 
    List<ListItem> list = new ArrayList<ListItem>();
//    private Context activityContext = this;  // our execution context
    
    
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
            	String URL = "http://192.168.0.41:8080";
            	
            	Log.d(LOGTAG, "URL = "+URL+"  command = "+command);
            	            	
            	// call AsynTask to perform network operation on separate thread
        		new HttpAsyncTask().execute("http://192.168.0.41:8080"+command);
            	
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
        
        
 
        new GetDataTask().execute ();
	}
	
	
	
	public static String GET(String url)
	{
	    String result = "";
	    
	    Log.d("In GET   ", url);
	    
	    try 
	    {
	        Document doc  = Jsoup.connect(url).get();

	        Log.d("GET", "doc = "+doc.toString());
	        
	        result = doc.body().text();
	        
	        Log.d("GET", "result = "+ result );
	 
	    } 
	    catch (NullPointerException e1) 
	    {
	    //    Log.d("InputStream", e.getLocalizedMessage());
	    	Log.d("InputStream ", "NullPointerException" );
	    }
	    catch (Exception e) 
	    {
	    //    Log.d("InputStream", e.getLocalizedMessage());
	    	Log.d("InputStream ", e.toString() );
	    }
 
     	return result;
    }
    
/*    
     private static String convertInputStreamToString(InputStream inputStream) throws IOException
     {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        
        Log.d("convertInputStream  ", result);
        
        return result;
 
    }
*/
	
    private class HttpAsyncTask extends AsyncTask<String, Void, String> 
    {
    	
    
        @Override
        protected String doInBackground(String... urls) 
        {
        	Log.d("In doInBackground  ",  urls[0]);
        	
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            mResultTV.setText(result);
       }
    }
	 
	
	
	
	
    
    static class ViewHolder 
    {
    	ListItem 	listItem;
        int 		position;
        TextView  	cirNumTV;
        TextView  	cirNameTV;
        Chronometer cirChrono;
        ToggleButton cirOnOffTButton;
    }
    
    
    /**
     * Custom list class for sprinkler circuit description for handling  
     * manual mode spriner operation.
     * @author Susan Marosek
     */
    class CustomListAdapter extends BaseAdapter
    {
        private Context          context;
        private List<ListItem>   list;
        private LayoutInflater   layoutInflater;
        
        /**
         * CustomListAdapter Class Constructor
         * @param context
         */
        CustomListAdapter(Context context) 
        {
            this.context = context;
        }
        
        public void setList (List<ListItem> list) 
        {
            this.list = list;
        }
        
        @Override
        public int getCount() 
        {
            return ((list == null) ? 0 : list.size());
        }

        @Override
        public Object getItem (int position) 
        {
            //  In theory should not be called if getCount() returned 0;
            return list.get(position);
        }

        @Override
        public long getItemId (int position) 
        {
            return position;
        }
            
        @Override
        public View getView (int position, View convertView, ViewGroup parent) 
        {    
        	if (DEBUG2) 
            {
        		Log.d(LOGTAG, "\n\nEntered getView() Position = "+position);
        		Log.d(LOGTAG, "getView.convertView="+convertView);
            }
            
            if (list == null) {
                //  In theory it should not happen but handle this in some graceful way.
                //  Returning null will not produce graceful results.
                return null;
            }
            // You can find this ViewHolder idiom described in detail in this talk:
            //      http://www.youtube.com/watch?v=N6YdwzAvwOA&feature=related
            
            /*
             * When you are not using Holder so getView() method will call 
             * findViewById() as many times as you row(s) will be out of View. 
             * So if you have 1000 rows in List and 990 rows will be out of 
             * View then 990 times will be called findViewById() again.
             * Holder design pattern is used for View caching - Holder 
             * (arbitrary) object holds child widgets of each row and when row 
             * is out of View then findViewById() won't be called but View will 
             * be recycled and widgets will be obtained from Holder.
             */
            ViewHolder holder = null;
            
            if (convertView == null) 
            {
                convertView = (LinearLayout) getLayoutInflator().inflate(R.layout.list_item, null);
                
                holder = new ViewHolder();
                
                // Need listItem in the view holder because I stored the 
                // item's position in the list, in the list so I can 
                // get that position to set toggle button & chronometer info
                holder.listItem = list.get(position);
                holder.position = holder.listItem.getPosition();
                
                holder.cirNumTV = (TextView) convertView.findViewById(R.id.circuit_num_tv);
                holder.cirNameTV = (TextView) convertView.findViewById(R.id.circuit_name_tv);
                
                holder.cirOnOffTButton  = (ToggleButton) convertView.findViewById(R.id.cir_toggle_button);
                holder.cirOnOffTButton.setTag(position);
                
                holder.cirChrono  = (Chronometer) convertView.findViewById(R.id.chronometer);
                
                convertView.setTag(holder); 

                // Need final variable to be able to access inside onClick(), 
                final ViewHolder newHolder = holder;
                
                // Handle ToggleButton clicks
                holder.cirOnOffTButton.setOnClickListener(new ToggleButton.OnClickListener() 
                {                	
                    @Override
                    public void onClick (final View v) 
                    {
                    	// Get position that we saved as tag for the ToggleButton
                    	int pos = (Integer)v.getTag();
                    	
                    	ToggleButton btn = (ToggleButton) v;
                    	long startTime = 0;
                    	long stopTime = 0;

                    	if ( DEBUG2 )
                    	{	Log.d(LOGTAG, "*");
                    		Log.d(LOGTAG, "*");
                    	}
                    	
                    	// If button is checked, start chronometer
                    	if (btn.isChecked() )
                    	{
                    		if ( DEBUG2 )
                    		{
                    			Log.d(LOGTAG, "  newHolder.position = "+
                    				newHolder.position);
                    			Log.d(LOGTAG, "   ToggleButton at position "+
                    				pos+" is checked. starttime is: "+                   				
                    				SystemClock.elapsedRealtime());
                    		}
                    		newHolder.cirChrono.setBase(SystemClock.elapsedRealtime());
                    		startTime = SystemClock.elapsedRealtime();
            				newHolder.cirChrono.start();	
                    	}
                    	else
                    	{	// Button is unchecked, stop chronometer and save time to DB
                    		if ( DEBUG2 )
                    		{
                    			Log.d(LOGTAG, "  newHolder.position = "+
                    				newHolder.position);
                    			Log.d(LOGTAG, "   ToggleButton at position "+
                    				pos+" is NOT checked. stoptime is: "+
                    				SystemClock.elapsedRealtime());
                    		}
                    		newHolder.cirChrono.stop();
                    		stopTime = SystemClock.elapsedRealtime();
                    		
                    	}
                    	

                    	// Take care of updating toggle button state & 
                    	// chronometer-related fields when toggle button 
                    	// state changes      	
                    	int pos1 = newHolder.position;
                    	if ( DEBUG2 )
                    		Log.d(LOGTAG, " pos1 = "+pos1 );
                    	
                    	ListItem li;
                    	li = list.get(pos1);
                    	int b = (btn.isChecked() ? 1 : 0 );
                    	li.setToggleBtnState(b);
                    	
                    	if ( btn.isChecked() )
                    	{
                    		// Save startTime to list, chronometer is running
                    		li.setStartTime(startTime);
                    	}
                    	else
                    	{
                    		// Chronometer is not running, save stopTime 
                    		// Actually it's a duration
                    		li.setStopTime( stopTime - li.getStartTime());
                    	}
                    	// Update List
                    	list.set( pos1,  li );
                    	if ( DEBUG2 )
                    	{
                    		Log.d(LOGTAG, "1 list row "+pos1+" updated: "+
                    					li.toStringAll()); 
                    		Log.d(LOGTAG, "*");
                    		Log.d(LOGTAG, "*");
                    	}
                    }
                });         
            }
            else 
            {
            	// At this point, holder is pointing to the view that got
            	// scrolled off. Need to put new row info into it
            	holder = (ViewHolder) convertView.getTag();
            	if ( DEBUG2 )
            		Log.d(LOGTAG, "!!! else holder.position = "+holder.position);
            }
            
            // Handle setting list row info when row is visible 
            // (includes when a row becomes visible due to scrolling)           
            ListItem item = list.get(position);
            holder.cirNumTV.setText(item.cirNum);
            holder.cirNameTV.setText(item.cirName);
            holder.position = item.getPosition();
            
            holder.cirOnOffTButton.setChecked((item.toggleBtnState == 1));
            
            if ( DEBUG2 )
            {
            	Log.d(LOGTAG, "holder.position = "+holder.position);
            	Log.d(LOGTAG, "toggle button at "+position+" = "+ 
            				(item.toggleBtnState == 1));
            }
            // If toggle button is on
            if  (item.toggleBtnState == 1)
            {
            	if ( DEBUG2 )
            		Log.d( LOGTAG, "Handling togglebutton == true, startTime = "+
            				item.getStartTime());

            	// Need to set chronometer's startTime & call start           	            	
            	holder.cirChrono.setBase(item.getStartTime());
            	holder.cirChrono.start();
            }
            else	// Toggle button is off
            {	
            	if (DEBUG2 )
            	{
            		Log.d( LOGTAG, 
            			"Handling togglebutton == false, try setting to starttime");
            		Log.d(LOGTAG, "ListItem["+position+"] = "+item.toStringAll());
            	}
            	// togglebutton is off. chronometer must show the duration
            	// of it's last run (if it ran).
            	// This will be 0 if the chronometer has never been turned on.
            	
            	// To get the value of the last duration, need to 
            	// subtract the stopTime (stored in the list item) from 
            	// the SystemClock.elapsedRealtime() (or current time)
            	long tmpTime = SystemClock.elapsedRealtime()-item.getStopTime();
            	if ( item.getStartTime() != 0 )	
            	{	// Chronometer is stopped (at some non-zero time)
            		holder.cirChrono.stop();
            		holder.cirChrono.setBase( tmpTime );
            	}
            	else	
            	{	// Chronometer is in initial state, has never run & stopped
            		if ( DEBUG2 )
            			Log.d( LOGTAG, "startTime  = 0, stop Chrono and reset" );
            		holder.cirChrono.stop();
            		holder.cirChrono.setBase( SystemClock.elapsedRealtime() );
            	}
            }
            if ( DEBUG2 )
            {	Log.d(LOGTAG, "End getView()");
            	Log.d(LOGTAG, "*");
            	Log.d(LOGTAG, "*");
            }
            return convertView;
        }
        
        private LayoutInflater getLayoutInflator() 
        {
            if (layoutInflater == null) {
                layoutInflater = (LayoutInflater)
                    this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }      
            return layoutInflater;
        }
    }  // end class CustomListAdapter
    	
	
    
    
    
	private class GetDataTask
	extends AsyncTask<Void, Void, List<ListItem>>
	{
		private final String LOGTAG = GetDataTask.class.getSimpleName();
		private final boolean DEBUG1 = true;
		
		//  Runs on Main thread so we can manipulate the UI.
		@Override
		protected void onPreExecute() 
		{
			//  1.  Display indeterminate progress indicator
			Log.d (LOGTAG, "show dialog");
			pD.show();
		}
		
		//  Do all expensive operations here off the main thread.
		@Override
		protected List<ListItem> doInBackground (Void... params) 
		{
		    if (DEBUG1) Log.d(LOGTAG, "**** doInBackground() SLM STARTING");
	    
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
		           
		            //  3.  Delay for 2 seconds.  We wan't to see the progress indicator.
		            try { Thread.sleep(2000); }
		            catch (InterruptedException e) { /* ignore */ }
		          
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
	            if ( DEBUG1 )
	            	Log.d (LOGTAG, "Done reading");     
	            
	            return list;
		}
		
		//  Runs on Main thread so we can manipulate the UI.
		@Override
		protected void onPostExecute(final List<ListItem> list) 
		{
			//  4.  Load the data into the listAdapter.  Hint:  Use a handler.
			if ( DEBUG1 )
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
