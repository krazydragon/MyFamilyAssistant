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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	 
    private final int PAGES = 3;
    private String[] titles={"Upcoming", "Alerts", "Kids"};
    @SuppressWarnings("unused")
	private Context context = null;
    
    public ViewPagerAdapter(Context ctxt, FragmentManager fm) {
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
            case 2:
                return new KidsFragment();
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