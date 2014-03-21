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

import org.json.JSONException;
import org.json.JSONObject;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class ChoreFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	private List<ParseObject> ob;
	private ArrayList<Card> cards;
	private static ArrayList<Card> completedCards;
	private CardListView listView;
	private PopupWindow pw; 
	CardArrayAdapter adapter;
	static CardArrayAdapter completedAdapter;
	private int page; 
	private String _famName;
	private String _user;
	private String _tempString;
	private int _userColor;
	
	
	 // newInstance constructor for creating fragment with arguments
    public static ChoreFragment newInstance(int page) {
    	ChoreFragment choreFrag = new ChoreFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        choreFrag.setArguments(args);
        return choreFrag;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page", 0);
    }
    
    
    
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_main_list, container, false);
	
	
	_context = getActivity();
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	cards = new ArrayList<Card>();
	completedCards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.choir_list);
	TextView titleText = (TextView)view.findViewById(R.id.title);
	_userColor = ParseUser.getCurrentUser().getNumber("userColor").intValue();    
	new RemoteDataTask().execute();

	titleText.setText("Chores");
	changeTextViewFont(titleText);
	
	setHasOptionsMenu(true);

	view.setFocusableInTouchMode(true);
	view.requestFocus();
	view.setOnKeyListener(new View.OnKeyListener() {
	        @Override
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	            Log.i("does this work", "keyCode: " + keyCode);
	            if( keyCode == KeyEvent.KEYCODE_BACK ) {
	                    Log.i("does this work", "onKey Back listener is working!!!");
	                getFragmentManager().popBackStack();
	                return true;
	            } else {
	            	getFragmentManager().popBackStack();
	                return false;
	            }
	            
	        }
	    });
	
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
		
		
		
		
		//We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) ChoreFragment.this
                .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout
        View layout = inflater.inflate(R.layout.fragment_main_add,
                (ViewGroup) getActivity().findViewById(R.id.PopUpAddLayout));
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x/2;
        int height = size.y/4;
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
        
		addTitleText.setText("Add Chores");
		popupInput.setHint("Enter new chore");
		
        submit.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		
        		if(popupInput.getText().toString().trim().length() > 0){

        			// TODO Auto-generated method stub
            		
        			ParseObject obj = new ParseObject("Chores");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(_famName, true);
        			postACL.setRoleReadAccess(_famName, true);
        			obj.setACL(postACL);
        			
        			obj.put("item", popupInput.getText().toString());
        			obj.put("completed", false);
        			obj.put("from", _user);
        			obj.put("color", _userColor);
        			obj.saveEventually();
            			
        			_tempString = _user + " says that " + popupInput.getText().toString() + " needs to be completed";
        			sendNoti();
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       header.setTitle(popupInput.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
                       Calendar c = Calendar.getInstance();
       				
       		        Date date = c.getTime();
       	               
       	            card.setSecondaryTitle((String) android.text.format.DateFormat.format("EEEE MMMM d yyyy hh:mm a", date));   
        		        card.setChecked(false);
        		        card.setObj(obj);   
        		       card.setTitle(_user);
        		       
        		        card.setBackgroundResourceId(R.drawable.card_background);
        		           
        		        
        		        card.setSwipeable(false);
        		        card.setClickable(true);
        		        //Add thumbnail to a card
        		        
                       cards.add(card);
        			adapter.notifyDataSetChanged();
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
                    "Chores");
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
               
                header.setTitle((String) item.get("item"));
                //Add Header to card
                card.addCardHeader(header);
              //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
	            
		        //card.setCardColor(item.getNumber("color").intValue());
		        card.setObj(item);   
		        card.setChecked((Boolean) item.get("completed"));
		        Date date = new Date();
		        
		        
        		
		        card.setCardColor(item.getNumber("color").intValue());

    		    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    		    String dateString = simpleDate.format(date);
    		    
    		    card.setId(item.getObjectId());
		        //Set resource
		        thumb.setDrawableResource(R.drawable.ic_launcher);
		        //card.setSwipeable(true);
		        
		        //Add thumbnail to a card
		        if((Boolean) item.get("completed")){
		        	date = item.getUpdatedAt();
		        	dateString = simpleDate.format(date);
		        	card.setSecondaryTitle(dateString);
		               card.setBackgroundResourceId(R.drawable.card_background2);
		               card.setClickable(false);
		               card.setTitle(item.getString("by"));
		               completedCards.add(card);
		           }else {
		        	   date = item.getCreatedAt();
		        	   dateString = simpleDate.format(date);
		        	   card.setSecondaryTitle(dateString);
		               card.setBackgroundResourceId(R.drawable.card_background);
		               card.setClickable(true);
		               card.setTitle(item.getString("from"));
		               cards.add(card);
		           }
                
            }
        	

        	

        	
        	
        	adapter = new CardArrayAdapter(getActivity(),cards);
        	adapter.setEnableUndo(true);
        	adapter.setNotifyOnChange(true);
        	completedAdapter = new CardArrayAdapter(getActivity(),completedCards);
        	
        	if (page == 0){
        		listView.setAdapter(adapter);
            }
        	
        	if (page == 1){
        		listView.setAdapter(completedAdapter);
            }
        	mProgressDialog.dismiss();
        	
        }
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

                //Add ClickListener
                setOnClickListener(new OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                				_context);
                 
                    	Date date = new Date();
	       		        
	       		        
	               		
	         		       

	           		    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	           		    final String dateString = simpleDate.format(date);
                			// set title
                			alertDialogBuilder.setTitle("Chore Completed");
                 
                			// set dialog message
                			alertDialogBuilder
                				.setMessage("Click to add this chore again!")
                				.setCancelable(false)
                				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                					public void onClick(DialogInterface dialog,int id) {
                						// if this button is clicked, close
                						// current activity
                						// TODO Auto-generated method stub
                	            		String s = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", "error");
                	            		
                	        			ParseObject newObj = new ParseObject("Chores");
                	        			ParseACL postACL = new ParseACL();
                	        			postACL.setRoleWriteAccess(s, true);
                	        			postACL.setRoleReadAccess(s, true);
                	        			//obj.setACL(postACL);
                	        			
                	        			newObj.put("item", getTitle());
                	        			newObj.put("completed", false);
                	        			newObj.put("by", _user);
                	        			newObj.saveEventually();
                	        			
                	        			MainCard newCard = new MainCard(getActivity());
                	                       //Create a CardHeader
                	                       CardHeader header = new CardHeader(getActivity());
                	                       newCard.setTitle(_user);
                	                       
                	                       
                	           		    
                	           		    newCard.setSecondaryTitle(dateString);
                	                       //Add Header to card
                	                       newCard.addCardHeader(header);
                	                     //Create thumbnail
                	        		        
                	        	              
                	                       newCard.setChecked(false);
                	                       newCard.setObj(newObj);   
                	        		       
                	        		       
                	                       newCard.setBackgroundResourceId(R.drawable.card_background);
                	        		           
                	        		        
                	                       newCard.setSwipeable(false);
                	                       newCard.setClickable(true);
                	        		        //Add thumbnail to a card
                	        		        
                	                       cards.add(newCard);
                	        			adapter.notifyDataSetChanged();
                	        			
                					}
                				  })
                				.setNegativeButton("No",new DialogInterface.OnClickListener() {
                					public void onClick(DialogInterface dialog,int id) {
                						// if this button is clicked, just close
                						// the dialog box and do nothing
                						dialog.cancel();
                					}
                				});
                				
                				// create alert dialog
                				AlertDialog alertDialog = alertDialogBuilder.create();
                 
                				// show it
                				alertDialog.show();
                        
                        	
                        
                       
                        	
                        obj.put("completed", true);	
                        obj.saveEventually();
                        cards.remove(card);
                        
                        
                        MainCard newCard = new MainCard(getActivity());
	                       //Create a CardHeader
	                       CardHeader header = new CardHeader(getActivity());
	                       newCard.setTitle(getTitle());
	                       header.setTitle(card.getCardHeader().getTitle());
	                       
	           		    
	           		    newCard.setSecondaryTitle(dateString);
	                       //Add Header to card
	                       newCard.addCardHeader(header);
	                     //Create thumbnail
	        		        
	                       ParseObject newObj = new ParseObject("Chores");   
	                       newCard.setChecked(true);
	                       newCard.setObj(newObj);   
	                       newCard.setBackgroundResourceId(R.drawable.card_background2);
	                       newCard.setSwipeable(false);
	                       newCard.setClickable(true);
                        
                        
                        adapter.notifyDataSetChanged();
                        completedCards.add(newCard);
                        completedAdapter.notifyDataSetChanged();
                        
                        JSONObject data = new JSONObject();
                		try {
                			data.put("action", "com.rbarnes.UPDATE_STATUS");
                			data.put("alert", "Chore was completed");
                		} catch (JSONException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
                		query.whereEqualTo("parent", true);
                		query.whereEqualTo("family", "krazy");

                		
                		
                		ParsePush push = new ParsePush();
                		push.setQuery(query);
                		push.setData(data);
                		push.sendInBackground();
                        
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
