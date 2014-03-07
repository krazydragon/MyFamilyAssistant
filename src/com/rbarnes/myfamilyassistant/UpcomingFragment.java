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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rbarnes.myfamilyassistant.CalendarFragment.MainCard;
import com.rbarnes.myfamilyassistant.CalendarFragment.RemoteDataTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class UpcomingFragment extends Fragment {
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> futureCards;
	CardListView listView;
	PopupWindow pw; 
	CardArrayAdapter futureAdapter;
	Date today;
	Calendar c;
	private int page;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_kids, container, false);
		
		
		
		_context = getActivity();
		futureCards = new ArrayList<Card>();
		
		listView = (CardListView) view.findViewById(R.id.list);
		
		
		
  
		new RemoteDataTask().execute();


		
		c = Calendar.getInstance();
		 // set the calendar to start of today
	   c.set(Calendar.HOUR_OF_DAY, 0);
	   c.set(Calendar.MINUTE, 0);
	   c.set(Calendar.SECOND, 0);
	   c.set(Calendar.MILLISECOND, 0);

	   today = c.getTime();
	   
	   
	   today = c.getTime();
	   
	   ViewPager vp=(ViewPager) getActivity().findViewById(R.id.pager);
	   
	   Log.i("pager","%d" + vp.getCurrentItem() );
	   
		
		return view;
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
                    "Calendar");
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
                card.setTitle((String) item.get("title"));
                //Add Header to card
                card.addCardHeader(header);
              //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
		        card.setObj(item);   
		        card.setBackgroundResourceId(R.drawable.card_background);
		        Date date = item.getDate("date");
        		
    		       

    		    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    		    String dateString = simpleDate.format(date);
    		    card.setSecondaryTitle(dateString);
		        //Set resource
		        thumb.setDrawableResource(R.drawable.ic_launcher);
		        card.setSwipeable(true);
		        card.setClickable(true);
		        //Add thumbnail to a card
		        
		        c.setTime(date);
        		c.set(Calendar.HOUR_OF_DAY, 0);
    		    c.set(Calendar.MINUTE, 0);
    		    c.set(Calendar.SECOND, 0);
    		    c.set(Calendar.MILLISECOND, 0);
    		    // and get that as a Date
    		    Date dateSpecified = c.getTime();
		        
    		     if (dateSpecified.after(today)) {
    		            	 futureCards.add(card);      	 
   
    		    }
                
            }
        	

        	

        	
        	
        	futureAdapter = new CardArrayAdapter(getActivity(),futureCards);
        	
        	if (listView != null){
        		listView.setAdapter(futureAdapter);
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
                        if(mCheckbox.isChecked()){
                        	Toast.makeText(getContext(), "You need to buy more" + title, Toast.LENGTH_SHORT).show();
                            
                            mCheckbox.setChecked(false);
                            card.changeBackgroundResourceId(R.drawable.card_background);
                        }else{
                        	Toast.makeText(getContext(), title + "was purchased", Toast.LENGTH_SHORT).show();
                            
                            mCheckbox.setChecked(true);
                            card.changeBackgroundResourceId(R.drawable.card_background2);
                        }
                    	
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
            
            if (mTitle != null)
                mTitle.setText(title);

            if (mSecondaryTitle != null)
            	mSecondaryTitle.setText(secondaryTitle);

            
            mImageView.setImageResource(R.drawable.calendar);
          

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
