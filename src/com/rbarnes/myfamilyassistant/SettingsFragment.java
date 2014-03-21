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




import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
	private Button _kidInfoButton;
	private Button _feedbackButton;
	private Button _aboutButton;
	private Button _famLogoutButton;
	
	public interface SettingsListener{
		public void onSettingsButtonPress(int opt);
		
		
	}
	FragmentManager _manager;
    FragmentTransaction _transaction;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_settings, container, false);
	_manager = getChildFragmentManager();
	
	_editUserButton = (Button)view.findViewById(R.id.editUserButton);
	_famPassButton = (Button)view.findViewById(R.id.famPassButton);
	_kidInfoButton = (Button)view.findViewById(R.id.kidInfoButton);
	_feedbackButton = (Button)view.findViewById(R.id.feedbackButton);
	_aboutButton = (Button)view.findViewById(R.id.aboutButton);
	_famLogoutButton = (Button)view.findViewById(R.id.famLogoutButton);
	
	
	
	_editUserButton.setOnClickListener(this);
	_famPassButton.setOnClickListener(this);
	_kidInfoButton.setOnClickListener(this);
	_feedbackButton.setOnClickListener(this);
	_aboutButton.setOnClickListener(this);
	_famLogoutButton.setOnClickListener(this);
	
	
	
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
		}
	}
	
}
