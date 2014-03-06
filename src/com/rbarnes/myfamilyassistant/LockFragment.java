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


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LockFragment extends Fragment{
	
	RelativeLayout view;
	boolean isUnlocked = true;
	Button lockButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	view = (RelativeLayout) inflater.inflate(R.layout.fragment_lock, container, false);
	
	TextView tv = (TextView) view.findViewById(R.id.lockView);
	lockButton = (Button)view.findViewById(R.id.lockButton);
	
	
	
	
	
	Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
	tv.setTypeface(custom_font);
	lockButton.setTypeface(custom_font);
	
	lockButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub

    		ImageView lockImage = (ImageView)view.findViewById(R.id.lockImage);
    		
    		if(isUnlocked){
    			lockImage.setBackgroundResource(R.drawable.lock);
    			lockButton.setText("Unlock");
    			isUnlocked = false;
    		}else{
    			lockImage.setBackgroundResource(R.drawable.unlock);
    			lockButton.setText("Lock");
    			isUnlocked = true;
    		}

    	}
    	}
    );
	return view;
	
	
	
	
	}


}
