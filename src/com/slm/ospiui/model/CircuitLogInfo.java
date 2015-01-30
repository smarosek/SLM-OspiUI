package com.slm.ospiui.model;

//import java.io.Serializable;

public class CircuitLogInfo 
//implements Serializable
{
	/**
	 * 
	 */
//	private static final long serialVersionUID = -7598439235202263571L;

//	int mPosition;
	
	String mDate;
	String mCNum;
	String mCName;
	String mRunTime;
	

	public CircuitLogInfo( String date, String cnum, String cname, String runTime )
	{
		mDate = date;
		mCNum = cnum;
		mCName = cname;
		mRunTime = runTime;
	}
/*	
	public int getPosition() {
		return mPosition;
	}
	public void setPosition(int position) {
		this.mPosition = position;
	}
*/
	public String getDate() {
		return mDate;
	}
	public void setDate(String date) {
		this.mDate = date;
	}

	public String getCNum() {
		return mCNum;
	}
	public void setCNum(String cNum) {
		this.mCNum = cNum;
	}

	public String getCName() {
		return mCName;
	}
	public void setCName(String cName) {
		this.mCName = cName;
	}

	public String getRunTime() {
		return mRunTime;
	}
	public void setRunTime(String runTime) {
		this.mRunTime = runTime;
	}
	

    public String toString()
    {
    	String retStr = "mDate = " + mDate + " mCNum = " + mCNum+ 
    			" mCName = " + mCName + "  mRunTime = "+ mRunTime;
    	return retStr;
    }

}
