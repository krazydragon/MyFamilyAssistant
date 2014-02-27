package com.rbarnes.myfamilyassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.afollestad.cardsui.*;

public class MessageFragment extends Fragment{
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_messages, container, false);
	CardListView list = (CardListView)view.findViewById(android.R.id.list);

	CardAdapter adapter = new CardAdapter(getActivity())
	    // This sets the color displayed for card titles and header actions by default
	    .setAccentColorRes(android.R.color.holo_orange_light);
	
	

	// Add a basic header and three cards below it
	adapter.add(new CardHeader("Messages"));
	adapter.add(new Card("Mom", "I'm going to be working late today."));
	adapter.add(new Card("Me", "Ok I will make dinner tonight."));
	adapter.add(new Card("Dad", "Or I can pick something up?"));
	adapter.add(new Card("Me", "Sounds even better"));
	
	
	

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
