package com.slm_ospiui;


public class ListItem 
{
	String 	cirNum;
	int		cirNumber;
    String 	cirName;
   
    
	int 	cirType; 			// 1=> MASTER; 0=> REGULAR
    int 	toggleBtnState;
    long 	startTime;
    long 	stopTime;
    long 	displayTime;
    int 	position;
    
    public ListItem ( String cirNum, String cirName, int cirType, int position )
    {
    	this.cirNum = cirNum;
    	this.cirName = cirName;
    	this.cirType = cirType;
    	this.position = position;
    	this.toggleBtnState = 0;  //OFF
    	this.startTime = 0;
    	this.displayTime = 0;
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
    	this.displayTime = 0;
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
    public long getDisplayTime ()
    {
    	return this.displayTime;
    }
    public void setDisplayTime ( long displayTime )
    {
    	this.displayTime = displayTime;
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
    			" cirType = " + cirType + "  toggleBtnState = "+ (((toggleBtnState==0) ? "OFF" : "ON")+ 
    					"  startTime = "+startTime+"  stopTime = "+stopTime);
    	return retStr;
    }
}
