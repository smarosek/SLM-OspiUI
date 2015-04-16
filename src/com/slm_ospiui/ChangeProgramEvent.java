package com.slm_ospiui;

import android.util.Log;

/**
 * @author Susan L Marosek
 * @version 0.1
 * @ChangeProgramEvent - use to create a cp or change program request to the 
 * Ospi web app 
 */

/*
 * Modification History:
 * 	
 * 	03/31/15 Created
 *	04/15/15 Currently just creates a new program with pid hard-coded to -1.
 *
 */

public class ChangeProgramEvent 
{
	private static final String LOGTAG = "ChangeProgramEvent";
	
	// For now make this a static final string (updated 1/15/15)
    static final String URL = "http://192.168.1.41:8080";
    
//    static final String CMD_URL = "http://192.168.1.41";
    static final String CHANGE_PROGRAM = "/cp?";
    static final String PASSWD = "pw=opendoor";
    static final String PROGRAM_ID = "&pid=";
    static final String VALUES_BEGIN = "&v=[";	
    static final String VALUES_END = "]";	
    static final String WEIRD_END_STRING = "&rad_en=on&rad_day=on&rad_rst=on";
	
	
	//public final String message;
	public String message;
	
	// 8 integer values for command (weird setup but this is how it works)
	public int mEnabled;	// Is the program enabled
	public int mDays0;		// Selected days to run (bit 0 corres. to Monday)
	public int mDays1;		// Restrictions:
							//	if 1, even days restricted
							//	if 2, odd days restricted
							//	if 0, no restrictions
	public int mStartTime;	// Start time in minutes: 360 corresponds to 6AM
	public int mEndTime;	// End time in minutes	
	public int mDuration;	// Duration of run for stations in seconds
	public int mInterval;	// Program interval in minutes, 240 = run every 
							//	4 hours until end time.
	public int mCircuits;	// Circuits to turn on for this program.
	

	// Constructor
    public ChangeProgramEvent( int days0, int starttime_hr, int starttime_min, int duration, int circuits )
    {
        this.mEnabled = 1;				// HARDCODING to true / enabled
        this.mDays0 = days0;
        this.mDays1 = 0;				// HARDCODING to no restrictions
        
        this.mStartTime = starttime_hr*60+starttime_min;
        this.mEndTime = mStartTime+59;	// HARDCODING to mStartTime + 59 
        								//	minutes so program will only run once
        this.mDuration = duration*60;	// Convert from minutes to seconds for Web Service
        this.mInterval = 60;			// HARDCODING to 1 hour
        this.mCircuits = circuits;
        
       
       //SLM0414 Changed Program ID from "0" to "1" in hopes of not messing up real schedule
        message = URL+CHANGE_PROGRAM+PASSWD+PROGRAM_ID+"-1"+VALUES_BEGIN+
        		mEnabled+","+
        		mDays0+","+
        		mDays1+","+
        		mStartTime+","+
        		mEndTime+","+
        		mInterval+","+
        		mDuration+","+
        		mCircuits+
        		VALUES_END+
        		WEIRD_END_STRING; // Don't know what this is/means, or if it's needed 
       
    }	
}
