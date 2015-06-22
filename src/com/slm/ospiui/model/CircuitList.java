package com.slm.ospiui.model;

import java.util.ArrayList;

import android.util.Log;

//import com.slm_ospiui.ListItem;
//import com.slm_ospiui.ManualModeActivity.GetDataTask;

/**
 * CircuitList class - Defined as a Singleton, CircuitList contains info 
 * 		regarding the sprinkler circuits in the system. This list is meant
 * 		to be available to all classes and contain circuit and circuit 
 * 		status.
 * 
 * @author Susan Marosek
 *
 */
/*
 * Modification History:
 * 	
 * 06/11/15 Created. 
 * 			See http://stackoverflow.com/questions/9045233/android-array-list
 */
public class CircuitList 	
{
	
    private static final String LOGTAG = "CircuitList";
    
    private static final boolean DEBUG = true;
    
    private static final int MAX_NUM_CIRCUITS = 8;
    
	
    static CircuitList sCircuitList;	// Singleton CircuitList
    ArrayList<Circuit> maCircuitList;

    private CircuitList() 
    {
        // Build your ArrayList
    	maCircuitList = new ArrayList<Circuit>();
    	BuildCircuitList();
    }

    public synchronized static CircuitList getInstance() 
    {
        if(sCircuitList == null) {
        	sCircuitList = new CircuitList();
        }
        return sCircuitList;
    }

    public ArrayList<Circuit> getCircuitList() 
    {
        return maCircuitList;
    }

    public void setCircuitList(ArrayList<Circuit> circuits)
    {
    	this.maCircuitList = circuits;
    }
    
    public void add ( Circuit c )
    {
    	maCircuitList.add( c );
    }
    
    public Circuit get( int pos )
    {    
    	Circuit c = maCircuitList.get(pos);
    	return c;
    }
    
    
    
	private void BuildCircuitList()
	{
		Log.d(LOGTAG, "IN BuildCircuitList()");
		
	//	maCircuits = new Circuit[MAX_NUM_CIRCUITS];
		
//		gCircuitList = CircuitList.getInstance();
//		gCircuitList.setgCircuitList(100);

		// Build circuit list with default names
		BuildGeneralCircuitList();
/*SLM0606 moved to onCreate		
		listAdapter.setList(list);
		listAdapter.notifyDataSetChanged();	
		*/
	}
	

	/**
	 *  BuildGeneralCircuitList - Build circuit list with default names.
	 */
	private void BuildGeneralCircuitList()
	{
		Log.d(LOGTAG, "IN BuildGeneralCircuitList()");
		
		int j = 0;
		// Temporarily set up circuits
		for ( int i=0; i < MAX_NUM_CIRCUITS; i++ )
		{
			
			j= i+1;
			Circuit c = new Circuit();
			c.setCircNum(j);
			
			// Temporary - set circuit 1 as Master Circuit 
			if ( i == 0 )
			{
				Log.d(LOGTAG, "MASTER");
				
				c.setIsMaster(true);
				c.setCircName("Master Circuit");
			}
			else
			{
				c.setCircName("Circuit"+j);
			}
			c.setCircIsRunning(false);	
		
			maCircuitList.add( c );
			

		
		}	
	}
	
	
	
	
	/**
	 *  BuildGeneralCircuitList - Build circuit list from  
	 *  circuit_description file in assets directory.
	 ///
	private void BuildCircuitListFromFile()
	{
	// Will need to rethink  	new GetDataTask().execute ();
	}
	*/


	public void PrintCircuitList() 
	{
		 int i = 0;
		 
		 for ( Circuit c : maCircuitList )
		 {
			 Log.d(LOGTAG, "maCircuit["+i+"] = "+ c.toString());
			 i++;
		 }
			
	}
	
}
