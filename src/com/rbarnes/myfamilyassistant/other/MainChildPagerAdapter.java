/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.myfamilyassistant.other;

import com.rbarnes.myfamilyassistant.parent.AlertsFragment;
import com.rbarnes.myfamilyassistant.parent.UpcomingFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class MainChildPagerAdapter extends FragmentStatePagerAdapter {
	 
    private final int PAGES = 2;
    private String[] titles={"Upcoming", "Alerts",};
    @SuppressWarnings("unused")
	private Context context = null;
    
    public MainChildPagerAdapter(Context ctxt, FragmentManager fm) {
        super(fm);
        this.context = ctxt;
    }
 
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UpcomingFragment();
            case 1:
                return new AlertsFragment();
            
            default:
                throw new IllegalArgumentException("The item position should be less or equal to:" + PAGES);
        }
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	return titles[position];
    }
 
    @Override
    public int getCount() {
        return PAGES;
    }
    
   
}