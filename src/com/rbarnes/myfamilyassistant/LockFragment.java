package com.rbarnes.myfamilyassistant;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LockFragment extends Fragment{
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_lock, container, false);
	
	TextView tv = (TextView) view.findViewById(R.id.lockView);
	Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
	tv.setTypeface(custom_font);
	
	return view;
	
	
	
	
	}


}
