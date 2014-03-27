package com.rbarnes.myfamilyassistant.other;

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
	    
	    Log.d("INTENT",intent.toString());
	    Log.d("INTENT",intent.getAction());
	    if(intent.getAction().equals("com.rbarnes.UPDATE_STATUS")){
	    
		try {
		Intent i= new Intent(context, SendParseService.class);
	      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
	     
	      Log.d("GOAL",""+ json.getString("goal"));
	      if(json.getString("goal").equals("lock")){
	    	  
	    	  i.putExtra("name", json.getString("name"));
	    	  i.putExtra("goal", "lock");
	    	  context.startService(i); 
		  }else if(json.getString("goal").equals("getLocation")){
		
			  i.putExtra("name", json.getString("name"));
	    	  i.putExtra("goal", "getLocation");		
	    	  context.startService(i); 		
		     }else if(json.getString("goal").equals("unlock")){
		    	 
		    	  i.putExtra("name", json.getString("name"));
		    	  i.putExtra("goal", "unlock");
		    	  context.startService(i); 
			  }else if(json.getString("goal").equals("noti_parent")){
			    	 
			    	  i.putExtra("goal", "noti_parent");
			    	  context.startService(i); 
				  }
	      
	      
	    } catch (JSONException e) {
	      Log.d(TAG, "JSONException: " + e.getMessage());
	    }
	  
	    }
	  }
	}
/*ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
query.whereEqualTo("parent", true);
query.whereEqualTo("family", "krazy");
ParsePush.sendMessageInBackground("Nuclear launch detected!", query);*/