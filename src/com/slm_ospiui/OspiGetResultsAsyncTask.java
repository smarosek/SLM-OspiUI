package com.slm_ospiui;

import java.io.IOException;

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
	
	//private static final String LOGTAG = "OspiGetResultsAsyncTask";
	private static final String LOGTAGBG = "OspiGetResultsAsyncTask:doInBackground";

    Context mContext;
    ProgressDialog pdialog;
    OspiResultsListener ospiResultsListener; 
    
    public OspiGetResultsAsyncTask(Context context) 
    {
        mContext = context;
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
        pdialog.setTitle(R.string.str_loading_data);
        pdialog.setCancelable(false);
        pdialog.show();
    }

    @Override
    protected String doInBackground(String... data) 
    {
    	String result = "";
        try 
        {
            Document doc = Jsoup.connect(data[0]).get();
            Log.d(LOGTAGBG, "doc = "+doc.toString());
	        
	        result = doc.body().text();
	        Log.d(LOGTAGBG, "result = "+ result );

            //OrigEx return doc;
            return result;
        } 
        catch (IOException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

        return null;
    }

    @Override
    protected void onPostExecute(String result) 
    {
        pdialog.cancel();
        if (result != null) 
        {
              ospiResultsListener.onResults(result);
        }
        else
            Toast.makeText(mContext, "NULL ASYNC", Toast.LENGTH_LONG).show();
    }
}
