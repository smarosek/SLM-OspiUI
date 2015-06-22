package com.slm.ospiui.model;

/**
 * @author Susan Marosek
 *
 */
/*
 * Modification History:
 * 	
 * 06/11/15 Added mlStartTime, mlStopTime and duration to the class and changed 
 * 				mbCircOn to mbIsRunning.
 * 06/16/15 Added cases for specific Manual Mode, Program Schedule, and Log for 
 * 				start time, stop time and duration. Not sure if this is final 
 * 				or not.	
 */
public class Circuit {
	
	public static final int	CIR_MANUAL_MODE 	= 1;
	public static final int	CIR_PROGRAM_SCHED 	= 2;
	public static final int	CIR_LOG 			= 3;
	public static final int	CIR_OTHER 			= 4;
	
	
	// Circuit - general info
	int 	miCircNum;
	String 	msCircName;
	boolean mbCircIsRunning;
	boolean mbIsMaster;
	
	// Circuit - Manual Mode Info
	long	mlStartTimeMM;
	long	mlStopTimeMM;
	long	mlDurationMM;
	long	mlExpiredTimeMM;
	
	// Circuit - Program Schedule Info
	long	mlStartTimePS;
	long	mlStopTimePS;
	long	mlDurationPS;
	
	// Circuit - Log Info
	long	mlDurationLog;
	
	// Circuit - Other ??
	
	
	public Circuit()
	{
		mbIsMaster = false;
		
	}
	
	public int getCircNum() {
		return miCircNum;
	}
	
	public void setCircNum(int circNum) {
		this.miCircNum = circNum;
	}
	
	public String getCircName() {
		return msCircName;
	}
	
	public void setCircName(String circName) {
		this.msCircName = circName;
	}
	
	public boolean isCircIsRunning() {
		return mbCircIsRunning;
	}
	
	public void setCircIsRunning( boolean circIsRunning ) {
		this.mbCircIsRunning = circIsRunning;
	}
	
	
	public boolean isMaster() {
		return mbIsMaster;
	}
	
	public void setIsMaster( boolean master ) {
		this.mbIsMaster = master;
	}
	
	
	//Activity specific info
	public long getStartTime( int act ) 
	{
		long retStartTime = -1;
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				retStartTime =  mlStartTimeMM;
				break;
			case CIR_PROGRAM_SCHED: 
				retStartTime =  mlStartTimePS;
				break;
			case CIR_LOG: 
//				retStartTime =  mlStartTimeLog;
//				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
		return retStartTime;
		
	}
	
	public void setStartTime( int act, long starttime ) 
	{
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				this.mlStartTimeMM = starttime;
				break;
			case CIR_PROGRAM_SCHED: 
				this.mlStartTimePS = starttime;
				break;
			case CIR_LOG: 
//				this.mlStartTimeLog = starttime;
//				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
		
	}
	
	
	public long getStopTime( int act ) 
	{	
		long retStopTime = -1;
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				retStopTime =  mlStopTimeMM;
				break;
			case CIR_PROGRAM_SCHED: 
				retStopTime =  mlStopTimePS;
				break;
			case CIR_LOG: 
//				retStopTime =  mlStopTimeLog;
//				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
		return retStopTime;
	}
	
	public void setStopTime( int act, long stoptime ) 
	{
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				this.mlStopTimeMM = stoptime;
				break;
			case CIR_PROGRAM_SCHED: 
				this.mlStopTimePS = stoptime;
				break;
			case CIR_LOG: 
//				this.mlStopTimeLog = stoptime;
//				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
	}
	
	
	public long getDuration( int act ) 
	{

		long retDuration = -1;
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				retDuration =  mlDurationMM;
				break;
			case CIR_PROGRAM_SCHED: 
				retDuration =  mlDurationPS;
				break;
			case CIR_LOG: 
				retDuration =  mlDurationLog;
				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
		return retDuration;
	}
	
	
	
	public void setDuration( int act, long duration ) 
	{
		switch ( act )
		{
			case CIR_MANUAL_MODE: 
				this.mlDurationMM = duration;
				break;
			case CIR_PROGRAM_SCHED: 
				this.mlDurationPS = duration;
				break;
			case CIR_LOG: 
				this.mlDurationLog = duration;
				break;
			case CIR_OTHER: 
				break;
			default:
				break;	
		}
	}

	
	public void storeManualModeInfo( long starttime, long stoptime, long duration ) 
	{
		int act = CIR_MANUAL_MODE;
		
		setStartTime( act, starttime );
		setStopTime( act, stoptime );
		setDuration( act, duration );
	

	}
	
	
	
	@Override
	public String toString() {
		return 
			"Circuit [ miCircNum = " + miCircNum + ", msCircName = " + msCircName
			+ ", mbCircIsRunning = " + mbCircIsRunning + ", mbIsMaster = " + mbIsMaster
			+ ", mlStartTimeMM = " + mlStartTimeMM + ", mlStopTimeMM = " + mlStopTimeMM
			+ ", mlDurationMM = " + mlDurationMM 
			+ ", mlStartTimePS = " + mlStartTimePS + ", mlStopTimePS = " + mlStopTimePS
			+ ", mlDurationPS = " + mlDurationPS
//			+ ", mlStartTimeLog = " + mlStartTimeLog + ", mlStopTimeLog = " + mlStopTimeLog
			+ ", mlDurationLog = " + mlDurationLog
			+" ]";
	}
	
}

