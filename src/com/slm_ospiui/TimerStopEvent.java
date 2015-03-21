package com.slm_ospiui;

import java.util.EventListener;
import java.util.EventObject;

import android.view.View;

public class TimerStopEvent extends EventObject 
{
	private static final long serialVersionUID = -2861015199103618404L;

		public TimerStopEvent ( View v )
		{
			super( v );
			
		}


}
		
interface TimerStopListener extends EventListener
{
	void OnTimeout( TimerStopEvent e );
}


