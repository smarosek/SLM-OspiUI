package com.slm_ospiui;

import android.util.Log;

/**
 * @author Susan L Marosek
 * @version 0.1
 * @ListItem - Defines the content of a List Item for the Manual Mode screen. 
 */
  
/*
 * Modification History:
 * 	
 * 	02/25/15 Added displayDuration and logDuration members and will remove  
 * 				duration soon. Not sure that I'll use the displayDuration, may
 * 				only need it in ViewHolder to save the value of a timer that
 * 				is not currently running.
 */
public class ListItem 
{
	
	private static final String LOGTAG = "ListItem";
	
	String 	cirNum;
	int		cirNumber;
    String 	cirName;
   
    
	int 	cirType; 			// 1=> MASTER; 0=> REGULAR
    int 	toggleBtnState;		// Off / On
    long 	startTime;			// Log	
    long 	stopTime;			// Log
    long	displayDuration;	// Display duration (of watering)
    long 	logDuration;		// Log duration of watering (stop - start)
//    long 	duration;			// Log duration of watering (stop - start) - deprecate
    private int 	position;
    
    public ListItem ( String cirNum, String cirName, int cirType, int position )
    {
    	this.cirNum = cirNum;
    	this.cirName = cirName;
    	this.cirType = cirType;
    	this.position = position;
    	this.toggleBtnState = 0;  //OFF
    	this.startTime = 0;
    	this.displayDuration = 0;
    	this.logDuration = 0;
//    	this.duration = 0;
    	this.stopTime = 0;
    	
    	
    }
    public ListItem ( String cirNum, int cirNumber, String cirName, int cirType, int position )
    {
    	this.cirNum = cirNum;
    	this.cirNumber = cirNumber;
    	this.cirName = cirName;
    	this.cirType = cirType;  
    	this.position = position;
    	this.toggleBtnState = 0;  //OFF
    	this.startTime = 0;
    	this.displayDuration = 0;
    	this.logDuration = 0;
//    	this.duration = 0;
    	this.stopTime = 0;
    	
    	
    }
    
    public String getCirNum ()
    {
    	return this.cirNum;
    }
    public void setCirNum(String cirNum) 
    {
		this.cirNum = cirNum;
	}
    public String getCirName ()
    {
    	return this.cirName;
    }
    public void setCirName(String cirName) 
    {
		this.cirName = cirName;
	}
    public int getCirNumber() {
		return cirNumber;
	}
    public void setCirNumber ( int number )
    {
    	this.cirNumber = number;
    }
    public int getCirType ()
    {
    	return this.cirType;
    }
    public int getPosition ()
    {
    	return this.position;
    }
    public void setPosition ( int position )
    {
    	Log.d(LOGTAG,  "### IN ListItem SetPosition !!!!!");
    	this.position = position;
    }
    public int getToggleBtnState ()
    {
    	return this.toggleBtnState;
    }
    public void setToggleBtnState ( int state )
    {
    	this.toggleBtnState = state;
    }
    public long getStartTime ()
    {
    	return this.startTime;
    }
    public void setStartTime ( long startTime )
    {
    	this.startTime = startTime;
    }
    public long getStopTime ()
    {
    	return this.stopTime;
    }
    public void setStopTime ( long stopTime )
    {
    	this.stopTime = stopTime;
    }
    public long getDisplayDuration()
    {
    	return this.displayDuration;
    }
    public void setDisplayDuration ( long dispDuration )
    {
    	this.displayDuration = dispDuration;
    }

    
    public long getLogDuration ()
    {
    	return this.logDuration;
    }
    public void setLogDuration ( long logDuration )
    {
    	this.logDuration = logDuration;
    }
    
    /*
    public long getDuration ()
    {
    	return this.duration;
    }
    */
    /*
    public void setDuration ( long duration )
    {
    	this.duration = duration;
    }
    */
    int GetDDurationMin()
    {
    	int minutes = (int)(displayDuration / 1000 / 60);
    	Log.d( LOGTAG, "In GetDDurationMin() minutes = "+minutes);
    	return minutes;
    
    }
    int GetDDurationSec()
    {
    	int seconds = (int)((displayDuration / 1000) % 60);
    	Log.d( LOGTAG, "In GetDDurationSec() seconds = "+seconds);
    	return seconds;
    }
    
    public String toString()
    {
    	String retStr = "cirNum = " + cirNum + " cirName = " + cirName+ 
    			" cirType = " + cirType + "  toggleBtnState = "+ (((toggleBtnState==0) ? "OFF" : "ON"));
    	return retStr;
    }
    public String toStringAll()
    {
    	String retStr = "cirNum = " + cirNum + " cirName = " + cirName+ 
    			" cirType = " + cirType + "  toggleBtnState = "+ 
    			(((toggleBtnState==0) ? "OFF" : "ON")+ 
    			"  startTime = "+startTime+"  stopTime = "+stopTime+
    			"  displayDuration = "+displayDuration+
    			"  logDuration = "+logDuration+
    			"  position = "+position);
    	return retStr;
    }
}
