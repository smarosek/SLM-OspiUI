package com.slm_ospiui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

public class STimerView extends STimeTextView 
{

	private static final String LOGTAG = "STimerView";
	
	private static final boolean DEBUG = false;
	private static final boolean DEBUG_TIMER = false;
	
	
	private ArrayList<TimerStopListener> timerStopListenerList = new ArrayList<TimerStopListener>();

	
//	private static final long DEFAULT_TIMEOUT = 10000;
	private boolean countDown = false;
	private boolean isTimeOut = false;
	
	private long mTimeOut = 0;  		//20000;	
	private long mStartTime = 0;
	
    //runs without a timer by reposting this handler at the end of the runnable
    private Handler mTimerHandler = new Handler();
	
	Activity a;
	
	STimerView s = this;

	public STimerView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    
	    a = (Activity) this.getParent();

	}
	
	public void SetStartTime( long time )
	{
		mStartTime = time;
	}
	
	public void SetDuration ( int minutes, int seconds )
	{		
		this.mTimeOut = seconds*1000 + minutes*1000*60;
		isTimeOut = true;
		
		if ( DEBUG )
			Log.d( LOGTAG, " In SetDuration(int, int)  mTimeOut = "+ mTimeOut);
	}
	
	
    Runnable timerRunnable = new Runnable() 
    {
        @Override
        public void run() 
        {
        	if( DEBUG_TIMER )
        		Log.d( LOGTAG, "In timerRunnable");
        	
            //SLM WAS long millis = System.currentTimeMillis() - mStartTime;
            long millis;
            
            if ( countDown )
            {
            	millis = mTimeOut - (System.currentTimeMillis() - mStartTime);
            	
            }
            else
            {
            	millis = System.currentTimeMillis() - mStartTime;
            }
            
            SetTime( millis );
            
            if( DEBUG_TIMER )
            	Log.d( LOGTAG, "In timerRunnable   "+
            		"  millis = " + millis + 
            		"   mTimeOut = " + mTimeOut );
            
            if ( !isTimeOut || millis < mTimeOut )
            	mTimerHandler.postDelayed(this, 1000);           	
            
            else
            {
            	mTimerHandler.removeCallbacks(this);
            	Log.d( LOGTAG, "In timerRunnable  - SHOULD STOP ");
            	fireTimerStop(new TimerStopEvent( s ));
            }            
        }
    };
    
    
    public Handler GetTimerHandler()
    {
    	if ( DEBUG )
    		Log.d( LOGTAG, "In GetTimerHandler");
    	return mTimerHandler;
    }
    
    public void RemoveTimerCallbacks()
    {
    	if ( DEBUG )
    		Log.d( LOGTAG, "In RemoveTimerCallbacks");
    	mTimerHandler.removeCallbacks(timerRunnable);
    }
    
    public void PostTimerDelayed( int delay )
    {
    	Log.d( LOGTAG, "In PostTimerDelayed");
    	// If this is the first time we're calling PostTimerDelayed then need
    	// to establish a starting time. Especially important to keep mStartTime 
    	// to initial setting when the timer is scrolled off the screen while 
    	// running. Don't want timer to get reset to 0
    	//
    	if ( mStartTime == 0 )
    		mStartTime = System.currentTimeMillis();
    	
    	mTimerHandler.postDelayed( timerRunnable, delay);
    }
    
    
    @Override
    public void SetTime( long ms )
    {
    	super.SetTime( ms );
      
    	if (ms <= 0 )
        	RemoveTimerCallbacks();
    }
    
    @Override
    public void SetTime( int minutes, int seconds )
    {
    	super.SetTime( minutes, seconds );
    	
    	if (mTimeOut <= 0 )
        	RemoveTimerCallbacks();
    }
    
 

    
    
    /*********************/
  //Register an event listener
  	public synchronized void addTimerStopListener(TimerStopListener listener) 
  	{
  		Log.d( LOGTAG, "In addTimerStopListener");
  		
  		if (!timerStopListenerList.contains(listener)) 
  		{
  			Log.d( LOGTAG, "HERE 2");
  			timerStopListenerList.add(listener);
  		}
  	}
  	
  	
  	protected void fireTimerStop(TimerStopEvent timerStopEvent) 
  	{ 		
  	     Object[] listeners = timerStopListenerList.toArray();
  	     // loop through each listener and pass on the event if needed
  	     int numListeners = listeners.length;
  	     
  	     Log.d( LOGTAG, "In fireTimerStop length = "+numListeners );
  	   
	     for (int i = 0; i<numListeners; i++) 
	     { 
        	  Log.d( LOGTAG, "HERE 3 " );
               // pass the event to the listeners event dispatch method
                ((TimerStopListener)listeners[i]).OnTimeout(timerStopEvent);           
	     }
  	}

}
