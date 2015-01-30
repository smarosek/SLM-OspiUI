package com.slm_ospiui;

import java.util.List;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;



    
/**
 * Custom list class for sprinkler circuit description for handling  
 * manual mode spriner operation.
 * @author Susan Marosek
 */
public class CustomListAdapter extends BaseAdapter
{
    private static final String LOGTAG = "CustomListAdapter";
    private static final boolean DEBUG = true;
//    private static final boolean DEBUG1 = true;
//    private static final boolean DEBUG2 = true;
	
    private Context          mParentContext;
    private List<ListItem>   list;
    private LayoutInflater   layoutInflater;
    
    /**
     * CustomListAdapter Class Constructor
     * @param mParentContext
     */
    CustomListAdapter(Context context) 
    {
        this.mParentContext = context;
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
 
    /*
     * The getItemViewType returns the view type of the current row, if you look at the method signature we have the position as parameter. (non-Javadoc)
     * @see android.widget.BaseAdapter#getItemViewType(int)
     */
    @Override
    public int getItemViewType( int position )
    {
    	return 1;
    	
    }
    
    
    /*
     * The method getViewTypeCount "returns the number of types of Views that will be created by getView(int, View, ViewGroup)".
In other words this method returns how many different layouts we have in our ListView. 
(non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public int getViewTypeCount() 
    {
    	return 1;
    }
    
    
    @Override
    public View getView (int position, View convertView, ViewGroup parent) 
    {    
    	if (DEBUG) 
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

                	if ( DEBUG )
                	{	Log.d(LOGTAG, "*");
                		Log.d(LOGTAG, "*");
                	}
                	
                	// If button is checked, start chronometer
                	if (btn.isChecked() )
                	{
                		if ( DEBUG )
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
        				
        			//	if ( newHolder.position == 0 )
        			//	{
        			//		Log.d( LOGTAG, "Clicked MASTER CIRCUIT button");
        					
        			//	}
        				
                	}
                	else
                	{	// Button is unchecked, stop chronometer and save time to DB
                		if ( DEBUG )
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
                	if ( DEBUG )
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
                	if ( DEBUG )
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
        	if ( DEBUG )
        		Log.d(LOGTAG, "!!! else holder.position = "+holder.position);
        }
        
        // Handle setting list row info when row is visible 
        // (includes when a row becomes visible due to scrolling)           
        ListItem item = list.get(position);
        holder.cirNumTV.setText(item.cirNum);
        holder.cirNameTV.setText(item.cirName);
        holder.position = item.getPosition();
        
        holder.cirOnOffTButton.setChecked((item.toggleBtnState == 1));
        
        if ( DEBUG )
        {
        	Log.d(LOGTAG, "holder.position = "+holder.position);
        	Log.d(LOGTAG, "toggle button at "+position+" = "+ 
        				(item.toggleBtnState == 1));
        }
        // If toggle button is on
        if  (item.toggleBtnState == 1)
        {
        	if ( DEBUG )
        		Log.d( LOGTAG, "Handling togglebutton == true, startTime = "+
        				item.getStartTime());

        	// Need to set chronometer's startTime & call start           	            	
        	holder.cirChrono.setBase(item.getStartTime());
        	holder.cirChrono.start();
        }
        else	// Toggle button is off
        {	
        	if (DEBUG )
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
        		if ( DEBUG )
        			Log.d( LOGTAG, "startTime  = 0, stop Chrono and reset" );
        		holder.cirChrono.stop();
        		holder.cirChrono.setBase( SystemClock.elapsedRealtime() );
        	}
        }
        if ( DEBUG )
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
                this.mParentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }      
        return layoutInflater;
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

} 



