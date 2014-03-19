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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class SettingsFragment extends Fragment{
	
	
	FragmentManager _manager;
    FragmentTransaction _transaction;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_settings, container, false);
	_manager = getChildFragmentManager();
	
	Button editUserButton = (Button)view.findViewById(R.id.editUserButton);
	editUserButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub

    		_transaction = _manager.beginTransaction();
            _transaction.replace(R.id.content_frame, new EditUserFragment());
            _transaction.commit();

    	}
    	}
    );
	
	
	return view;
	
	
	
	
	}


	
}
