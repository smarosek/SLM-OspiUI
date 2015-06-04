package com.slm_ospiui;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * 
 * @author Susan Marosek
 * ProgSumListAdapter - Custom list adapter class for sprinkler program 
 * schedule description for displaying a summary of the program
 * schedule list items.
 */

/*
 * Modification History:
 * 	
 * 	04/29/15 Created.
 * 			
 */
public class ProgSumListAdapter extends BaseAdapter
{
	private static final String LOGTAG = "ProgSumListAdapter";
    
    private static final boolean DEBUG = true;
    private static final boolean DEBUG_EVENT_BUS = false;
    
    private Context          mParentContext;
    private List<ListItem>   list;
    private LayoutInflater   layoutInflater;
    
    /**
     * ProgSumListAdapter Class Constructor
     * @param context
     */
    ProgSumListAdapter(Context context) 
    {
        this.mParentContext = context;
    }

	@Override
	public int getCount() 
	{
		return ((list == null) ? 0 : list.size());
	}

	@Override
	public Object getItem(int position) 
	{
	//  In theory should not be called if getCount() returned 0;
        return list.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	/* 
     * getView		
     * Desc: 	Get a View that displays the data at the specified position 
     * 			in the data set.
     * Parameters:
     * 		position 	- position of item within the adapter's data set 
     * 					(the list) of item whose view we want.
     * 		convertView - The old view to reuse, if possible. 
     * 		parent 		- parent that this view will eventually be attached to.
     * 
     * Returns: A View corresponding to data at the specified position.
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (list == null) {
            //  In theory it should not happen but handle this in some graceful way.
            //  Returning null will not produce graceful results.
            return null;
        }
        // You can find this ViewHolder idiom described in detail in this talk:
        //      http://www.youtube.com/watch?v=N6YdwzAvwOA&feature=related
        
        /*
         * When you are not using Holder so getView() method will call 
         * findViewById() as many times as you row(s) will be out of View. 
         * So if you have 1000 rows in List and 990 rows will be out of 
         * View then 990 times will be called findViewById() again.
         * Holder design pattern is used for View caching - Holder 
         * (arbitrary) object holds child widgets of each row and when row 
         * is out of View then findViewById() won't be called but View will 
         * be recycled and widgets will be obtained from Holder.
         */
        ViewHolder holder = null;
        
        if (convertView == null) 
        {
            convertView = (LinearLayout) getLayoutInflator().inflate(
            				R.layout.program_summary_list_item, parent, false);
            
            holder = new ViewHolder();

//NEED?            holder.cirNumTV = (TextView) convertView.findViewById(R.id.circuit_num_tv);
//            holder.cirNameTV = (TextView) convertView.findViewById(R.id.circuit_name_tv);
            

            holder.mDeleteButton  = (Button) convertView.findViewById(R.id.delete_button);
            //WAS0311 holder.v.setTag(position);
            holder.mDeleteButton.setTag(holder);
           
            // Handle Delete Button click
            holder.mDeleteButton.setOnClickListener(new Button.OnClickListener() 
            {                	
                @Override
                public void onClick (final View v) 
                {
                	// Get viewHolder that we saved as tag for the Button
                	ViewHolder mH = (ViewHolder)v.getTag();
	            
            		if ( DEBUG )
            		{
            			Log.d(LOGTAG, "   Button at mH.position "+
            					mH.mPosition+" was clicked ");
            		}
            		
            	
    				
                }	// end onClick()
            });  	// end holder.mDeleteButton.setOnClickListener(new Button.OnClickListener()  
            
            
            
  
            convertView.setTag(holder); 


            
        }	// end convertView == null
        else 
        {
        	// At this point, holder is still pointing to the view / ListItem  
        	// that got scrolled off. Need to replace this info with the info
        	// that was saved in the convertView "Tag" in above section, i.e. 
        	// convertView == null the first time this item was displayed.
        	holder = (ViewHolder) convertView.getTag();
   	
        }
        
        
        /*********************************************************************/
        /*********************************************************************/
        /*********************/
        
        
        Log.d(LOGTAG, "Exitting getView");   
        return convertView;
    }
    
//	}






	/*********************/
	
	private LayoutInflater getLayoutInflator() 
	{
	    if (layoutInflater == null) {
	        layoutInflater = (LayoutInflater)
	            this.mParentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }      
	    return layoutInflater;
	}



	// ViewHolder should contain data that is static for each given row in list,
	// per Google talk on ListViews.
	static class ViewHolder 
	{
	//	ListItem 		mListItem;	// Not sure correct per above comment
	    int 			mPosition;	// Not sure correct per above comment
	    							// 	But saw example on Android Developers
	    							//	that showed position in the ViewHolder. 
	    							//	Unfortunately it didn't show it's usage.
	
	    Button			mDeleteButton;

	}
}
