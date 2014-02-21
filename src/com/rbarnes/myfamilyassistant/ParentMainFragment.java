/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Feb 19, 2014
 */
package com.rbarnes.myfamilyassistant;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


public class ParentMainFragment extends Fragment implements TabListener{
	
	private String[] drawerListViewItems;
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    
	/** Called when the activity is first created. */ 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.parent_main_activity, container, false);

	
	Intent intent = new Intent(getActivity(), AlertsFragment.class);
	TabHost tabs=(TabHost)view.findViewById(R.id.tabhost); 
	tabs.setup(); 
	TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
	spec.setContent(R.id.tab1); 
	spec.setIndicator("Analog Clock"); 
	tabs.addTab(spec); 
	spec=tabs.newTabSpec("tag2"); 
	spec.setContent(intent); 
	spec.setIndicator("Digital Clock"); 
	tabs.addTab(spec); 
	tabs.setCurrentTab(0); 
	
	ActionBar bar = getActivity().getActionBar();
	
	bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
	 
	
	    for (int i=1; i <= 3; i++) {
	
	        Tab tab = bar.newTab();
	
	        tab.setText("Tab " + i);
	
	        tab.setTabListener(this);
	
	        bar.addTab(tab);
	
	 
	
	    }

	    
    

    
	return view;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.i("TABPOSITION", ""+tab.getPosition());
		Fragment frag = null;
		
        switch (tab.getPosition()) {
        case 0:
            frag = new AlertsFragment();
            break;

        case 1:
            frag = new UpcomingFragment();
            break;

        case 2:
            frag = new KidsFragment();
            break;
        }
        ft.replace(android.R.id.content, frag);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        actionBarDrawerToggle.onConfigurationChanged(newConfig);
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
             frag = new GroceryFragment();
             break;

         case 3:
             frag = new ParentMainFragment();
             break;
         case 4:
             frag = new CalendarFragment();
             break;

         case 5:
             frag = new ChoirFragment();
             break;

         
         default:
             frag = new ChildMainFragment();
             break;
         }
         FragmentManager manager = getFragmentManager();
         FragmentTransaction transaction = manager.beginTransaction();
         transaction.replace(R.id.content_frame, frag);
         transaction.commit();

     }
 }
}
