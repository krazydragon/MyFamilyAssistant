/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.myfamilyassistant.parent;




import com.rbarnes.myfamilyassistant.R;
import com.rbarnes.myfamilyassistant.R.id;
import com.rbarnes.myfamilyassistant.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SettingsFragment extends Fragment implements OnClickListener{
	
	private SettingsListener listener;
	private Button _editUserButton;
	private Button _famPassButton;
	private Button _feedbackButton;
	private Button _famLogoutButton;
	
	
	public interface SettingsListener{
		public void onSettingsButtonPress(int opt);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_settings, container, false);
	
	
	
	_editUserButton = (Button)view.findViewById(R.id.editUserButton);
	_famPassButton = (Button)view.findViewById(R.id.famPassButton);
	_feedbackButton = (Button)view.findViewById(R.id.feedbackButton);
	_famLogoutButton = (Button)view.findViewById(R.id.famLogoutButton);
	
	
	
	_editUserButton.setOnClickListener(this);
	_famPassButton.setOnClickListener(this);
	_feedbackButton.setOnClickListener(this);
	_famLogoutButton.setOnClickListener(this);
	
	
	view.setFocusableInTouchMode(true);
	view.requestFocus();
	final Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
	    @Override
	    public void run() {
	    	view.setOnKeyListener(new View.OnKeyListener() {
		        @Override
		        public boolean onKey(View v, int keyCode, KeyEvent event) {
		         
		            if( keyCode == KeyEvent.KEYCODE_BACK ) {
		                    
		                    getActivity().getSupportFragmentManager().popBackStack();
		                return true;
		            } else {
		            	
		                return false;
		            }
		            
		        }
		    });
	    }
	}, 3000);
	
	return view;
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			listener = (SettingsListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "did not Implemnt Call Listener!");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == _editUserButton){
			listener.onSettingsButtonPress(0);
		}else if(v == _famPassButton){
			listener.onSettingsButtonPress(1);
		}else if(v == _feedbackButton){
			listener.onSettingsButtonPress(2);
		}else if(v == _famLogoutButton){
			listener.onSettingsButtonPress(3);
		}
	}
	
}
