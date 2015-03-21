package com.slm_ospiui;

import java.util.ArrayList;


//import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class STimeTextView extends TextView 
{
	private static final String LOGTAG = "STimeTextView";
	private static final boolean DEBUG = false;

	public static final int MINUTES = 0;
	public static final int SECONDS = 1;
	
	private ArrayList<TimerSetListener> timerSetListenerList = new ArrayList<TimerSetListener>();
	   
	private long mTimeMs = 0; 
	 
	public STimeTextView(Context context, AttributeSet attrs) 
	{
	    super(context, attrs);
	}
    
	public long GetTimeMs()
	{
		return mTimeMs;
	}
	
   // @Override
    public void SetTime( long ms )
    {
    	if( DEBUG )
    		Log.d( LOGTAG, "In SetTime( long ms)   ms = "+ms);
    	//long millis = System.currentTimeMillis() - mStartTime;
    	
    	//SLM 225 mTimeOut = ms;
    	
    	int seconds, minutes;
    	
		seconds = (int) (ms / 1000);
		minutes = seconds / 60;
		seconds = seconds % 60;

		if( DEBUG )
			Log.d( LOGTAG, "In SetTime  minutes = "+minutes+ "   seconds = "+seconds);
		
        setText(String.format("     %02d:%02d", minutes, seconds));
        
  //      if (ms <= 0 )
    //    	RemoveTimerCallbacks();
    }
    
    public void SetTime( int minutes, int seconds )
    { 
    	if( DEBUG )
    		Log.d( LOGTAG, "In SetTime( int minutes, int seconds)  minutes = "+minutes+"   seconds= "+seconds);
    	
    	mTimeMs = seconds*1000 + minutes*1000*60;
    	//long mTimeOut = seconds*1000 + minutes*1000*60;
    	
    	setText(String.format("     %02d:%02d", minutes, seconds));
    	
    	this.fireTimerSet(new TimerSetEvent(this, "s"));
    	
    	
    	//if( DEBUG_TIMER )
    	//	Log.d(LOGTAG,  "a mTimeOut = "+this.mTimeOut );
    	
    	//if (mTimeOut <= 0 )
        //	RemoveTimerCallbacks();
    }
    
    
    public int GetMinutesValue()
	{
    	return GetDigitsValue(MINUTES);
	}
    
    public int GetSecondsValue()
	{
    	return GetDigitsValue(SECONDS);
	}
    
	private int GetDigitsValue( int pos )
	{
		int value;
		String posStr = null;
		String strVal = ((String) this.getText()).trim();
		
		if ( DEBUG )
			Log.d(LOGTAG,  "strVal = *"+strVal+"*");
		
		String[] tokens = strVal.split(":");
		
		if ( DEBUG )
			Log.d(LOGTAG,  "tokens.length = "+tokens.length);
	
		try
		{	posStr = tokens[pos];
			value = Integer.parseInt(posStr);
			
			if ( DEBUG )
				Log.d( LOGTAG, "In GetDigitsValue  posStr = "+posStr +"  value = "+ value);
		}
		catch ( StringIndexOutOfBoundsException ex)
		{
			Log.d( LOGTAG, "In GetDigitsValue SIOOB  value = 0");
			value = 0;
		}
		catch ( NumberFormatException ex)
		{
			Log.d( LOGTAG, "In GetSecondsValue NFE  value = 0");
			value = 0;
		}
		return value;
	}
    
    
    /*********************/
  //Register an event listener
  	public synchronized void addTimerSetListener(TimerSetListener listener) 
  	{
  		if ( DEBUG )
  			Log.d( LOGTAG, "In addTimerSetListener");
  		
  		if (!timerSetListenerList.contains(listener)) 
  		{
  			timerSetListenerList.add(listener);
  		}
  	}
  	
  	
  	protected void fireTimerSet(TimerSetEvent timerSetEvent) 
  	{
  	     Object[] listeners = timerSetListenerList.toArray();
  	     // loop through each listener and pass on the event if needed
  	     int numListeners = listeners.length;
  	     
  	     if ( DEBUG )
  	    	 Log.d( LOGTAG, "In fireTimerSet length = "+numListeners );
  	   
  	   
	     for (int i = 0; i<numListeners; i++) 
	     {
	    	 //NOTE: no help from the getClass() calls
	    	 
	    	 if ( DEBUG )
	    	 {
		    	 // This is Ljava.lang.Object;@b20d2d48
		    	 Log.d(LOGTAG, "listeners = "+listeners.toString() );
		    	 // This is Ljava.lang.Object
		    	 Log.d(LOGTAG, "listeners.getClass() = "+listeners.getClass().toString() );
		    	 // This is class com.slm.ospiui.CustomListAdapter$1
		    	 Log.d(LOGTAG, "listeners[i].getClass = "+listeners[i].getClass().toString() );
	    	 }
//	          if (listeners[i]==TimerSetListener.class) 
//	          {
	        	  //Log.d( LOGTAG, "HERE 1 " );
	               // pass the event to the listeners event dispatch method
	                ((TimerSetListener)listeners[i]).OnTimerSet(timerSetEvent);
//	          }            
	     }
  	}

}
