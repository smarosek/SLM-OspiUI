package com.slm_ospiui;

import android.os.CountDownTimer;
//import android.widget.TextView;


public class SCountDownTimer extends CountDownTimer 
{  
	STimerView mTimerView;
	
    public SCountDownTimer(long millisInFuture, long countDownInterval, STimerView tView ) 
    {  
         super(millisInFuture, countDownInterval);  
         mTimerView = tView;
    }  
    
    @Override  
    public void onFinish() 
    {  
    	mTimerView.setText("Completed.");  
    }  
     
    @Override  
    public void onTick(long millisUntilFinished) 
    {  
          long millis = millisUntilFinished;  
  //         String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),  
  //                 TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),  
  //                 TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));  
  //             System.out.println(hms);  
          mTimerView.SetTime(millis); 
  //      }  
   }  

}
