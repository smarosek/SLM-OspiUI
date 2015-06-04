package com.slm_ospiui;

import android.util.Log;

/**
 * 
 * @author Susan Marosek
 * 		ProgSumListItem - Code description for program_summary_list_item.xml.
 * 		At a minimum contains Constructor, getters, setters & toString methods.
 */

/*
 * Modification History:
 * 	
 * 	04/23/15 Created.
 */

public class ProgSumListItem 
{
	private final String LOGTAG = ProgSumListItem.class.getSimpleName();
	
	public int mProgNum;
	public int mCircuits;	// Circuits to turn on for this program.
	public int mDays0;		// Selected days to run (bit 0 corres. to Monday)
	public int mStartTime;	// Start time in minutes: 360 corresponds to 6AM
	public int mDuration;	// Duration of run for stations in seconds
//	public int mInterval;	// Program interval in minutes, 240 = run every 
							//	4 hours until end time.
	public int mRepeatCount;
	
	
	
	
	/**
	 * @return the mProgNum
	 */
	public int getProgNum() 
	{
		return mProgNum;
	}
	
	/**
	 * @param mProgNum the mProgNum to set
	 */
	public void setProgNum(int progNum) 
	{
		this.mProgNum = progNum;
	}
	
	
	/**
	 * @return the mCircuits
	 */
	public int getCircuits() 
	{
		return mCircuits;
	}
	
	/**
	 * @param circuits - the mCircuits to set
	 */
	public void setmCircuits(int circuits) 
	{
		this.mCircuits = circuits;
	}
	
	
	/**
	 * @return the mDays0
	 */
	public int getDays0() 
	{
		return mDays0;
	}
	
	/**
	 * @param days0 - the mDays0 to set
	 */
	public void setmDays0(int days0) 
	{
		this.mDays0 = days0;
	}
	
	/**
	 * @return the mStartTime
	 */
	public int getStartTime() 
	{
		return mStartTime;
	}
	
	/**
	 * @param startTime - the mStartTime to set
	 */
	public void setmStartTime(int startTime) 
	{
		this.mStartTime = startTime;
	}
	
	
	/**
	 * @return the mDuration
	 */
	public int getDuration() 
	{
		return mDuration;
	}
	
	/**
	 * @param duration - the mDuration to set
	 */
	public void setmDuration(int duration) 
	{
		this.mDuration = duration;
	}
	
	/**
	 * @return the mRepeatCount
	 */
	public int getmRepeatCount() 
	{
		return mRepeatCount;
	}
	
	/**
	 * @param repeatCount - the mRepeatCount to set
	 */
	public void setmRepeatCount(int repeatCount) 
	{
		this.mRepeatCount = repeatCount;
	}
	
	/**
	 * @return a String representation of the mDays0 data member, such that
	 * mDays0 = 7 => binary 0111 returns "M, Tu, W"
	 */
	public String getDays0String( )
	{
		//Log.d(LOGTAG, "In getDays0String() mDays0 = "+mDays0 );
		
		String retStr = "";
		boolean first = true;
		int bitmask = 1;
		
		String [] daysA = { "M", "Tu", "W", "Th", "F", "Sa", "Su"};
		
		int daysTester = mDays0;
		int val = 0;
		
		for ( int i=0; i<7; i++ )
		{
			val =  daysTester & bitmask;
			if ( val == 1 )
			{
				if ( !first )
					retStr+=",";
				retStr+=daysA[i];				
				first = false;
			}
			daysTester = daysTester >> 1;
		}
		return retStr;
	}
	

	/**
	 * @return a String representation of the mCircuits data member, such that
	 * mCircuits = 5 => binary 0101 returns "1, 3"
	 */
	public String getCircuitsString( )
	{
		String retStr = "";
		boolean first = true;
		int bitmask = 1;
		
		String [] circsA = {"1", "2", "3", "4", "5", "6", "7", "8" };
		
		int circTester = mCircuits;
		int val = 0;
		
		for ( int i=0; i<circsA.length; i++ )
		{
			val =  circTester & bitmask;
			if ( val == 1 )
			{
				if ( !first )
					retStr+=",";
				retStr+=circsA[i];
				
				first = false;
			}
			circTester = circTester >> 1;
		}
		return retStr;
	}

	/**
	 * @return a String representation of ProgSumListItem class data members
	 */
	public String toString()
    {
    	String retStr = "mProgNum = " + mProgNum + " mDays0 = " + getDays0String() + 
    			" mCircuits = " + getCircuitsString() + "  mStartTime = " + mStartTime + 
    			"mDuration" + mDuration + "mRepeatCount" + mRepeatCount;
    	return retStr;
    }
}
