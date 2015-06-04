package com.slm_ospiui;

/**
 * @author Susan L Marosek
 * @version 0.1
 * @ChangeProgramMessage - use to create a cp or change program request to the 
 * Ospi web app 
 */

/*
 * Modification History:
 * 	
 * 	03/31/15 Created
 *	04/15/15 Currently just creates a new program with pid hard-coded to -1.
 *	04/16/15 Added enabled and pid parameters to the constructor so now we
 *				can specify whether to change an existing program or add a
 *				new one. Still no validation of pid values.
 *	06/02/15 Changed name from ChangeProgramEvent to ChangeProgramMessage.
 *
 */

public class ChangeProgramMessage extends OspiMessage
{
//	private static final String LOGTAG = "ChangeProgramMessage";
	
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

	
	// 8 integer values for command (weird setup but this is how it works)
	public int mEnabled;	// Is the program enabled
	public int mPid;		// The pid of the program. Must be less than the 
							//	total number of programs. -1 if entering a 
							//	new program.
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
    public ChangeProgramMessage( int enabled, int pid, int days0, 
    		int starttime_hr, int starttime_min, int duration, int circuits )
    {
        this.mEnabled = enabled;		// 1 = true, program is enabled, false otherwise
        this.mPid = pid;
        this.mDays0 = days0;
        this.mDays1 = 0;				// HARDCODING to no restrictions
        
        this.mStartTime = starttime_hr*60+starttime_min;
        this.mEndTime = mStartTime+59;	// HARDCODING to mStartTime + 59 
        								//	minutes so program will only run once
        this.mDuration = duration*60;	// Convert from minutes to seconds for Web Service
        this.mInterval = 60;			// HARDCODING to 1 hour
        this.mCircuits = circuits;
        
       
       // Create the Ospi change program request
        mMessage = URL+
        		CHANGE_PROGRAM+
        		PASSWD+
        		PROGRAM_ID+
        		mPid+
        		VALUES_BEGIN+
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
    
    public void onOspiEvent()
    {
    	
    }
    
    
}
