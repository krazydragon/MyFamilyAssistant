package com.rbarnes.myfamilyassistant;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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

        // Getting Map for the SupportMapFragment
        MapFragment mf = (MapFragment) this.getFragmentManager()
                .findFragmentById(R.id.map);
        GoogleMap map = mf.getMap();
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        

        LatLng pnts[] = new LatLng[4];

        pnts[0] = new LatLng(0, 0);
        pnts[1] = new LatLng(10, 0);
        pnts[2] = new LatLng(10, 10);
        pnts[3] = new LatLng(0, 10);

        PolygonOptions poly = new PolygonOptions().add(pnts)
                .fillColor(Color.argb(100, 255, 50, 50))
                .strokeColor(Color.GRAY).strokeWidth(0.5f);
        map.addPolygon(poly);
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
