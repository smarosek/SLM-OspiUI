package com.slm_ospiui;

import android.util.Log;

public class CircuitOnOffEvent {

	private static final String LOGTAG = "CircuitOnOffEvent";
	
	public static final int CIRCUIT_OFF = 0;
	public static final int CIRCUIT_ON = 1;
	
	// For now make this a static final string (updated 1/15/15)
    static final String URL = "http://192.168.1.41:8080";
    
    static final String CMD_URL = "http://192.168.1.41";
    static final String CONTROLLER_VALUES = "/cv?";
    static final String PASSWD = "pw=opendoor";
    static final String MANUAL_MODE = "&mm=";
    static final String STATION_NUMBER = "/sn";		
	
	// Kinds of commands
	public static final int SET_MANUAL_MODE = 100;
	// Set controller to manual mode => http://x.x.x.x/cv?pw=opendoor&mm=1
	
	public static final int SET_MASTER_CIRCUIT = 200;
	// Turn on circuit => http://x.x.x.x/sn1=1 (turn on the first station (My Master))
	
	public static final int SET_CIRCUIT_ON_OFF_STATUS = 300;
	// Turn on circuit => http://x.x.x.x/sn1=3 (turn on the third station)
	
	//public final String message;
	public String message;
	
	public int mStatus;
	public int mCircuitNum;
	public int mMsgType;
/*
    public CircuitOnOffEvent(String message) 
    {
        this.message = message;
    }
  */  
    //public CircuitOnOffEvent( int msgType, int circuitNum, int status )
    public CircuitOnOffEvent( int status, int circuitNum )
    {
        this.mStatus = status;
        this.mCircuitNum = circuitNum;
        
        
        // TEMPORARY switch here
        switch ( circuitNum )
        {
	        case 1:
	        	this.mMsgType = SET_MANUAL_MODE;
	        	break;
//	        case 2:
//	        	this.mMsgType = SET_MASTER_CIRCUIT;
//	        	break;
//	        case 3:
//	        case 4:
	        default:
	        	Log.d(LOGTAG, "In CircuitOnOffEvent Setting MsgType");
	        	this.mMsgType = SET_CIRCUIT_ON_OFF_STATUS;
	        	break;
        }
        
        // Build command message based on mMsgType
        switch ( mMsgType )
        {
	        case SET_MANUAL_MODE:
	        	message = URL+CONTROLLER_VALUES+PASSWD+MANUAL_MODE+mStatus;
	        	Log.d( LOGTAG, message );
	        	break;
	        case SET_MASTER_CIRCUIT:
	        	if ( mStatus == 1 )
	        		mStatus++;
	        	message = URL+STATION_NUMBER+"1"+"="+mStatus;
	        	break;
	        case SET_CIRCUIT_ON_OFF_STATUS:
	        default:
	        	// This works   
	        	message = URL+STATION_NUMBER+mCircuitNum+"="+mStatus+"&t=0";
	        	//if ( mStatus == 1 )
	        	//	mStatus++;
	        	//message = URL+STATION_NUMBER+mCircuitNum+"="+mStatus;
	        	Log.d( LOGTAG, "CircuitOnOffEvent message= ** "+message+" **" );
	        	break;
        }
       
    }
}
