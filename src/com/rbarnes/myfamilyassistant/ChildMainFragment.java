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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class ChildMainFragment extends Fragment{
	/** Called when the activity is first created. */ 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.child_main_activity, container, false);

	TabHost tabs=(TabHost)view.findViewById(R.id.tabhost); 
	tabs.setup(); 
	TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
	spec.setContent(R.id.tab1); 
	spec.setIndicator("Analog Clock"); 
	tabs.addTab(spec); 
	spec=tabs.newTabSpec("tag2"); 
	spec.setContent(R.id.digitalClock1); 
	spec.setIndicator("Digital Clock"); 
	tabs.addTab(spec); 
	tabs.setCurrentTab(0); 
	return view;
	}
}
