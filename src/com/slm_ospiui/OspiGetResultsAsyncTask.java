package com.slm_ospiui;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

//OrigEx public class OspiGetResultsAsyncTask extends AsyncTask<String, Void, Document> {
public class OspiGetResultsAsyncTask extends AsyncTask<String, Void, String> 
{
	public static final int OSPI_GET = 1;
	public static final int OSPI_EXEC = 2;
	
	private static final String LOGTAG = "OspiGetResultsAsyncTask";
	private static final String LOGTAGBG = "OspiGetResultsAsyncTask:doInBackground";
	

    Context mContext;
    int mCommandType;
    ProgressDialog pdialog;
    OspiResultsListener ospiResultsListener; 
    
    public OspiGetResultsAsyncTask(Context context, int ct ) 
    {
        mContext = context;
        mCommandType = ct;
        
        Log.d(LOGTAG, "mCommandType = "+mCommandType);
        ospiResultsListener = (OspiResultsListener)context;
    }

    @Override
    protected void onPreExecute() 
    {
        super.onPreExecute();
        pdialog = new ProgressDialog(mContext);
       // pD.setMessage(getResources().getText(R.string.str_loading_data));
       // pD.setIndeterminate(true);
       // pD.setCancelable(true);
        pdialog.setTitle(R.string.str_retreiving_data);
        pdialog.setCancelable(false);
        pdialog.show();
    }

    @Override
    protected String doInBackground(String... data) 
    {

    	String result = "";
        try 
        {
        	
        	if ( mCommandType == OSPI_GET )
        	{
        		Log.d(LOGTAGBG, "sending get command");
        		Document doc = Jsoup.connect(data[0]).get();
        		Log.d(LOGTAGBG, "doc = "+doc.toString());
           
        		result = doc.body().text();
        	}
        	else
        	{
        		Log.d(LOGTAGBG, "sending execute command");
        		//Jsoup.connect(data[0]).ignoreHttpErrors(true).execute();
        		Jsoup.connect(data[0]).execute();
        		result = "OK";
        	}
        	Log.d(LOGTAGBG, "result = "+ result );

            //OrigEx return doc;
           // return result;
        } 
        catch (HttpStatusException e) 
        {
        	Log.d(LOGTAGBG, "HttpStatusException e = "+ e );
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = Integer.toString(e.getStatusCode());
        }
        catch (IOException e) 
        {
        	Log.d(LOGTAGBG, "IOException e = "+ e );
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /* END TEMP TEMP */
    	
    	
    	
    	
        /*WAS
         *     catch (NullPointerException e1) 
	    {
	    //    Log.d("InputStream", e.getLocalizedMessage());
	    	Log.d("InputStream ", "NullPointerException" );
	    }
	    catch (Exception e) 
	    {
	    //    Log.d("InputStream", e.getLocalizedMessage());
	    	Log.d("InputStream ", e.toString() );
	    }

         */

        return result;
    }

    @Override
    protected void onPostExecute(String result) 
    {
    	Log.d(LOGTAG, "Got to onPostExectue " );
    	
        pdialog.cancel();
        if (result != null && ospiResultsListener != null ) 
        {
              ospiResultsListener.onOspiResults(result);
        }
        else
            Toast.makeText(mContext, 
            		"NULL ASYNC OspiGetResultsAsyncTask", 
            		Toast.LENGTH_LONG).show();
    }
}
