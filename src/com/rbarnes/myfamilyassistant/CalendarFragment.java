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
import it.gmariotti.cardslib.library.view.CardListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarFragment extends Fragment{
	 
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> todayCards;
	ArrayList<Card> pastCards;
	ArrayList<Card> futureCards;
	CardListView listView;
	PopupWindow pw; 
	CardArrayAdapter todayAdapter; 
	CardArrayAdapter pastAdapter;
	CardArrayAdapter futureAdapter;
	Date today;
	Calendar c;
	private int page;
	
	
	 // newInstance constructor for creating fragment with arguments
    public static CalendarFragment newInstance(int page) {
    	CalendarFragment calendarFrag = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", page);
        calendarFrag.setArguments(args);
        return calendarFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("pageNum", 0);
    }
	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_calendar, container, false);
	
	
	
	_context = getActivity();
	todayCards = new ArrayList<Card>();
	pastCards = new ArrayList<Card>();
	futureCards = new ArrayList<Card>();
	
	listView = (CardListView) view.findViewById(R.id.listView);
	
	
	
	
	TextView titleText = (TextView)view.findViewById(R.id.title);
	     
	new RemoteDataTask().execute();

	titleText.setText("Calendar");
	changeTextViewFont(titleText);
	
	
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
	public void addPopUp(){
		
		
		
		
		//We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) CalendarFragment.this
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View layout = inflater.inflate(R.layout.fragment_main_add,
                (ViewGroup) getActivity().findViewById(R.id.PopUpAddLayout));
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/2;
        int height = size.y/2;
        pw = new PopupWindow(layout, width, height, true);
        // display the popup in the center
       
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);//Intent loginIntent = new Intent(this, LoginActivity.class);
        TextView addTitleText = (TextView)layout.findViewById(R.id.addTitle);
        Button submit = (Button)layout.findViewById(R.id.addButton);
        Button cancel =(Button)layout.findViewById(R.id.cancelButton);
        final EditText popupInput = (EditText) layout.findViewById(R.id.addInput);

        
        
        changeEditTextFont(popupInput);
		changeButtonFont(submit);
		changeButtonFont(cancel);
		changeTextViewFont(addTitleText);
        
		addTitleText.setText("Add Events");
		popupInput.setHint("Enter supply needed");
		
        submit.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		
        		if(popupInput.getText().toString().trim().length() > 0){

        			// TODO Auto-generated method stub
            		String s = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", "error");
            		
        			ParseObject obj = new ParseObject("Calendar");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(s, true);
        			postACL.setRoleReadAccess(s, true);
        			//obj.setACL(postACL);
        			
        			//obj.put("item", popupInput.getText().toString());
        			
        			
        			//obj.saveEventually();
            			
            			
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       card.setTitle(popupInput.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
        	              
        		        
        		        card.setObj(obj);   
        		       
        		       
        		        card.setBackgroundResourceId(R.drawable.card_background);
        		           
        		        
        		        card.setSwipeable(true);
        		        card.setClickable(true);
        		        //Add thumbnail to a card
        		        
        		        todayCards.add(card);
        			todayAdapter.notifyDataSetChanged();
            		pw.dismiss();

   				}

   				else {

   					Toast.makeText(getActivity(),"Input can not be blank!", Toast.LENGTH_SHORT).show();
   					
   				}
        		
        		
        		
        	}
        	}
       );

        cancel.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub

        		pw.dismiss();
 
        	}
        	}
        );
        
        
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
                
              //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
		        card.setObj(item);   
		        card.setBackgroundResourceId(R.drawable.card_background);
		        Date date = item.getDate("date");
        		
    		       

    		    SimpleDateFormat simpleDate =  new SimpleDateFormat("hh:mm a");
    		    header.setTitle((String) android.text.format.DateFormat.format("EEEE MMMM d yyyy", date));
    		  //Add Header to card
                card.addCardHeader(header);
    		    String dateString = "at "+simpleDate.format(date);
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
		        
    		    if (dateSpecified.before(today)) {
    		    	 pastCards.add(card);
    		       
    		    } else if (dateSpecified.equals(today)) {
    		    	 todayCards.add(card);
    		        
    		    } 
    		             else if (dateSpecified.after(today)) {
    		            	 futureCards.add(card);
    		            	 
    		        
    		    }
                
            }
        	

        	

        	
        	pastAdapter = new CardArrayAdapter(getActivity(),pastCards);
        	todayAdapter = new CardArrayAdapter(getActivity(),todayCards);
        	futureAdapter = new CardArrayAdapter(getActivity(),futureCards);
        	if (page == 2){
        		listView.setAdapter(pastAdapter);
            }
        	
        	if (page == 0){
        		listView.setAdapter(todayAdapter);
            }
        	
        	if (page == 1){
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
