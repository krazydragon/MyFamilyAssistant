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


import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.rbarnes.other.FamDeviceAdminReceiver;
import com.rbarnes.other.MainChildPagerAdapter;
import com.rbarnes.other.MainParentPagerAdapter;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ChildMainFragment extends Fragment{
	/** Called when the activity is first created. */ 

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.child_main_activity, container, false);

	 ViewPager pager=(ViewPager)view.findViewById(R.id.pager);
	 
	    pager.setAdapter(buildAdapter()); 
	 // Bind the widget to the adapter
	 		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
	 		tabs.setViewPager(pager);
	 		final View touchView = view.findViewById(R.id.pager);
	 	    touchView.setOnTouchListener(new View.OnTouchListener() {
	 	        @Override
	 	        public boolean onTouch(View v, MotionEvent event) {

	 	            return true;
	 	        }
	 	    });
	 	    
	 		return view;
	}
	
	private PagerAdapter buildAdapter() {
	    return(new MainChildPagerAdapter(getActivity(), getChildFragmentManager()));
	  }
	
	
}
