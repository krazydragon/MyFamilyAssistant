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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class LocationFragment extends Fragment {
	
	View view;
	GoogleMap map;
	private PopupMenu popupMenu;
	private String _kid;
	private ProgressDialog mProgressDialog;
	private Context _context;
	private List<ParseObject> ob;
	private LatLng _cords;
	private String _famName;
	private String _user;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	 
	_context = getActivity();
	_kid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("child_list_0", "");
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	
	
	if (view != null) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    try {
    	view = inflater.inflate(R.layout.fragment_location, container, false);
    	TextView tv = (TextView) view.findViewById(R.id.locationTitle);
    	Button button = (Button)view.findViewById(R.id.locationButton);
        // Getting Map for the SupportMapFragment
        SupportMapFragment mf = (SupportMapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);
        map = mf.getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        
        
        
        

        
        
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
    	tv.setTypeface(custom_font);
    	button.setTypeface(custom_font);
    	
    	button.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		JSONObject data = new JSONObject();
        		
        		try {
        			data.put("action", "com.rbarnes.UPDATE_STATUS");
        			data.put("name", _user);
        			data.put("goal", "getLocation");
        			data.put("family", _famName);
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		
        		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        		query.whereEqualTo("parent", false);
        		query.whereEqualTo("family", _famName);
        		query.whereEqualTo("name", _kid);
        		
        		
        		ParsePush push = new ParsePush();
        		push.setQuery(query);
        		push.setData(data);
        		push.sendInBackground();
        		
        		findLocation();
        		
        	}
        	}
        );
    } catch (InflateException e) {
    }

   
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
	private void findLocation(){
		ParseQuery<ParseObject> query = ParseQuery.getQuery("kidLocation");
		
		query.whereEqualTo("name", _kid);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> kidLoc, ParseException e) {
		    	
		    	
		    	if(kidLoc.isEmpty()){
		    		
		    	}else{
		    		for (ParseObject item : kidLoc) {
		    			
		    			ParseGeoPoint point = item.getParseGeoPoint("kid_loc");
		    			LatLng ll = new LatLng(point.getLatitude(), point.getLongitude());
		    			
		    			
		    			map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			            map.addMarker(new MarkerOptions()
			                    .position(ll)
			                    .title(item.getString("name")))
			                    .setSnippet("Last updated 03/13/2014  5:30pm");
			            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));
			    	
			            
		                
				           
		                
		            }
		    		}
		    }
		});
	}

	@Override
	public void onDestroyView() {
		SupportMapFragment f = (SupportMapFragment) getFragmentManager()
	            .findFragmentById(R.id.map);
	    if (f != null) {
	        try {
	            getFragmentManager().beginTransaction().remove(f).commit();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    if (map != null) {
	        
	        
	    }
	    map = null;
	    super.onDestroyView();

	}
	
	// RemoteDataTask AsyncTask
    public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
        
        
        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Country" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "KidName");
            query.orderByDescending("_created_at");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
 
        @SuppressWarnings({ })
		@Override
        protected void onPostExecute(Void result) {
        	
        	
        	
            
        	popupMenu = new PopupMenu(_context, view.findViewById(R.id.locationTitle));
        	
        	
        	popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		 
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    _kid = item.getTitle().toString();
                    findLocation();
                    return true;
                }
            });
        	
        	int i = 1;
        	for (ParseObject item : ob) {
            	
        		popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, (String) item.get("name"));
                i++;
                
		           
                
            }
        	

        	
        	mProgressDialog.dismiss();
        	popupMenu.show();
        }
    }
}
