package com.rbarnes.myfamilyassistant;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChoirFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> cards;
	CardListView listView;
	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_choirs, container, false);
	
	
	cards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.list);
     
	     
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
 
        @SuppressWarnings({ })
		@Override
        protected void onPostExecute(Void result) {
        	
		    
        	
            
        	
        	
        	for (ParseObject item : ob) {
            	
        		//Create a Card
                Card card = new MainCard(getActivity());

                //Create a CardHeader
                CardHeader header = new CardHeader(getActivity());
                header.setTitle((String) item.get("item"));
                //Add Header to card
                card.addCardHeader(header);
                
                cards.add(card);
            }
        	

        	

        	
        	
        	CardArrayAdapter adapter = new CardArrayAdapter(getActivity(),cards);
        	if (listView!=null){
        		listView.setAdapter(adapter);
            }
        	mProgressDialog.dismiss();
        	
        }
    }
    
    public class MainCard extends Card {

        protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected RatingBar mRatingBar;
        protected int resourceIdThumbnail;
        protected int count;

        protected String title;
        protected String secondaryTitle;
        protected float rating;


        public MainCard(Context context) {
            this(context, R.layout.custom_card);
        }

        public MainCard(Context context, int innerLayout) {
            super(context, innerLayout);
            //init();
        }

        private void init() {

            //Add thumbnail
            CardThumbnail cardThumbnail = new CardThumbnail(mContext);

            if (resourceIdThumbnail==0)
                cardThumbnail.setDrawableResource(R.drawable.lock);
            else{
                cardThumbnail.setDrawableResource(resourceIdThumbnail);
            }

            addCardThumbnail(cardThumbnail);

            //Only for test, some cards have different clickListeners
            if (count==2){

                setTitle(title + " No Click");
                setClickable(false);

            }else if (count==4){

                setTitle(title + " Partial Click");
                addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW,new OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Toast.makeText(getContext(), "Partial click Listener card=" + title, Toast.LENGTH_SHORT).show();
                    }
                });

            }else{

                //Add ClickListener
                setOnClickListener(new OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        Toast.makeText(getContext(), "Click Listener card=" + title, Toast.LENGTH_SHORT).show();
                    }
                });

            }


            //Swipe
            if (count>1 && count<3){

                setTitle(title + " Swipe enabled");
                setSwipeable(true);
                setOnSwipeListener(new OnSwipeListener() {
                    @Override
                    public void onSwipe(Card card) {
                        Toast.makeText(getContext(), "Removed card=" + title, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            //Retrieve elements
            mTitle = (TextView) view.findViewById(R.id.inner_title);
            mSecondaryTitle = (TextView) parent.findViewById(R.id.inner_title2);
            mRatingBar = (RatingBar) parent.findViewById(R.id.inner_title);

            if (mTitle != null)
                mTitle.setText("Google Maps");

            if (mSecondaryTitle != null)
                mSecondaryTitle.setText("Google Inc.");

            if (mRatingBar != null) {
                mRatingBar.setNumStars(5);
                mRatingBar.setMax(5);
                mRatingBar.setStepSize(0.5f);
                mRatingBar.setRating(4.7f);
            }

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

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public int getResourceIdThumbnail() {
            return resourceIdThumbnail;
        }

        public void setResourceIdThumbnail(int resourceIdThumbnail) {
            this.resourceIdThumbnail = resourceIdThumbnail;
        }
    }

}
