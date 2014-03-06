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
import android.preference.PreferenceManager;
import android.app.AlertDialog;
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
import com.rbarnes.other.SendParseService;



public class MainActivity extends FragmentActivity {

	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout mainView;
    private String _pass;
    private String _passName;
    private String _famName;
    private Context _context;
    private Boolean _fam_auth;
    String _tempString;
    ParseUser _currentUser;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_main);
		mainView = (LinearLayout)this.findViewById(R.id.mainFrag);
		  
	    
	    _context = this;
		Parse.initialize(this, "wAoWswK6kE9xpSqkrHrKjrIbWDMfeF0xYGWkDWFc", "2wZeexj6posiXETwFUbQ0LJFkT62wg63wnaS711L");
		
		
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("mode", "parent");
		installation.put("parent", true);
		installation.put("family", "lazy");
		installation.saveInBackground();
		
		JSONObject data = new JSONObject();
		
		try {
			data.put("action", "com.rbarnes.UPDATE_STATUS");
			data.put("name", "kid1");
			data.put("goal", "lock");
			data.put("alert", "I work!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
		query.whereEqualTo("parent", true);
		query.whereEqualTo("family", "krazy");
		
		
		ParsePush push = new ParsePush();
		push.setQuery(query);
		push.setData(data);
		//push.sendInBackground();
		_fam_auth = false;
		
		 _currentUser = ParseUser.getCurrentUser();
		 
		 
		_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
		
		if ((_famName != null) && _currentUser!= null) {
			//Crouton.makeText(this, "Welcome Back "+ currentUser.getUsername() + "!", Style.INFO).show();
			android.support.v4.app.FragmentManager anager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ransaction = anager.beginTransaction();
            ransaction.replace(R.id.main_frame, new ParentMainFragment());
            ransaction.commit();
			
			//new RemoteDataTask().execute();
			
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
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_frame, frag);
            transaction.commit();
 
        }
    }
	private void checkAccess(){
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
	    builder.setTitle("Enter Family Password");

	    // Set up the input
	    final EditText input = new EditText(_context);
	    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
	    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    builder.setView(input);

	    // Set up the buttons
	    builder.setPositiveButton("Parent", new DialogInterface.OnClickListener() { 
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            _pass = input.getText().toString();
	            _passName = "password";
	            verFamCheck();
	        }
	    });
	    builder.setNegativeButton("Kid", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	
	        	_passName = "kidPass";
	        	_pass = input.getText().toString();
	        	verFamCheck();
	        }
	    });

	    builder.show();
	}
	
	private void verFamCheck(){
		
		final ParseACL roleACL = new ParseACL();
		
		
		roleACL.setRoleReadAccess(_famName, true);
		roleACL.setRoleWriteAccess(_famName, true);
		ParseACL.setDefaultACL(roleACL, true);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
		
		query.whereEqualTo(_passName, _pass);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> famPass, ParseException e) {
		    	ParseRole role = new ParseRole(_famName, roleACL);
		    	
		    	role.getUsers().add(ParseUser.getCurrentUser());
		    	role.saveEventually();
		    	
			    		Log.d("YES", "It worked!!!");
			    		android.support.v4.app.FragmentManager anager = getSupportFragmentManager();
			            android.support.v4.app.FragmentTransaction ransaction = anager.beginTransaction();
			            ransaction.replace(R.id.main_frame, new ParentMainFragment());
			            ransaction.commit();
			            
			            
			    		ParseAnalytics.trackAppOpened(getIntent());
			    
		    			    	
		    	
		    	
		    	
		    }
		});
	}
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    	
	   
	}
	private void pleaseWait(){
		if(_fam_auth){
	    	
	    	Editor editor = PreferenceManager.getDefaultSharedPreferences(_context).edit();
	        
	        editor.putBoolean("fam_auth", false);
	        
	        editor.commit();
	        
	        
	            	_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
	        	    
	        	    
	        	    checkAccess();
	            
	    }
		
	}
	
	public void myButtonMethod(View v) {
		
		Toast.makeText(_context,"ImageButton is working!", Toast.LENGTH_SHORT).show();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		_fam_auth = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("fam_auth", _fam_auth);
	    if(_fam_auth){
	    	if (requestCode == 1) {

			     if(resultCode == RESULT_OK){      
			           
			         //String msg = data.getStringExtra("msg"); 
			         //Crouton.makeText(this, msg, Style.INFO).show();
			        
			         _famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
			         
			        pleaseWait();
			         
			 		
			     }else if (resultCode == RESULT_CANCELED) {    
			         
			    	Intent loginIntent = new Intent(this, LoginActivity.class);
					startActivityForResult(loginIntent,1);		
			     }
			  }
	    }
		  
		}
	
}
