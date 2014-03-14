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

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rbarnes.myfamilyassistant.MessageFragment.MainCard;
import com.rbarnes.myfamilyassistant.MessageFragment.RemoteDataTask;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlertsFragment extends Fragment {
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> cards;
	CardListView listView;
	CardArrayAdapter adapter; 

	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_kids, container, false);
	
	_context = getActivity();
	cards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.choir_list);


	
	
	new RemoteDataTask().execute();
	
	
	return view;
	}
	
	
	void changeButtonFont(TextView v){
		Typeface t=Typeface.createFromAsset(getActivity().getAssets(),
			"primer.ttf");
		v.setTypeface(t);
	}
	void changeEditTextFont(TextView v){
		Typeface t=Typeface.createFromAsset(getActivity().getAssets(),
            "primer.ttf");
		v.setTypeface(t);
	}
	void changeTextViewFont(TextView v){
		Typeface t=Typeface.createFromAsset(getActivity().getAssets(),
            "primer.ttf");
		v.setTypeface(t);
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
                   "Messages");
           query.orderByDescending("_created_at");
           try {
               ob = query.find();
           } catch (ParseException e) {
               Log.e("Error", e.getMessage());
               e.printStackTrace();
           }
           return null;
       }

       @SuppressWarnings({ })
		@Override
       protected void onPostExecute(Void result) {
       	
       	
       	
           
       	
       	
       	for (ParseObject item : ob) {
           	
       		//Create a Card
               
       		MainCard card = new MainCard(getActivity());
               //Create a CardHeader
       		CardHeader header = new CardHeader(getActivity());
       		header.setTitle("From "+(String) item.get("from"));
            card.setTitle((String) item.get("message"));
            //Add Header to card
            Date date = item.getCreatedAt();
            	card.setSecondaryTitle("Sent on " +(String) android.text.format.DateFormat.format("EEEE MMMM d yyyy hh:mm a", date));
               //Add Header to card
               card.addCardHeader(header);
             //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
	              
		        card.setBackgroundResourceId(R.drawable.card_background);
		        card.setObj(item);  

		        //Set resource
		        
		        card.setSwipeable(false);
		        card.setClickable(false);
		        //Add thumbnail to a card
		        if(!(Boolean) item.get("read")){
		        	   
		        	cards.add(card);
		           }
               
           }
       	

       	

       	
       	
       	adapter = new CardArrayAdapter(getActivity(),cards);
       	if (listView!=null){
       		listView.setAdapter(adapter);
           }
       	mProgressDialog.dismiss();
       	
       }
   }
   
   public class MainCard extends Card {

       protected TextView mTitle;
       protected TextView mSecondaryTitle;
       protected ImageView mImageView;
       protected CheckBox mCheckbox;
       protected ParseObject mObj;
       protected int resourceIdThumbnail;
       protected int count;
       protected ParseObject obj;
       protected String title;
       protected String secondaryTitle;
       protected float image;
       


       public MainCard(Context context) {
           this(context, R.layout.custom_card);
       }

       public MainCard(Context context, int innerLayout) {
           super(context, innerLayout);
           init();
       }

       private void init() {
    	   CardThumbnail cardThumbnail = new CardThumbnail(mContext);
           cardThumbnail.setDrawableResource(R.drawable.calendar);
           addCardThumbnail(cardThumbnail);

    	   setOnSwipeListener(new Card.OnSwipeListener() {
               @Override
               public void onSwipe(Card card) {
                   obj.deleteInBackground();
               }
           });

               //Add ClickListener
               setOnClickListener(new OnCardClickListener() {
                   @Override
                   public void onClick(Card card, View view) {
                       
                   	
                   }
               });



           
           
       }

       @Override
       public void setupInnerViewElements(ViewGroup parent, View view) {
           //Retrieve elements
           mTitle = (TextView) view.findViewById(R.id.inner_title);
           mSecondaryTitle = (TextView) parent.findViewById(R.id.inner_title2);
           mImageView = (ImageView) parent.findViewById(R.id.imageView1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  );
           mCheckbox = (CheckBox)parent.findViewById(R.id.checkBox1);
           
           changeEditTextFont(mTitle);
           changeEditTextFont(mSecondaryTitle);
           
               mTitle.setText(title);

          
               mSecondaryTitle.setText(secondaryTitle);

           
           
           count = 0;

       }


       @Override
		public String getTitle() {
           return title;
       }

       @Override
		public void setTitle(String title) {
           this.title = title;
       }

       public String getSecondaryTitle() {
           return secondaryTitle;
       }

       public void setSecondaryTitle(String secondaryTitle) {
           this.secondaryTitle = secondaryTitle;
       }

       public ParseObject getObj() {
           return obj;
       }

       public void setObj(ParseObject obj) {
           this.obj = obj;
       }

       public int getResourceIdThumbnail() {
           return resourceIdThumbnail;
       }

       public void setResourceIdThumbnail(int resourceIdThumbnail) {
           this.resourceIdThumbnail = resourceIdThumbnail;
       }
   }

}
