package com.rbarnes.myfamilyassistant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TempFragment extends Fragment{
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.blank, container, false);
	
	
	Button myButton = (Button)view.findViewById(R.id.button1);
	myButton.setOnClickListener(new OnClickListener() {
	  public void onClick(View view) {
		  FragmentManager manager = getFragmentManager();
		    FragmentTransaction transaction = manager.beginTransaction();
		    transaction.replace(R.id.content_frame, new ParentMainFragment());
		    transaction.commit();

	  }
	});
	
	Button myButton2 = (Button)view.findViewById(R.id.button2);
	myButton2.setOnClickListener(new OnClickListener() {
	  public void onClick(View view) {
		  FragmentManager manager = getFragmentManager();
		    FragmentTransaction transaction = manager.beginTransaction();
		    transaction.replace(R.id.content_frame, new ChildMainFragment());
		    transaction.commit();

	  }
	});
	return view;
	
	
	
	
	}


}
