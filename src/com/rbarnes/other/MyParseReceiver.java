package com.rbarnes.other;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyParseReceiver extends BroadcastReceiver {

	 @Override
	  public void onReceive(Context context, Intent intent) {
	    String TAG = "Fam";
	    
	    
	    
	    /*
		try {
	      String action = intent.getAction();
	      
	      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
	      
	      
	      if(json.getString("goal")=="lock"){
	        	
		  }else if(json.getString("goal")=="getInfo"){
		    	 
		    	 
		    	    	
		    	    	
		    			
		    			
		    			
		    			
		     }
	      
	      
		
	      
	        
	     
	      
	    } catch (JSONException e) {
	      Log.d(TAG, "JSONException: " + e.getMessage());
	    }*/
	  }
	}
/*ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
query.whereEqualTo("parent", true);
query.whereEqualTo("family", "krazy");
ParsePush.sendMessageInBackground("Nuclear launch detected!", query);*/