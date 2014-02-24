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

public class CalendarFragment extends Fragment{
	 
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_calendar, container, false);
	CardListView list = (CardListView)view.findViewById(android.R.id.list);

	CardAdapter adapter = new CardAdapter(getActivity())
	    // This sets the color displayed for card titles and header actions by default
	    .setAccentColorRes(android.R.color.holo_green_light);
	
	

	// Add a basic header and three cards below it
	adapter.add(new CardHeader("Calendar"));
	adapter.add(new Card("March 21 2014", "Soccer game"));
	adapter.add(new Card("June 11 2014", "Jason's birthday"));
	adapter.add(new Card("September 17 2014", "Back to school"));

	

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
