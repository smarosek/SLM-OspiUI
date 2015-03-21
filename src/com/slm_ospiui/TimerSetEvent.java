package com.slm_ospiui;

import java.util.EventListener;
import java.util.EventObject;

import android.view.View;

public class TimerSetEvent extends EventObject 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 496265981434124715L;

	public TimerSetEvent ( View v, String s )
	{
		super( v );
		
	}
}


	
interface TimerSetListener extends EventListener
{
	void OnTimerSet( TimerSetEvent e );
}
