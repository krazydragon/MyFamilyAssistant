package com.rbarnes.myfamilyassistant.other;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;

public class SendParseService extends IntentService {

	LocationManager locationManager;
	private String _famName;
	private String _user;
	private String _name;
	private DevicePolicyManager _devicePolicyManager;
	private String _tempString;
	private ParseObject _kidInfo;
	private ParseObject _location;
	private static Boolean _updated;
	
	public SendParseService() {
		super("SendParseService");
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		_devicePolicyManager= (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		_famName = PreferenceManager.getDefaultSharedPreferences(this).getString("fam_name", _famName);
		_user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", _user);
		
		String goal = (String) intent.getExtras().get("goal");
		_name = (String) intent.getExtras().get("name");
		if(_name==null)
			_name="";
		 
		if(goal.equals("lock")){
			lockDevice();
		}else if(goal.equals("getLocation")){
			sendLocation();
		}else if(goal.equals("getKidInfo")){
			sendKidInfo();
		}else if(goal.equals("unlock")){
			unlockDevice();
		}else if(goal.equals("noti_parent")){
			notiParent();
		}
		
	}
	
	private void notiParent(){
		if(_updated==null)
			_updated = false;
		_updated = true;
	  	
	}

	public Boolean isUpdated(){
		if(_updated==null)
			_updated = false;
		Boolean tempBool = _updated;
		_updated = false;
		
		return tempBool;
		
	}
	private void sendNotiBack(){
		JSONObject data = new JSONObject();
		
		
		
		try {
			data.put("action", "com.rbarnes.UPDATE_STATUS");
			data.put("goal", "noti_parent");
			data.put("alert", _tempString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
		query.whereEqualTo("parent", true);
		query.whereEqualTo("family", _famName);
		query.whereEqualTo("name", _name);
		
		
		ParsePush push = new ParsePush();
		push.setQuery(query);
		push.setData(data);
		push.sendInBackground();
	}
	private void lockDevice(){
		
	  	_devicePolicyManager.lockNow();
	  	_devicePolicyManager.resetPassword("password", 1);
	  	_tempString = _user + "'s phone is now locked";
	  	sendNotiBack();
	}
	
	private void unlockDevice(){
		_devicePolicyManager.lockNow();
	  	_devicePolicyManager.resetPassword("", 1);
	  	_tempString = _user + "'s phone is now unlocked";
	  	sendNotiBack();
	}
	
	
	private void sendKidInfo(){
		final JSONObject kidInfoObj = new JSONObject();
		
		// TODO Auto-generated method stub
		try {
			kidInfoObj.put("appList", getAppList());
			kidInfoObj.put("callLog", getCallLog());
			kidInfoObj.put("contacts", getContacts());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("kidContent");
		
		query.whereEqualTo("name", _user);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> info, ParseException e) {
		    	
				
		    	if(info.isEmpty()){
		    		_kidInfo =  new ParseObject("kidContent");
		    		ParseACL postACL = new ParseACL();
		    		postACL.setRoleWriteAccess(_famName, true);
		    		postACL.setRoleReadAccess(_famName, true);
		    		_kidInfo.setACL(postACL);
		    	}else{
		    		for (ParseObject item : info){
		    			_kidInfo =  item;
		    			
		    		}
		    	}
		    	_kidInfo.put("content", kidInfoObj);
				_kidInfo.put("name", _user);
				_kidInfo.saveInBackground();
		    }
		});
		
		
	}
	private void sendLocation(){
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("kidLocation");
		
		query.whereEqualTo("name", _user);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> info, ParseException e) {
		    	Location l = getLastBestLocation();
				
		    	if(info.isEmpty()){
		    		_location = new ParseObject("kidLocation");
		    		ParseACL postACL = new ParseACL();
		    		postACL.setRoleWriteAccess(_famName, true);
		    		postACL.setRoleReadAccess(_famName, true);
		    		_location.setACL(postACL);
		    	}else{
		    		for (ParseObject item : info){
		    			_location =  item;
		    			
		    		}
		    		
		    	}
		    	ParseGeoPoint gp = new ParseGeoPoint(l.getLatitude(),l.getLongitude());
	    		_location.put("kid_loc", gp);
	    		_location.put("name", _user);
	    		_location.saveInBackground();
	    		_tempString = _user + "'s location was updated";
	    		sendNotiBack();
		    }
		});
		
		
		
		
		
		
		
		
	}
	private Location getLastBestLocation() {
	    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }

	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	        return locationGPS;
	    }
	    else{
	        return locationNet;
	    }

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
