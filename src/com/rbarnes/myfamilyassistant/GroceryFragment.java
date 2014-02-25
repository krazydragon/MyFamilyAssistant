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

import java.util.List;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroceryFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	CardListView _list;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_grocery, container, false);
	
	 _list = (CardListView)view.findViewById(android.R.id.list);
	
	
	new RemoteDataTask().execute();

	
	
	return view;
	}
	// RemoteDataTask AsyncTask
    public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
        
        
        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Country" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Groceries");
            query.orderByDescending("_created_at");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
 
        @SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
        protected void onPostExecute(Void result) {
        	
		    
            
            

        	CardAdapter adapter = new CardAdapter(getActivity())
        	    // This sets the color displayed for card titles and header actions by default
        	    .setAccentColorRes(android.R.color.holo_blue_bright);
        	adapter.add(new CardHeader("Groceries"));
        	for (ParseObject item : ob) {
            	adapter.add(new Card((String) item.get("item"), null));
            }
        	

        	

        	_list.setAdapter(adapter);
        	mProgressDialog.dismiss();
        	_list.setOnCardClickListener(new CardListView.CardClickListener() {
        	    @Override
        	    public void onCardClick(int index, CardBase card, View view) {
        	        // Do what you want here
        	    }
        	});
        }
    }
}
