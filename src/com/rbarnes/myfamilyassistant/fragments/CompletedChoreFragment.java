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



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rbarnes.myfamilyassistant.R;
import com.rbarnes.myfamilyassistant.R.drawable;
import com.rbarnes.myfamilyassistant.R.id;
import com.rbarnes.myfamilyassistant.R.layout;
import com.rbarnes.myfamilyassistant.fragments.ChoreFragment.MainCard;

@SuppressLint("SimpleDateFormat")
public class CompletedChoreFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	private List<ParseObject> ob;
	private static ArrayList<Card> completedCards;
	private CardListView listView;
	private PopupWindow pw; 
	private static  CardArrayAdapter completedAdapter;
	private int page; 
	private String _famName;
	private static String _user;
	private String _tempString;
	private int _userColor;
	private Boolean _parent = false;
	private static String _tempHeader; 
	
	 // newInstance constructor for creating fragment with arguments
    public static CompletedChoreFragment newInstance(int page) {
    	CompletedChoreFragment choreFrag = new CompletedChoreFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        choreFrag.setArguments(args);
        return choreFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
    }
    
    
    
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_main_list, container, false);
	
	
	_context = getActivity();
	_parent = PreferenceManager.getDefaultSharedPreferences(_context).getBoolean("parent", _parent);
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	completedCards = new ArrayList<Card>();
	completedAdapter = new CardArrayAdapter(getActivity(),completedCards);
	listView = (CardListView) view.findViewById(R.id.choir_list);
	TextView titleText = (TextView)view.findViewById(R.id.title);
	_userColor = ParseUser.getCurrentUser().getNumber("userColor").intValue();    
	

	titleText.setText("Chores");
	changeTextViewFont(titleText);
	
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
	
	listView.setAdapter(completedAdapter);
	return view;
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu,inflater);
	    MenuItem addItem = menu.findItem(R.id.menu_add);
		MenuItem childItem = menu.findItem(R.id.menu_child);
		addItem.setVisible(true);
		childItem.setVisible(false);
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
		query.whereEqualTo("parent", false);
		query.whereEqualTo("family", _famName);

		
		
		ParsePush push = new ParsePush();
		push.setQuery(query);
		push.setData(data);
		push.sendInBackground();
	}
	
	public void addPopUp(){
		
		final EditText popUpTxt = new EditText(_context);

		// Set the default text to a link of the Queen
		popUpTxt.setHint("Enter new chore");

		new AlertDialog.Builder(_context)
		  .setTitle("Add Chores")
		  .setView(popUpTxt)
		  .setPositiveButton("Add", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	if(popUpTxt.getText().toString().trim().length() > 0){

	    			
	        		
		    		ParseObject obj = new ParseObject("Chores");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(_famName, true);
        			postACL.setRoleReadAccess(_famName, true);
        			obj.setACL(postACL);
        			
        			obj.put("item", popUpTxt.getText().toString());
        			obj.put("completed", false);
        			obj.put("from", _user);
        			obj.put("color", _userColor);
        			obj.saveEventually();
            			
        			_tempString = _user + " says that " + popUpTxt.getText().toString() + " needs to be completed";
        			sendNoti();
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       header.setTitle(popUpTxt.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
                       Calendar c = Calendar.getInstance();
       				
       		        Date date = c.getTime();
       	               
       	            card.setSecondaryTitle((String) android.text.format.DateFormat.format("EEEE MMMM d yyyy hh:mm a", date));   
        		        card.setChecked(false);
        		        card.setObj(obj);   
        		       card.setTitle(_user);
        		       card.setCardColor(_userColor);
        		        card.setBackgroundResourceId(R.drawable.card_background);
        		           
        		        
        		        card.setSwipeable(false);
        		        card.setClickable(true);
        		        //Add thumbnail to a card
        		        
        		        ChoreFragment cf = new ChoreFragment();
                        cf.addCard(card);

					}

					else {

						Toast.makeText(getActivity(),"Input can not be blank!", Toast.LENGTH_SHORT).show();
						
					}
		    }
		  })
		  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	
		    }
		  })
		  .show();
		
		
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
            "primer_bold.ttf");
		v.setTypeface(t);
	}
	
	public void addCard(Card card){
		completedCards.add(card);
		completedAdapter.notifyDataSetChanged();
		
	}
	
    
    public class MainCard extends Card {

    	protected TextView mTitle;
        protected TextView mSecondaryTitle;
        protected CheckBox mCheckbox;
        protected ParseObject mObj;
        protected int cardColor;
        protected int count;
        protected ParseObject obj;
        protected String title;
        protected String secondaryTitle;
        protected float image;
        protected Boolean checked = false;

        public MainCard(Context context) {
            this(context, R.layout.custom_card);
        }

        public MainCard(Context context, int innerLayout) {
            super(context, innerLayout);
            init();
        }

        private void init() {
        	
        	//Add thumbnail
            CardThumbnail cardThumbnail = new CardThumbnail(mContext);
            cardThumbnail.setDrawableResource(R.drawable.broom);
            addCardThumbnail(cardThumbnail);

        	setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    obj.deleteInBackground();
                }
            });

        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            //Retrieve elements
            mTitle = (TextView) view.findViewById(R.id.inner_title);
            mSecondaryTitle = (TextView) parent.findViewById(R.id.inner_title2);
            mCheckbox = (CheckBox)parent.findViewById(R.id.checkBox1);
            
            changeEditTextFont(mTitle);
            changeEditTextFont(mSecondaryTitle);
            
            mCheckbox.setChecked(checked);
            if(checked){
            	mTitle.setText("Completed by " + title);
         	   mCheckbox.setChecked(true);
         	  mSecondaryTitle.setText("Completed on" + secondaryTitle); 
            }else {
            	mTitle.setText("Assigned by " + title);
         	   mCheckbox.setChecked(false);
         	  mSecondaryTitle.setText( "Created on "+ secondaryTitle);
            }
            
                
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
        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public int getCardColor() {
            return cardColor;
        }

        public void setCardColor(int cardColor) {
            this.cardColor = cardColor;
        }
    }

}
