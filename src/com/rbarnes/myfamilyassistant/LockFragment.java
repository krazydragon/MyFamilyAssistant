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


import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LockFragment extends Fragment{
	
	RelativeLayout view;
	boolean isUnlocked = true;
	Button lockButton;
	private String _famName;
	private String _user;
	private String _kid;
	private Context _context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	view = (RelativeLayout) inflater.inflate(R.layout.fragment_lock, container, false);
	_kid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("child_list_0", "");
	_context = getActivity();
	TextView tv = (TextView) view.findViewById(R.id.lockView);
	lockButton = (Button)view.findViewById(R.id.lockButton);
	
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	
	
	
	Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
	tv.setTypeface(custom_font);
	lockButton.setTypeface(custom_font);
	
	lockButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub

    		ImageView lockImage = (ImageView)view.findViewById(R.id.lockImage);
    		JSONObject data = new JSONObject();
    		
    		if(isUnlocked){
    			lockImage.setBackgroundResource(R.drawable.lock);
    			
    			lockButton.setText("Unlock");
    			isUnlocked = false;
    			
    			
        		
        		try {
        			data.put("action", "com.rbarnes.UPDATE_STATUS");
        			data.put("name", _user);
        			data.put("goal", "lock");
        			data.put("family", _famName);
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		
    		}else{
    			lockImage.setBackgroundResource(R.drawable.unlock);
    			lockButton.setText("Lock");
    			isUnlocked = true;
    			
    			try {
        			data.put("action", "com.rbarnes.UPDATE_STATUS");
        			data.put("name", _user);
        			data.put("goal", "unlock");
        			data.put("family", _famName);
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
    		}


    		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
    		query.whereEqualTo("parent", false);
    		query.whereEqualTo("family", _famName);
    		query.whereEqualTo("name", _kid);
    		
    		
    		ParsePush push = new ParsePush();
    		push.setQuery(query);
    		push.setData(data);
    		push.sendInBackground();
    	}
    	}
    );
	
	setHasOptionsMenu(true);
	return view;
	
	
	
	
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu,inflater);
		MenuItem childItem = menu.findItem(R.id.menu_child);
		childItem.setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	   case R.id.menu_child:
		   
		   View menuItemView = getActivity().findViewById(R.id.menu_child);
		   PopupMenu menu = new PopupMenu(_context, menuItemView);
		   
		   
		   menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      		 
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                   _kid = item.getTitle().toString();
                   Log.d("KIDNAME",_kid);
                   return true;
               }
           });
		   int size = PreferenceManager.getDefaultSharedPreferences(_context).getInt("child_list_size",0);
		   for(int i = 0; i<size;i++){
	            String childName = PreferenceManager.getDefaultSharedPreferences(_context).getString("child_list_"+i, "");
	            menu.getMenu().add(Menu.NONE, 0, Menu.NONE, childName);
	        }
		   
		   menu.show();
		   
	        return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	

}
