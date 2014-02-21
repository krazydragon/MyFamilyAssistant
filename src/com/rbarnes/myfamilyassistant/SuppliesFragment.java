/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Feb 19, 2014
 */
package com.rbarnes.myfamilyassistant;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SuppliesFragment extends Fragment{
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_grocery, container, false);
	
	
	CardListView list = (CardListView)view.findViewById(android.R.id.list);

	CardAdapter adapter = new CardAdapter(getActivity())
	    // This sets the color displayed for card titles and header actions by default
	    .setAccentColorRes(android.R.color.holo_red_dark);
	
	

	// Add a basic header and three cards below it
	adapter.add(new CardHeader("Supplies"));
	adapter.add(new Card("Tape", null));
	adapter.add(new Card("Glue", null));
	adapter.add(new Card("Bleach", null));
	adapter.add(new Card("Markers", null));
	adapter.add(new Card("Mop", null));
	

	

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