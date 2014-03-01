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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationFragment extends Fragment{
	
	View view;
	GoogleMap map;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	 
	
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
        MapFragment mf = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);
        map = mf.getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        
        
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.addMarker(new MarkerOptions()
                .position(new LatLng(28.561, -81.359))
                .title("John"))
                .setSnippet("Last updated 2/22/2014  2:30pm");

        
        
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
    	tv.setTypeface(custom_font);
    	button.setTypeface(custom_font);
    	
    	button.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(28.561, -81.359), 16));
        		
        	}
        	}
        );
    } catch (InflateException e) {
    }

   
	
	
	return view;
	
	
	
	
	}

	@Override
	public void onDestroyView() {
	    MapFragment f = (MapFragment) getFragmentManager()
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
}
