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

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.rbarnes.myfamilyassistant.SettingsFragment.SettingsListener;
import com.rbarnes.other.FamDeviceAdminReceiver;
import com.suredigit.inappfeedback.FeedbackDialog;



public class MainActivity extends FragmentActivity implements SettingsListener{

	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Context _context;
    private Boolean _fam_auth = false;
    private Boolean _parent = false;
    private String _famName = "";
    String _tempString;
    ParseUser _currentUser;
    FragmentManager _manager;
    FragmentTransaction _transaction;
    ComponentName _deviceAdmin;
    Editor _editor;
    private FeedbackDialog feedBack;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_main);
		
		  
		feedBack = new FeedbackDialog(this, "AF-886F6AB586F4-68");
	    _context = this;
	    
		Parse.initialize(this, "wAoWswK6kE9xpSqkrHrKjrIbWDMfeF0xYGWkDWFc", "2wZeexj6posiXETwFUbQ0LJFkT62wg63wnaS711L");
		PushService.setDefaultPushCallback(this, MainActivity.class);
		_manager = getSupportFragmentManager();
		
		_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
		_parent = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("parent", _parent);
		_currentUser = ParseUser.getCurrentUser();
		 getChildNames();
		 
		
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
		
		if ((_fam_auth) && _currentUser!= null) {
			checkParent();
			
			
           
			
		} else {
			//ParseUser.logOut();
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent,1);
			
			
		}
		
		
		
		// get list items from strings.xml
		drawerListViewItems = getResources().getStringArray(R.array.items);
		if(!_parent){
			String temp[] = new String[5];
			for (int i = 0; i < 5; i++)
			{
			    temp[i] = drawerListViewItems[i];
			}
			
			drawerListViewItems = temp;
		}
        
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
    
	}
	
	private void getChildNames(){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		_editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
		int size = PreferenceManager.getDefaultSharedPreferences(_context).getInt("child_list_size",0);
		
		for (int i = 0; i < size; i++) {
            _editor.remove("child_list_"+i);
        }
		
		_editor.commit();
		
		query.whereEqualTo("parent", false);
		query.whereEqualTo("famName", _famName);
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> objects, ParseException e) {
		    if (e == null) {
		    	
		    	int tempNum = 0;
	        	for (ParseUser user : objects) {
	            	
	        		_editor.putString("child_list_"+tempNum,(String) user.getString("firstName"));
	        		
	        		tempNum++;
	            }
	        	_editor.putInt("child_list_size", objects.size());
	        	_editor.commit();   
		    } else {
		    	
		    }
		  }
		});
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
		MenuItem addItem = menu.findItem(R.id.menu_add);
		MenuItem childItem = menu.findItem(R.id.menu_child);
		addItem.setVisible(false);
		childItem.setVisible(false);
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
        	
            return true;
             
        }else if(item.getItemId()== R.id.action_settings){
        	ParseUser.logOut();
        	_editor.putBoolean("fam_auth", false);
        	_editor.commit();
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
            if(frag != null){
            	_transaction = _manager.beginTransaction();
            	_transaction.replace(R.id.content_frame, frag, null);
            	_transaction.addToBackStack(null);
            	_transaction.commit();
            }
        }
    }
	private void checkParent(){
		_parent = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("parent", _parent);
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		 _deviceAdmin = new ComponentName(_context, FamDeviceAdminReceiver.class);
        _transaction = _manager.beginTransaction();
        
        	Log.i("Parent",""+ _parent);
       if(_parent){ 
       _transaction.replace(R.id.content_frame, new ParentMainFragment());
       }else{
    	   _transaction.replace(R.id.content_frame, new ChildMainFragment());
    	   if(!devicePolicyManager.isAdminActive(_deviceAdmin)){
	 	    	 installAdmin();
	 	    }
    	   
       }
       _transaction.addToBackStack(null);
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
		Log.d("code",""+requestCode);
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
		final Handler handler = new Handler();
		if(_fam_auth){
	    	if (requestCode == 1) {

			     if(resultCode == RESULT_OK){      
			           
			         //String msg = data.getStringExtra("msg"); 
			         //Crouton.makeText(this, msg, Style.INFO).show();
			        
			    	 _currentUser = ParseUser.getCurrentUser();
			         
			    	 
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
			  else if(requestCode == 75){
				  
				  if(ParseUser.getCurrentUser() == null){
					  finish();
				  }
				  
			  }
	    }
	    super.onActivityResult(requestCode, resultCode, data);
		}

	
	
	 @Override
	  public void onBackPressed() {
	    moveTaskToBack(true);
	  }
		@Override
		public void onSettingsButtonPress(int opt) {
			
			switch (opt) {
	        case 0:
	        	_transaction = _manager.beginTransaction();
	            _transaction.replace(R.id.content_frame, new EditUserFragment());
	            _transaction.addToBackStack(null);
	            _transaction.commit();
	            break;

	        case 1:
	        	_transaction = _manager.beginTransaction();
	            _transaction.replace(R.id.content_frame, new EditFamPassFragment());
	            _transaction.addToBackStack(null);
	            _transaction.commit();
	            break;

	        case 2:
	        	feedBack.show();
	            break;

	        
	        case 3:
	            _currentUser.put("famName", "");
	            _currentUser.saveEventually();
	            ParseUser.logOut();
	            _editor.putBoolean("fam_auth", false);
	            _editor.commit();
	            finish();
	            break;

	        
	        }
			// TODO Auto-generated method stub
			
		}
		
		@Override
		protected void onPause() {
		    super.onPause();
		    feedBack.dismiss();
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {

		    View v = getCurrentFocus();
		    boolean ret = super.dispatchTouchEvent(event);

		    if (v instanceof EditText) {
		        View w = getCurrentFocus();
		        int scrcoords[] = new int[2];
		        w.getLocationOnScreen(scrcoords);
		        float x = event.getRawX() + w.getLeft() - scrcoords[0];
		        float y = event.getRawY() + w.getTop() - scrcoords[1];

		        Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
		        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

		            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		        }
		    }
		return ret;
		}
}
