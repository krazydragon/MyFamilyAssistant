/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.myfamilyassistant.fragments;



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

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rbarnes.myfamilyassistant.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CalendarFragment3 extends Fragment{
	 
	
	private Context _context;
	List<ParseObject> ob;
	public static ArrayList<Card> pastCards;
	CardListView listView;
	PopupWindow pw; 
	static CardArrayAdapter pastAdapter;
	Date today;
	Calendar c;
	private String _famName;
	private String _user;
	private String _tempString;
	private int _userColor;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_calendar, container, false);
	
	
	
	_context = getActivity();
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	_userColor = ParseUser.getCurrentUser().getNumber("userColor").intValue(); 

	if(pastCards == null){
		pastCards = new ArrayList<Card>();
		pastAdapter = new CardArrayAdapter(getActivity(),pastCards);
		
	}
	
	listView = (CardListView) view.findViewById(R.id.listView);
	
	
	
	
	TextView titleText = (TextView)view.findViewById(R.id.title);
	     
	

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
   setHasOptionsMenu(true);
	
   view.setFocusableInTouchMode(true);
	view.requestFocus();
	final Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
	    @Override
	    public void run() {
	    	view.setOnKeyListener(new View.OnKeyListener() {
		        @Override
		        public boolean onKey(View v, int keyCode, KeyEvent event) {
		         
		            if( keyCode == KeyEvent.KEYCODE_BACK ) {
		                    
		                    getActivity().getSupportFragmentManager().popBackStack();
		                return true;
		            } else {
		            	
		                return false;
		            }
		            
		        }
		    });
	    }
	}, 3000);
	listView.setAdapter(pastAdapter);
	return view;
	}
	
	public void addCard(Card card, Context context){
		if(_context == null)
			_context = context;
		if(pastCards == null)
			pastCards = new ArrayList<Card>();
		if(pastAdapter == null)
			pastAdapter = new CardArrayAdapter(_context, pastCards);
		pastCards.add(card);
		pastAdapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu,inflater);
	    MenuItem addItem = menu.findItem(R.id.menu_add);
		addItem.setVisible(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	   case R.id.menu_add:
	        addPopUp();
	        return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	private void sendNoti(){
		JSONObject data = new JSONObject();
		
		
		
		try {
			data.put("alert", _tempString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
		query.whereEqualTo("family", _famName);

		
		
		ParsePush push = new ParsePush();
		push.setQuery(query);
		push.setData(data);
		push.sendInBackground();
	}
	
	public void addPopUp(){
		
		
		
		
		//We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) CalendarFragment3.this
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View layout = inflater.inflate(R.layout.fragment_add_cal,
                (ViewGroup) getActivity().findViewById(R.id.PopUpAddLayout));
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        pw = new PopupWindow(layout, width, height, true);
        // display the popup in the center
       
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);//Intent loginIntent = new Intent(this, LoginActivity.class);
        TextView addTitleText = (TextView)layout.findViewById(R.id.addTitle);
        Button submit = (Button)layout.findViewById(R.id.addButton);
        Button cancel =(Button)layout.findViewById(R.id.cancelButton);
        final EditText popupInput = (EditText) layout.findViewById(R.id.addInput);
        final TimePicker timePicker = (TimePicker) layout.findViewById(R.id.calTimePicker);
        final DatePicker datePicker = (DatePicker) layout.findViewById(R.id.calDatePicker);
        
        
		changeTextViewFont(addTitleText);
        
		addTitleText.setText("Add Events");
		popupInput.setHint("Enter event detail");
		
        submit.setOnClickListener(new Button.OnClickListener(){

        	@SuppressLint("SimpleDateFormat")
			@Override
        	public void onClick(View v) {
        		
        		if(popupInput.getText().toString().trim().length() > 0){

        			// TODO Auto-generated method stub
            		
            		
            		
            		// Get the date from our datepicker
            		int year = datePicker.getYear();
            		int month = datePicker.getMonth();
            		int day = datePicker.getDayOfMonth();
            		int hour = timePicker.getCurrentHour();
            		int minute = timePicker.getCurrentMinute();
        
            		// Create a new calendar set to the date chosen
       
            		Calendar cal = Calendar.getInstance();
            		
            		cal.set(Calendar.YEAR, year);
            		cal.set(Calendar.MONTH, month);
            		cal.set(Calendar.DAY_OF_MONTH, day);
            		cal.set(Calendar.HOUR_OF_DAY, hour);
            		cal.set(Calendar.MINUTE, minute);
            		cal.set(Calendar.SECOND, 0);
            		Date d = cal.getTime();
            		
            		
        			ParseObject obj = new ParseObject("Calendar");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(_famName, true);
        			postACL.setRoleReadAccess(_famName, true);
        			obj.setACL(postACL);
        			
        			obj.put("title", popupInput.getText().toString());
        			obj.put("date", d);
        			obj.put("name", _user);
        			obj.put("color", _userColor);
        			obj.saveEventually();
            			
        			_tempString = _user + " created a new calendar event.";
        			sendNoti();	
               		MainCard card = new MainCard(getActivity());
                    //Create a CardHeader
                    CardHeader header = new CardHeader(getActivity());
                    card.setTitle(popupInput.getText().toString());
                    card.setCardColor(_userColor);
                    header.setTitle((String) android.text.format.DateFormat.format("EEEE MMMM d yyyy", d));
          		  //Add Header to card
                      card.addCardHeader(header);
                  //Create thumbnail
                      SimpleDateFormat simpleDate =  new SimpleDateFormat("hh:mm a");
                      String dateString = "at "+ simpleDate.format(d);
          		    card.setSecondaryTitle(dateString+" created by " + _user);  
     		        
     		        card.setObj(obj);   
     		       
     		       
     		        card.setBackgroundResourceId(R.drawable.card_background);
     		           
     		        
     		        card.setSwipeable(true);
     		        
     		        //Add thumbnail to a card
     		        
     		       if (d.before(today)) {
     		    	  pastCards.add(card);
        		      pastAdapter.notifyDataSetChanged();
         		    	
         		       
     		       } else if (d.equals(today)) {
     		    	   CalendarFragment cf = new CalendarFragment();
        		       cf.addCard(card, _context);
         		        
     		       }else if (d.after(today)) {
     		    	  CalendarFragment2 cf2 = new CalendarFragment2();
       		    	  cf2.addCard(card, _context);
         		    }
     		        

        			
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

	
	void changeTextViewFont(TextView v){
		Typeface t=Typeface.createFromAsset(getActivity().getAssets(),
            "primer_bold.ttf");
		v.setTypeface(t);
	}

    
    public class MainCard extends Card {

    	protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected ImageView mImageView;
        protected CheckBox mCheckbox;
        protected ParseObject mObj;
        protected int cardColor;
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
                public void onSwipe(final Card card) {
                	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
             		// set title
        			alertDialogBuilder.setTitle("Delete "+ getTitle() + "?");
         
        			// set dialog message
        			alertDialogBuilder
        				.setMessage("Are you sure you want to delete "+ getTitle() + "?")
        				.setCancelable(false)
        				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog,int id) {
        						obj.deleteInBackground();
        					}
        				  })
        				.setNegativeButton("No",new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog,int id) {
        						//cards.add(card);
        						//adapter.notifyDataSetChanged();
        						dialog.cancel();
        						
        						
        					}
        				});
        				
        				// create alert dialog
        				AlertDialog alertDialog = alertDialogBuilder.create();
         
        				// show it
        				alertDialog.show();
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
            
            
            
            if (mTitle != null)
                mTitle.setText(title);

            if (mSecondaryTitle != null)
            	mSecondaryTitle.setText(secondaryTitle);

            view.setBackgroundColor(cardColor);
            
          

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

        public int getCardColor() {
            return cardColor;
        }

        public void setCardColor(int cardColor) {
            this.cardColor = cardColor;
        }
    }
}
