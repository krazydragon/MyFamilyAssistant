package com.rbarnes.myfamilyassistant;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class KidsFragment extends Fragment{
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_choirs, container, false);
	CardListView list = (CardListView)view.findViewById(android.R.id.list);

	CardAdapter adapter = new CardAdapter(getActivity())
	    // This sets the color displayed for card titles and header actions by default
	    .setAccentColorRes(android.R.color.holo_green_dark);
	
	

	// Add a basic header and three cards below it
	adapter.add(new CardHeader("Kids Information"));
	adapter.add(new Card("Tom left school", null));
	adapter.add(new Card("Tom has 3 new contacts", null));
	adapter.add(new Card("Tom is at home", null));
	adapter.add(new Card("Tom's phone was unlocked", null));
	

	list.setAdapter(adapter);
	
	list.setOnCardClickListener(new CardListView.CardClickListener() {
	    @Override
	    public void onCardClick(int index, CardBase card, View view) {
	        // Do what you want here
	    }
	});
	return view;
	
	
	
	
	}


}