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


import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;
import com.afollestad.cardsui.CardBase;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;



public class AlertsFragment extends Fragment{
	
	

	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_main_list, container, false);
	CardListView list = (CardListView)view.findViewById(android.R.id.list);

	CardAdapter adapter = new CardAdapter(getActivity())
	    // This sets the color displayed for card titles and header actions by default
	    .setAccentColorRes(android.R.color.holo_green_dark);
	
	

	// Add a basic header and three cards below it
	adapter.add(new CardHeader("Alerts"));
	adapter.add(new Card("Message from Dad", "I'm going to the stroe can you update the list"));
	adapter.add(new Card("Message from Tom", "Ok I will"));
	
	

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
