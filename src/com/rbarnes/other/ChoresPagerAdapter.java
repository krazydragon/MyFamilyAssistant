/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.other;

import com.rbarnes.myfamilyassistant.CalendarFragment;
import com.rbarnes.myfamilyassistant.ChoreFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class ChoresPagerAdapter extends FragmentStatePagerAdapter {
	 
    private final int PAGES = 2;
    private String[] titles={"To Do", "Completed"};
    @SuppressWarnings("unused")
	private Context context = null;
    
    public ChoresPagerAdapter(Context ctxt, FragmentManager fm) {
        super(fm);
        this.context = ctxt;
    }
 
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChoreFragment().newInstance(position);
            case 1:
                return new ChoreFragment().newInstance(position);
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