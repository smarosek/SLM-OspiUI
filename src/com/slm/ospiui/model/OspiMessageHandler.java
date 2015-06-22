/**
 * 
 */
package com.slm.ospiui.model;

//import org.jsoup.Jsoup;

import android.content.Context;
import android.util.Log;

import com.slm_ospiui.ChangeProgramMessage;
import com.slm_ospiui.CircuitOnOffMessage;
import com.slm_ospiui.OspiGetResultsAsyncTask;
//import com.slm_ospiui.OspiResultsListener;


import com.slm_ospiui.OspiMessage;
import com.slm_ospiui.SimpleOspiMessage;

import de.greenrobot.event.EventBus;

/**
 * OspiMessageHandler: Message Handler that sends the Open Sprinkler Pi commands 
 * to the Open Sprinkler Pi WebApp.
 *
 * 	Possible commands include: 
 * 		1. Change controller values (password required) [Keyword /cv]:
 * 			/cv?pw=xxx{&rsn=x}{&rbt=x}{&en=x}{&mm=x}{&rd=x} 
 * 			Ex: http://x.x.x.x/cv?pw=opendoor&mm=1 (switch controller to manual mode)
 * 		2. Set station bits [Keyword /sni=]:
 * 			/sni=x (where i = a station number & x = 0 for Off, 1 for On)
 * 			Ex: http://x.x.x.x/sn1=1 (turn on the first station)
 * 		3. Get station bits [Keyword /sni]:
 * 			/sni: i is positive station index (starts from 1). sn0 = all stations.
 * 			Ex: http://x.x.x.x/sn1 (returns binary status of the first station)
 * 			Ex: http://x.x.x.x/sn0 (returns binary status of every station)
 * 		4. Change station names & master operation bits (password required) [Keyword /cs]:
 * 			/cs?pw=xxx{&s0=xxx}{&s1=xxx}{&s2=xxx}.....{&m0=xxx}{&m1=xxx}
 * 			Ex: http://x.x.x.x/cs?pw=opendoor&s0=front%20lawn&s1=back%lawn 
 * 						(sets first two stations names)
 * 		5. Change run-once program (password required) [Keyword /cr]:
 * 			/cr?pw=xxx&t=[xx,xx,xx,xx....]
 * 			Ex: http://x.x.x.x/cr?pw=opendoor&t=[60,0,60,0,60,0,60,0] 
 * 						(start a run-once program  that turns on stations 
 * 							1, 3, 5 and 7 for 1 minute each
 * 		6. Change (add/modify) programs (password required) [Keyword /cp]:
 * 			/cp?pw=xxx&pid=x&v=[x,x,x,x,x,x,x,x]&rad_en=on&rad_day=on&rad_rst=on
 * 			Ex: http:/192.168.1.41:8080/cp?pw=opendoor&pid=0&v=[1,10,0,900,959,
 * 				60,720,46]&rad_en=on&rad_day=on&rad_rst=on      
 *   			means change program 0 settings to: enable=true (1), 
 *   			days0=Tues & Thurs (10), days1=no restrictions(0), 
 *   			start time = 15:00 GMT (15.00*60=900), endtime = 15:59 GMT (959), 
 *   			interval - 1 hour (60 minutes), duration = 12 minutes each 
 *   			circuit (12*60 secs per min=720 secs), circuits=2,3,4 & 6(46).
 *   	7. Delete a program (password required) [Keyword /dp]:
 *   		/dp?pw=xxx&pid=x
 *   		Ex: http://x.x.x.x/pw=opendoor&pid=2 delete the third program
 *   	8. Change options (password required) [Keyword /co]:
 *   		/co?pw=xxx{&o*=xx}{&loc=xxx}{&npw=xxx}{&cpw=xxx}
 *   		Ex:http://x.x.x.x/co?pw=opendoor&npw=1234&cpw=1234 
 *   					(change password to 1234)
 *   
 *   	9. 6/1/15 NEW FROM SLM: Get programs.json from the RPi [Keyword /pgi]
 *   		/pgi: i is positive program index (starts from 1). pg0 = get all programs.
 *   		Note: program/schedule format is same as that in #6 /cp command.
 * 			Ex: http://x.x.x.x/pg0 (returns entire content of programs.json 
 * 				on Raspberry Pi)
 * 			Ex: http://x.x.x.x/pg1 (returns program 1's schedule info stored in
 * 				 programs.json on Raspberry Pi)
 *   
 * @author Susan Marosek
 *
 */
/*
 * Modification History:
 * 	
 * 	05/21/15 Created. 
 * 	05/28/15 Moved EventBus to this class (from 
 * 	06/02/15 Changed name from OspiMessage to OspiMessageHandler.
 *
 */
public class OspiMessageHandler
{
	private static final String LOGTAG = "OspiMessageHandler";
	
	public static final String DEFAULT_PROTOCOL = "http";
	public static final String DEFAULT_DOMAIN = "192.168.1.41";
	public static final String DEFAULT_PORT = "8080";
	
//	private String mProtocol;
//	private String mDestDomain;
//	private String mPort;
//	private int mCommandType;
//	private String mCommand;
//	private String mQuery; 
//	private String mResponse;
//	private String mURL;
	
	private int mMsgType;
	private Context mContext;
	
	/**
	 * Description: Should be instantiated in onStart() method of an Activity
	 * @param context - context of the containing Activity
	 * @param msgType - type of Jsoup message we'll need to invoke
	 */
	
	//SLM0602 Could call this the OspiBus or OspiEventBus ???
	public OspiMessageHandler( Context context, int msgType )
	{
//SLM0602  DO I want the msgType (which is a OSPI_GET or OSPI_EXECUTE) 
		// tied to the OspiMessageHandler, the OspiMessage or the OspiGetResultsMessage?
		// Well, it Belongs with the OspiGetResultsMessage because that is where it id used and it indicates whether
		// to expect a result or not. Is there a BUT??
		
		mContext = context;
        //SLMEB Register as a subscriber
        EventBus.getDefault().register(this);
        
        mMsgType = msgType;
	}
	

	
	/**
	 * This method is called when an OspiMessage is posted (some examples of
	 * this are in CustonListAdapter getView() (1) when the timer runs out for 
     * a running circuit and (2) when toggle on/off button in clicked in manual 
     * mode operation.  
	 * Every class that intends to receive events from the EventBus should
	 * contain an onEvent method. The name of this method is important, because 
	 * the EventBus library uses the Java Reflection API to access this method. 
	 * It has a single parameter that refers to the event.
	 * 
	 * @param event - the event that was posted to the EventBus - Basically
	 * 					we do the same thing for all event types so far.
	 */  
	public void onEvent( OspiMessage ospiMsg )	//WAS SendMessage( String msg, int msgType )
	{
		Log.d(LOGTAG, "msg = "+ospiMsg.mMessage);
    	
    	// Invoke an AsynTask to perform network operation on separate thread.
		//   mContext indicates which onOspiResults() method will be called.
		new OspiGetResultsAsyncTask( mContext, mMsgType ).execute(ospiMsg.mMessage);
	}
	
	
	/*SLM526 may not make sense to have EventBus in this POJO. Mostly 
	 * because of the register and more importantly the unregister.
	 */
	/**
	 * This method is called to post a ChangeProgramMessage to the EventBus.
	 * 
	 * @param enabled - indicates whether the program is enabled
	 * @param pid - indicates the ID of the program 
	 * @param days0 - means days of the week to run schedule
	 * @param starttime_hr - indicates "hours" portion of start time info
	 * @param starttime_min - indications "minutes" portion of start time info
	 * @param duration - indicates duration to run each circuit in the program
	 * @param circuits - indicates circuit numbers to be run as part of 
	 * 						this schedule
	 */
	public void OspiPostChangeProgramMessage( int enabled, int pid, int days0, 
    		int starttime_hr, int starttime_min, int duration, int circuits )
	{
		EventBus.getDefault().post(new ChangeProgramMessage(
				enabled, pid,
				days0, starttime_hr, starttime_min, 
				duration, circuits));
	}
	
	
	/**
	 * This method is called to post a CircuitOnOffMessage to the EventBus.
	 * 
	 * @param status - on/off status: CIRCUIT_OFF or CIRCUIT_ON (0/1)
	 * @param circuitNum - Indicates the circuit number to be turned off/on
	 * @param timeMS - Indicates time in ms.
	 */
	public void OspiPostCircuitOnOffMessage( int status, int circuitNum, long timeMS )
	{
		// OpenSprinkler Web App expects time in seconds so convert timeMS 
		int time = (int)(timeMS/1000);
		EventBus.getDefault().post(new CircuitOnOffMessage( status, circuitNum, time ));
	}

	
	/**
	 * Send a Jsoup connect GET message (command) and will expect a 
	 * 		Document result back.
	 * 
	 * @param command - The string containing the entire command string 
	 * 		(including URL) to be sent to the OpenSprinkler Web App.
	 */
	public void SendSimpleGETCommand( String command )
	{
		new OspiGetResultsAsyncTask( mContext, 
				OspiGetResultsAsyncTask.OSPI_GET ).execute(command);
	}

	/**
	 * Send a Jsoup connect EXEC message (command) and will NOT expect a 
	 * 		result back.
	 * 
	 * @param command - The string containing the entire command string 
	 * 		(including URL) to be sent to the OpenSprinkler Web App.
	 */
	public void SendSimpleEXECCommand( String command )
	{
		// Use AsynTask to perform network operation on separate thread
		new OspiGetResultsAsyncTask( mContext, 
				OspiGetResultsAsyncTask.OSPI_EXEC ).execute(command);
	}
	
	
	/**
	 * Unregister - Call from containing Activity's onStop method to unregister
	 * 		the OspiMessage from the EventBus
	 */
	public void Unregister()
	{
		//SLMEB
        EventBus.getDefault().unregister(this);
	}

	
	/**
	 * OspiMessageHandler Constructor 
	 * @param aCmdType - Type of command (OSPI_GET, OSPI_EXEC, etc.)
	 * @param aCmd - Ospi command to send
	
	public OspiMessageHandler( int aCmdType, String aCmd )
	{
		// If needed can set each part of URL rather than using DEFAULT strings
		mURL = DEFAULT_PROTOCOL + "://"+ DEFAULT_DOMAIN+":"+DEFAULT_PORT;
		mCommand = aCmd;
		mCommandType = aCmdType;
		mResponse = "";
		mQuery = "";

	}
	 */
	
	/**
	 * OspiMessageHandler Constructor 
	 * @param aCmdType - Type of command (OSPI_GET, OSPI_EXEC, etc.)
	 * @param aCmd - Ospi command to send
	 * @param aQuery - Query portion of the OspiMessageHandler (includes parameters for the command)
	
	public OspiMessageHandler( int aCmdType, String aCmd, String aQuery )
	{
		this( aCmdType, aCmd );
		mQuery = aQuery;
	}
	 */
	
	/*Doesn't get called here
	@Override
	public void onOspiResults(String result) 
	{
		Log.d(LOGTAG, "OspiMessageHandler GOT RESULT  = "+result);
		
	}
	*/
}
