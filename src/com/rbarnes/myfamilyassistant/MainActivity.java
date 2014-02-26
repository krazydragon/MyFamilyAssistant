/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Feb 17, 2014
 */
package com.rbarnes.myfamilyassistant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.cardsui.Card;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.ParseException;



public class MainActivity extends FragmentActivity {

	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout mainView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainView = (LinearLayout)this.findViewById(R.id.mainFrag);
		FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();
	    
		Parse.initialize(this, "wAoWswK6kE9xpSqkrHrKjrIbWDMfeF0xYGWkDWFc", "2wZeexj6posiXETwFUbQ0LJFkT62wg63wnaS711L");
		
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			//Crouton.makeText(this, "Welcome Back "+ currentUser.getUsername() + "!", Style.INFO).show();
			android.support.v4.app.FragmentManager anager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction ransaction = anager.beginTransaction();
            ransaction.replace(R.id.main_frame, new ParentMainFragment());
            ransaction.commit();
			
			//new RemoteDataTask().execute();
			
		} else {
			
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
    
    String look =null;
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
        	
            return true;
             
        }
        return super.onOptionsItemSelected(item);
    }

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
            drawerLayout.closeDrawer(drawerListView);
            
			
            Fragment frag = null;
            switch (position) {
            case 0:
                frag = new CalendarFragment();
                break;

            case 1:
                frag = new ChoirFragment();
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
                frag = new KidsFragment();
                break;
            case 8:
                frag = new SettingsFragment();
                break;

            
            default:
                frag = new MessageFragment();
                break;
            }
            mainView.setVisibility(View.INVISIBLE);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_frame, frag);
            transaction.commit();
 
        }
    }
	
}
