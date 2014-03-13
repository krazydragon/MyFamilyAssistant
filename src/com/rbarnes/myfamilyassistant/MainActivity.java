/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.myfamilyassistant;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.PushService;
import com.rbarnes.other.FamDeviceAdminReceiver;
import com.rbarnes.other.SendParseService;



public class MainActivity extends FragmentActivity {

	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout mainView;
    private Context _context;
    private Boolean _fam_auth = false;
    private Boolean _parent = false;
    String _tempString;
    ParseUser _currentUser;
    FragmentManager _manager;
    FragmentTransaction _transaction;
    ComponentName _deviceAdmin;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_main);
		mainView = (LinearLayout)this.findViewById(R.id.mainFrag);
		  
		
	    _context = this;
	    
		Parse.initialize(this, "wAoWswK6kE9xpSqkrHrKjrIbWDMfeF0xYGWkDWFc", "2wZeexj6posiXETwFUbQ0LJFkT62wg63wnaS711L");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		_manager = getSupportFragmentManager();
		

		 _currentUser = ParseUser.getCurrentUser();
		 
		 
		
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
		_parent = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("parent", _parent);
		if ((_fam_auth) && _currentUser!= null) {
			checkParent();
			
			
           
			
		} else {
			//ParseUser.logOut();
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent,1);
			
			
		}
		
		
		
		// get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.items);
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
 
        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
 
        // App Icon 
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
 
        actionBarDrawerToggle = new ActionBarDrawerToggle(
        		this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.famassist_ic_navigation_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );
 
        // Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true); 
 
        // just styling option add shadow the right edge of the drawer
    //drawerLayout.setDrawerShadow(R.drawable.ic_launcher, GravityCompat.START);
 
    drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    _tempString = "1234567890";
    //Intent intent = new Intent(this, SendParseService.class);
    // add infos for the service which file to download and where to store
    
    //startService(intent);
	}

	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        actionBarDrawerToggle.onConfigurationChanged(newConfig);
	    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
         // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
        	LinearLayout mainfrag = (LinearLayout)findViewById(R.id.mainFrag);
        	mainfrag.setVisibility(View.INVISIBLE);
            return true;
             
        }else{
        	ParseUser.logOut();
        	finish();
        }
        return super.onOptionsItemSelected(item);
    }

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
            drawerLayout.closeDrawer(drawerListView);
            
			
            Fragment frag = null;
            switch (position) {
            case 0:
                frag = new CalendarMainFragment();
                break;

            case 1:
                frag = new ChoresMainFragment();
                break;

            case 2:
                frag = new MessageFragment();
                break;

            case 3:
                frag = new GroceryFragment();
                break;
            case 4:
                frag = new SuppliesFragment();
                break;

            case 5:
                frag = new LocationFragment();
                break;
            case 6:
                frag = new LockFragment();
                break;

            case 7:
                frag = new ChildInfoMainFragment();
                break;
            case 8:
                frag = new SettingsFragment();
                break;

            
            default:
                frag = new MessageFragment();
                break;
            }
            mainView.setVisibility(View.INVISIBLE);
            _transaction = _manager.beginTransaction();
            _transaction.replace(R.id.content_frame, frag);
            _transaction.commit();
 
        }
    }
	private void checkParent(){
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		 _deviceAdmin = new ComponentName(_context, FamDeviceAdminReceiver.class);
        _transaction = _manager.beginTransaction();
        
        	Log.i("Parent",""+ _parent);
       if(_parent){ 
       _transaction.replace(R.id.main_frame, new ParentMainFragment());
       }else{
    	   _transaction.replace(R.id.main_frame, new ChildMainFragment());
    	   if(!devicePolicyManager.isAdminActive(_deviceAdmin)){
	 	    	 installAdmin();
	 	    }
    	   
       }
       _transaction.commit();
	}
	
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    
	   
	}
	
	
	public void installAdmin() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
	   	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, _deviceAdmin);
	   	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Your Parents told you to do this");
	   	startActivityForResult(intent, 3);
		
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
		final Handler handler = new Handler();
		if(_fam_auth){
	    	if (requestCode == 1) {

			     if(resultCode == RESULT_OK){      
			           
			         //String msg = data.getStringExtra("msg"); 
			         //Crouton.makeText(this, msg, Style.INFO).show();
			        
			    	 _parent = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("parent", _parent);
			         
			    	 
			    	 handler.postDelayed(new Runnable() {
			    	     @Override
			    	     public void run() {
			    	    	 checkParent();
			    	     }
			    	 }, 1000);
			          
			         
			 		
			     }else if (resultCode == RESULT_CANCELED) {    
			         
			    	Intent loginIntent = new Intent(this, LoginActivity.class);
					startActivityForResult(loginIntent,1);		
			     }
			  }else if(requestCode == 3){
			    	if (resultCode == Activity.RESULT_OK) {
						Log.i("admin", "Administration enabled!");
						
					} else {
						Log.i("admin", "Administration enable FAILED!");
						handler.postDelayed(new Runnable() {
				    	     @Override
				    	     public void run() {
				    	    	 //installAdmin();
				    	     }
				    	 }, 1000);
					}
			    }
	    }
	    super.onActivityResult(requestCode, resultCode, data);
		}
	
}
