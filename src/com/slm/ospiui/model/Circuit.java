package com.slm.ospiui.model;

/**
 * @author Susan Marosek
 *
 */
public class Circuit {
	
	
	int 	miCircNum;
	String 	msCircName;
	boolean mbCircOn;
	boolean mbIsMaster;
	
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
	
	public boolean isCircOn() {
		return mbCircOn;
	}
	
	public void setCircOn( boolean circOn ) {
		this.mbCircOn = circOn;
	}
	
	
	public boolean isMaster() {
		return mbIsMaster;
	}
	
	public void setIsMaster( boolean master ) {
		this.mbIsMaster = master;
	}

	@Override
	public String toString() {
		return "Circuit [ miCircNum= " + miCircNum + ", msCircName= " + msCircName
				+ ", mbCircOn= " + mbCircOn + ", mbIsMaster= " + mbIsMaster+" ]";
	}
	
}
