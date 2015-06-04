package com.slm_ospiui;

public abstract class OspiMessage 
{
	public String mMessage;		// The string containing the Open Sprinkler message to be sent
	public int mMessageType;	// 
	
	
	abstract public void onOspiEvent();
	

}
