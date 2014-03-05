package com.rbarnes.other;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseObject;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

public class SendParseService extends IntentService {

	public SendParseService() {
		super("SendParseService");
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		JSONObject kidInfoObj = new JSONObject();
		// TODO Auto-generated method stub
		try {
			kidInfoObj.put("appList", getAppList());
			kidInfoObj.put("callLog", getCallLog());
			kidInfoObj.put("contacts", getContacts());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("json", kidInfoObj.toString());
		ParseObject kidInfo = new ParseObject("kidContent");
		kidInfo.put("content", kidInfoObj);
		
		kidInfo.saveInBackground();
	}
	
	private JSONArray getAppList(){
		
		JSONArray jsonArray = new JSONArray();
    	final PackageManager pm =  getPackageManager();
		//get a list of installed apps.
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {
			
			if((packageInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
		        
		    //it's a system app, not interested
		    } else if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		        //Discard this one
		    //in this case, it should be a user-installed app
		    } else {
		    	JSONObject appObj = new JSONObject();
		    	String appFile = packageInfo.sourceDir;
		    	long installed = new File(appFile).lastModified();
		    	String whenInstalled = String.valueOf(installed);
		    	try {
					appObj.put("name", packageInfo.loadLabel(pm).toString());
					appObj.put("package_name", packageInfo.packageName);
			    	appObj.put("date_installed", whenInstalled);
			    	jsonArray.put(appObj);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	 
			    
		    }
		    
		}
		
		
		return jsonArray;
	}
	
	@SuppressWarnings("unused")
	private JSONArray getCallLog() throws JSONException{
		
		JSONArray jsonArray = new JSONArray();
		
   	 Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
   	            null, null, null, CallLog.Calls.DATE + " DESC");
   	 	int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
   	    int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
   	    int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
   	    int date = cursor.getColumnIndex(CallLog.Calls.DATE);
   	    int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);       
   	    while (cursor.moveToNext()) {
   	    	JSONObject callLogObj = new JSONObject();
   	    	String phName = cursor.getString(name);
   	        String phNumber = cursor.getString(number);
   	        String callType = cursor.getString(type);
   	        String callDate = cursor.getString(date);
   	        String callDuration = cursor.getString(duration);
   	        String dir = null;
   	        int dircode = Integer.parseInt(callType);
   	        switch (dircode) {
   	        case CallLog.Calls.OUTGOING_TYPE:
   	            dir = "OUTGOING";
   	            break;
   	        case CallLog.Calls.INCOMING_TYPE:
   	            dir = "INCOMING";
   	            break;

   	        case CallLog.Calls.MISSED_TYPE:
   	            dir = "MISSED";
   	            break;
   	        }
   	        if(phName != null){
   	        callLogObj.put("name", phName);
   	        }else if(phName == null){
   	        	callLogObj.put("name", "");
   	        }
   	        callLogObj.put("date", callDate);
   	        callLogObj.put("number", phNumber);
   	        callLogObj.put("type", dir);
   	        callLogObj.put("duration", callDuration);
   	     jsonArray.put(callLogObj);
   	    }
   	    cursor.close();
   	    

   	    
   	    
   	 return jsonArray;
   	    
   	    
	}
	
	private JSONArray getContacts(){
		
		JSONArray jsonArray = new JSONArray();
		Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
	    try {
	    	
	        int conName = cursor.getColumnIndex(Phone.DISPLAY_NAME);
	        int conNumber = cursor.getColumnIndex(Phone.NUMBER);
	        int conContacted = cursor.getColumnIndex(Phone.LAST_TIME_CONTACTED);
	        cursor.moveToFirst();
	        do {
	        	JSONObject contactObj = new JSONObject();
	            String contactName = cursor.getString(conName);
	            String contactNumber = cursor.getString(conNumber);
	            String contactContacted = cursor.getString(conContacted);
	            
	            contactObj.put("name", contactName);
	            contactObj.put("number", contactNumber);
	            contactObj.put("contacted", contactContacted);
	            jsonArray.put(contactObj);
	        } while (cursor.moveToNext());  
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (cursor != null) {
	            cursor.close();
	        }
	    }
		
	    
		
		return jsonArray;
	}

}
