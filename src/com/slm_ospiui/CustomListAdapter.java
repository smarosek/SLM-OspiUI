package com.slm_ospiui;

import java.util.List;

import com.slm.ospiui.model.OspiMessageHandler;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

    
/**
 * 
 * @author Susan Marosek
 * CustomListAdapter - Custom list class for sprinkler circuit description for   
 * handling manual mode sprinkler operation.
 */

/*
 * Modification History:
 * 	
 * 	02/25/15 Begin
 * 	03/11/15 Remove excess info from ViewHolder (listItem, duration). Also
 * 			
 */
public class CustomListAdapter extends BaseAdapter
{
    private static final String LOGTAG = "CustomListAdapter";
    private static final String LOGTAG_POS = "HolderPos";
    
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_EVENT_BUS = false;
    
    
//    private static final boolean DEBUG1 = true;
//    private static final boolean DEBUG2 = true;
	
    
    private OspiMessageHandler	mOspiMsgHandler;
    private Context         	mParentContext;
    private List<ListItem>  	list;
    private LayoutInflater  	layoutInflater;
    
    /**
     * CustomListAdapter Class Constructor
     * @param mParentContext
     */
    CustomListAdapter(Context context, OspiMessageHandler msgHand ) 
    {
        this.mParentContext = context;
        this.mOspiMsgHandler = msgHand;
        
        if (msgHand == null )
        	Log.d(LOGTAG, "CLA mOspiMsgHandler is NULL");	
        else
        	Log.d(LOGTAG, "CLA mOspiMsgHandler is NOT NULL");	
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
    
    
    /* 
     * getView		
     * Desc: 	Get a View that displays the data at the specified position 
     * 			in the data set.
     * Parameters:
     * 		position 	- position of item within the adapter's data set 
     * 					(the list) of item whose view we want.
     * 		convertView - The old view to reuse, if possible. 
     * 		parent 		- parent that this view will eventually be attached to.
     * 
     * Returns: A View corresponding to data at the specified position.
     */
    @Override
    public View getView (int position, View convertView, ViewGroup parent) 
    {    
    	if (DEBUG) 
        {
    		Log.d(LOGTAG_POS, "\n\nEntered getView() position = "+position);
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
            convertView = (LinearLayout) getLayoutInflator().inflate(R.layout.list_item, parent, false);
            
            holder = new ViewHolder();

            // Need listItem in the view holder because I stored the 
            // item's position in the list, in the list so I can 
            // get that position to set toggle button & chronometer info
//LSM            holder.listItem = list.get(position);
            //SLM tmp
            ListItem li1 = list.get(position);
            //This position is important
            holder.position = li1.getPosition();
            
            Log.d(LOGTAG_POS, "holder.position = li1.getPosition() = "+holder.position);
            
         
            holder.cirNumTV = (TextView) convertView.findViewById(R.id.circuit_num_tv);
            holder.cirNameTV = (TextView) convertView.findViewById(R.id.circuit_name_tv);
            
            holder.cirOnOffTButton  = (ToggleButton) convertView.findViewById(R.id.cir_toggle_button);
            //WAS0309 holder.cirOnOffTButton.setTag(position);
            holder.cirOnOffTButton.setTag(holder);
            
            holder.cirSetTimerButton  = (Button) convertView.findViewById(R.id.cir_settimer_button);
            //WAS0311 holder.cirSetTimerButton.setTag(position);
            holder.cirSetTimerButton.setTag(holder);
            
           //SLMTimer  holder.cirChrono  = (Chronometer) convertView.findViewById(R.id.chronometer);
            holder.cirChronoSTV  = (STimerView) convertView.findViewById(R.id.chronometer_stv);
            holder.cirChronoSTV.setTag(holder);
            holder.cirChronoSTV.addTimerStopListener(new TimerStopListener ()
            {
            	public void OnTimeout( TimerStopEvent e )
            	{
            		View v = (View) e.getSource();
            		if ( DEBUG )
            			Log.d(LOGTAG, "GOT OnTimeout EVENT!!!  v = "+
            					v.getClass().toString());
            		
            		// Our ViewHolder contains the correct position in list.
            		// We'll use this to set correct ???
            		ViewHolder mH = (ViewHolder)v.getTag();
            		
            		// Get list item at position and set the ???
            		//ListItem li = list.get(mH.position);
            		mH.cirOnOffTButton.setChecked(false);
            		
            		// Need to send message / Event to turn off the circuit
            		// Posts message/event so ManualModeActivity's onEvent() is 
            		// called which in turn calls SendMessage() to OpenSprinkler Web App
            		// by starting OspiGetResultsAsyncTask.
            		// TMP SLM 0305 - yes correct position
    				//SLMEB
            		if ( DEBUG_EVENT_BUS )
            			Log.d(LOGTAG, "Posting OFF to EventBus");
            		
            		
    				//SLM 0529 EventBus.getDefault().post(new CircuitOnOffMessage(
            		mOspiMsgHandler.OspiPostCircuitOnOffMessage(
    						CircuitOnOffMessage.CIRCUIT_OFF, 
    						mH.position+1);
    						
            		
            		
            		list.get(mH.position).setToggleBtnState(0);
            		list.get(mH.position).setStopTime(System.currentTimeMillis());
            		if ( DEBUG )
            			Log.d(LOGTAG_POS, "Event!!!  list["+
            					mH.position+"] = "+ list.get(mH.position).toStringAll());
            	}
            });
            
            
            holder.cirDurationSTTV  = (STimeTextView) convertView.findViewById(R.id.duration_stv);
            holder.cirDurationSTTV.setTag(holder);
            //SLM4
            holder.cirDurationSTTV.addTimerSetListener(new TimerSetListener ()
            {
            	public void OnTimerSet( TimerSetEvent e )
            	{
            		// Get view (the STimeTextView that was changed by the 
            		// TimePickerDialogFragment so that we can set the 
            		// appropriate duration value in the list to be retrieved
            		// when listview is scrolled.
            		View v = (View) e.getSource();
            		
            		if ( DEBUG )
            			Log.d(LOGTAG, "GOT OnTimerSet EVENT!!!  v = "+
            					v.getClass().toString());
            		
            		// Our ViewHolder contains the correct position in list.
            		// We'll use this to set correct displahyDuration item
            		ViewHolder mH = (ViewHolder)v.getTag();
            		
            		// Get list item at position and set the displayDuration
            		ListItem li = list.get(mH.position);
            		li.setDisplayDuration(((STimeTextView) v).GetTimeMs());
            	
            		if ( DEBUG )
            			Log.d(LOGTAG_POS, "Event!!!  ListItem["+
            					mH.position+"] = "+ li.toStringAll());
            	}
            });
            
            
            
            Log.d(LOGTAG_POS, "1 $$$  ListItem["+position+"] = "+li1.toStringAll());

            convertView.setTag(holder); 

            /////////////////////////////////////////////////////////////
            // Handle ToggleButton clicks
            holder.cirOnOffTButton.setOnClickListener(new ToggleButton.OnClickListener() 
            {                	
                @Override
                public void onClick (final View v) 
                {
                	// Get view holder that we saved as tag for the ToggleButton
                	ViewHolder mH = (ViewHolder)v.getTag();
                	
                	ToggleButton btn = (ToggleButton) v;
                	long startTime = 0;
                	long stopTime = 0;

                	if ( DEBUG )
                	{
                		Log.d(LOGTAG_POS, "mH.position = "+ mH.position);
                		Log.d(LOGTAG_POS, "*");
                		Log.d(LOGTAG_POS, "*");
                	}
                	
                	// If button is checked, start chronometer
                	if (btn.isChecked() )
                	{
                		// This startTime is used to log the circuit watering data
                		startTime = System.currentTimeMillis();
                		
                		if ( DEBUG )
                		{
                			Log.d(LOGTAG_POS, "   ToggleButton at position "+
                					mH.position+" is checked. starttime is: "+                   				
                					startTime );
                		}
                		                		
                		// Zero out the circuit start Time and start runnable
                		mH.cirChronoSTV.SetStartTime(0);
                		mH.cirChronoSTV.PostTimerDelayed(0);
                        
                		// Posts message/event so ManualModeActivity's onEvent() is 
                		// called which in turn calls SendMessage() to OpenSprinkler Web App
                		// by starting OspiGetResultsAsyncTask.
                		// TMP SLM 0305 - yes correct position
        				//SLMEB
                		if ( DEBUG_EVENT_BUS )
                			Log.d(LOGTAG, "Posting ON to EventBus");
                		
                		
        				//SLM 0529 EventBus.getDefault().post(new CircuitOnOffMessage(
                		mOspiMsgHandler.OspiPostCircuitOnOffMessage(
        						CircuitOnOffMessage.CIRCUIT_ON, 
        						mH.position+1);
        					
                	}
                	else
                	{	// Button is unchecked, stop chronometer and save time to DB
                		
                		// stopTime is used to log the circuit watering data
                		stopTime = System.currentTimeMillis();
                		
                		if ( DEBUG )
                		{
                			Log.d(LOGTAG_POS, "   ToggleButton at mH.position "+
                					mH.position+" is NOT checked. stoptime is: "+
                					stopTime );
                		}
                		
                		// Button is unchecked, stop timer (and save time to DB??)
                		mH.cirChronoSTV.RemoveTimerCallbacks();
                		
                		
                	
                		// Posts message/event so ManualModeActivity's onEvent() is 
                		// called which in turn calls SendMessage() to OpenSprinkler Web App
                		// by starting OspiGetResultsAsyncTask.
                		//SLMEB
                		if ( DEBUG_EVENT_BUS )
                			Log.d(LOGTAG, "Posting OFF to EventBus");
                		
                		
                		//SLM0529 EventBus.getDefault().post(new CircuitOnOffMessage(
                		mOspiMsgHandler.OspiPostCircuitOnOffMessage(
                				CircuitOnOffMessage.CIRCUIT_OFF, 
                				mH.position+1);
                	}
                	
                	// Update toggle button state & 
                	// chronometer-related fields when toggle button 
                	// state changes. Position we're sending is position in the List  	
                	int pos1 = mH.position;
                	UpdateListItemElementsState( btn.isChecked(), pos1, startTime, stopTime );
                	            	
                }	// end onClick()
            });  	// end holder.cirOnOffTButton.setOnClickListener(new ToggleButton.OnClickListener()                
            
         // Handle SetTimer Button click
            holder.cirSetTimerButton.setOnClickListener(new Button.OnClickListener() 
            {                	
                @Override
                public void onClick (final View v) 
                {
                	// Get viewHolder that we saved as tag for the Button
                	ViewHolder mH = (ViewHolder)v.getTag();
	            
            		if ( DEBUG )
            		{
            			Log.d(LOGTAG_POS, "   Button at mH.position "+
            					mH.position+" was clicked ");
            		}
            		
            		FragmentManager fm = ((Activity) mParentContext).getFragmentManager();
            		
            		// Pass ViewHolder in so can set correct List item instance 
            		DialogFragment newFragment = new TimerPickerDialogFragment(mH);
    				newFragment.show(fm, "timerPicker");
          		
                	Log.d(LOGTAG, "##### AFTER newFragment");	
    				
                }	// end onClick()
            });  	// end holder.cirSetTimerButton.setOnClickListener(new Button.OnClickListener()  
            
            
            

            
            
            
        }	// end convertView == null
        else 
        {
        	// At this point, holder is still pointing to the view / ListItem  
        	// that got scrolled off. Need to replace this info with the info
        	// that was saved in the convertView "Tag" in above section, i.e. 
        	// convertView == null the first time this item was displayed.
        	holder = (ViewHolder) convertView.getTag();
   	
        }
        
        
        /*********************************************************************/
        /*********************************************************************/
        /*********************/
        ManageViewHolderElements( holder, position );
        
        Log.d(LOGTAG_POS, "Exitting getView");   
        return convertView;
    }
    
    
    
    /*********************/
    
    /* 
     * Update ListItem elements, i.e. toggle button state & 
     * timer-related fields when toggle button state changes.
     * Called in "if (convertView == null)" section of getView() (not scrolled)
     * within the holder.cirOnOffTButton.setOnClickListener definition in the 
     * onClick() method. Executed when toggle button state changes (doesn't 
     * matter whether toggle button is checked or not.
     * ONLY CALLED ON TOGGLE BUTTON CLICK
     *  
     */
    private void UpdateListItemElementsState( boolean isChecked, int pos1, long startTime, long stopTime )
    {
    	// This position should be the position in the List
    	Log.d(LOGTAG_POS, "In UpdateListItemElementsState   pos1 = "+ pos1);
    	ListItem li;
		li = list.get(pos1);
		int b = (isChecked ? 1 : 0 );
		li.setToggleBtnState(b);
		
		if ( isChecked )
		{
			// Save startTime to list, chronometer is running
			li.setStartTime(startTime);
		}
		else
		{
			Log.d(LOGTAG, "In else");
			
			// Chronometer is not running, save stopTime ??and duration 
			li.setStopTime( stopTime );
			
			// Set Duration
			//WAS 3/2 li.setDuration( li.getStopTime() - li.getStartTime());
			/*Don't think I want to change this duration
			Log.d(LOGTAG, "Before SetDuration CHECK!  li.getDuration() = "+li.getDuration());
			li.setDuration( li.getStopTime() - li.getStartTime());
			Log.d(LOGTAG, "CHECK!  li.getDuration() = "+li.getDuration());
			*/
			
			// SLM_NOTE: logDuration will replace duration
			li.setLogDuration( li.getStopTime() - li.getStartTime());
			
		}
		
		// Update List to reflect chrono-related info, startTimer/stopTime 
		// and ToggleButton state
		list.set( pos1,  li );
		
		if ( DEBUG )
    	{
    		Log.d(LOGTAG_POS, "1 list row "+pos1+" updated: "+
    					li.toStringAll()); 
    		Log.d(LOGTAG_POS, "*");
    		Log.d(LOGTAG_POS, "*");
    	}
    }
    
    /*********************/
    
    /* 
     * Handles setting list row info when row is visible (includes when a row 
     * becomes visible due to scrolling).
     * Called completely outside of the "if (convertView == null)" section of 
     * getView(), near end of the method. 
     * 
     * SLM? Handles Timer/Chrono only because this element needs to stop being updated when it is out of view
     * (i.e. scrolled off screen) and restarted at the appropriate increment in time when it is scrolled
     * back onto the screen.
     * 
     * Updated 2/28
     * Updated 3/11
     */
    
    private void ManageViewHolderElements( ViewHolder holder, int position )
    {       
    	Log.d(LOGTAG_POS, "In ManageViewHolderElements, position = "+position);
    	
        ListItem item = list.get(position);
        
        Log.d(LOGTAG_POS, "ListItem["+position+"] = "+item.toStringAll());
        
        holder.cirNumTV.setText(item.cirNum);
        holder.cirNameTV.setText(item.cirName);
        
        holder.cirDurationSTTV.SetTime( item.displayDuration ); //SLM Correct duration?? YES
        
        // Need for sending correct item info to UpdateListItemElementsState
        holder.position = item.getPosition();
        
        holder.cirOnOffTButton.setChecked((item.toggleBtnState == 1));
        
        if ( DEBUG )
        {
        	Log.d(LOGTAG_POS, "toggle button at "+holder.position+" = "+ 
        				(item.toggleBtnState == 1));
        }
        // If toggle button is on
        if  (item.toggleBtnState == 1)
        {
        	if ( DEBUG )
        		Log.d( LOGTAG, "Handling togglebutton == true, startTime = "+
        				item.getStartTime());

        	// Need to set chronometer's startTime & call start  
        	holder.cirChronoSTV.SetStartTime(item.getStartTime());
    		holder.cirChronoSTV.PostTimerDelayed(0);
        }
        else	// Toggle button is off
        {	
        	if (DEBUG )
        	{
        		Log.d( LOGTAG_POS, 
        			"Handling togglebutton == false, try setting to starttime");
        	}
        	// togglebutton is off. chronometer must show the duration
        	// of it's last run (if it ran).
        	// This will be 0 if the chronometer has never been turned on.
        	
        	// To get the value of the last duration, need to 
        	// subtract the stopTime (stored in the list item) from 
        	// the SystemClock.elapsedRealtime() (or current time)
        	//SLMTimer  long tmpTime = SystemClock.elapsedRealtime()-item.getStopTime();    	
        	
        	if ( item.getStartTime() != 0 )	
        	{	// Chronometer is stopped (at some non-zero time)
        		long tmpTime = item.getStopTime() - item.getStartTime();
        		holder.cirChronoSTV.RemoveTimerCallbacks();
        		holder.cirChronoSTV.SetTime(tmpTime);
           	}
        	else	
        	{	// Chronometer is in initial state, has never run & stopped
        		if ( DEBUG )
        			Log.d( LOGTAG, "startTime  = 0, stop Chrono and reset" );
        		
        		// Chronometer is in initial state, has never run & stopped
        		holder.cirChronoSTV.RemoveTimerCallbacks();
        		holder.cirChronoSTV.SetTime(0);
        	}
        }

         if ( DEBUG )
        {	Log.d(LOGTAG_POS, "End ManageViewHolderElements()");
        	Log.d(LOGTAG_POS, "*");
        	Log.d(LOGTAG_POS, "*");
        }
    }
    
    

    /*********************/
    
    private LayoutInflater getLayoutInflator() 
    {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater)
                this.mParentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }      
        return layoutInflater;
    }
    
    
    
    // ViewHolder should contain data that is static for each given row in list,
    // per Google talk on ListViews.
	static class ViewHolder 
	{
//		ListItem 		listItem;	// Not sure correct per above comment
	    int 			position;	// Not sure correct per above comment
	    							// 	But saw example on Android Developers
	    							//	that showed position in the ViewHolder. 
	    							//	Unfortunately it didn't show it's usage.
	    TextView  		cirNumTV;
	    TextView  		cirNameTV;
	    //SLMTimer 		Chronometer cirChrono;
	    STimerView 		cirChronoSTV;
	    ToggleButton 	cirOnOffTButton;
	    Button			cirSetTimerButton;
	    STimeTextView 	cirDurationSTTV;
	    long			cirDurationMs;	// Not sure correct per above comment
	    
	}
} 



