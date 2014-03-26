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
import it.gmariotti.cardslib.library.view.CardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MessageFragment extends Fragment{
	
	 
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> cards;
	CardListView listView;
	CardArrayAdapter adapter; 
	PopupWindow pw;
	private String _famName;
	private String _user;
	private int _userColor;
	
	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_messages, container, false);
	
	_context = getActivity();
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	_userColor = ParseUser.getCurrentUser().getNumber("userColor").intValue();
	cards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.choir_list);
	TextView titleText = (TextView)view.findViewById(R.id.title);
	 

	
	new RemoteDataTask().execute();
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
		addItem.setVisible(true);
	}
	
	public void addPopUp(){
		
		
		
		
		//We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) MessageFragment.this
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
        
		addTitleText.setText("Add Message");
		popupInput.setHint("Enter message");
		
        submit.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		
        		if(popupInput.getText().toString().trim().length() > 0){

        			// TODO Auto-generated method stub
            		
            		
        			ParseObject obj = new ParseObject("Messages");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(_famName, true);
        			postACL.setRoleReadAccess(_famName, true);
        			obj.setACL(postACL);
        			
        			obj.put("message", popupInput.getText().toString());
        			obj.put("from", _user);
        			obj.put("read", false);
        			obj.put("color", _userColor);
        			
        			obj.saveEventually();
            			
            			
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       header.setTitle("From "+ _user);
                       card.setTitle(popupInput.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
        	              
        		        
        		        card.setObj(obj);   
        		       card.setCardColor(ParseUser.getCurrentUser().getNumber("userColor").intValue());
        		       
        		        card.setBackgroundResourceId(R.drawable.card_background);
        		        Calendar c = Calendar.getInstance();
        				
        		        Date date = c.getTime();
        	               
        	            card.setSecondaryTitle("Sent on " +(String) android.text.format.DateFormat.format("EEEE MMMM d yyyy hh:mm a", date));   
        		        CardView cardView = (CardView) getActivity().findViewById(R.id.list_cardId);
        		        cardView.setCard(card); 
        		        card.setSwipeable(true);
        		        card.setClickable(true);
        		        //Add thumbnail to a card
        		        cardView.refreshCard(card);
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
                   "Messages");
           query.addAscendingOrder("_created_at");
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
               
               Date date = item.getCreatedAt();
               
               card.setSecondaryTitle("Sent on " +(String) android.text.format.DateFormat.format("EEEE MMMM d yyyy hh:mm a", date));
               //Add Header to card
               card.addCardHeader(header);
             //Create thumbnail
		        @SuppressWarnings("unused")
				CardThumbnail thumb = new CardThumbnail(getActivity());
	              
		        card.setBackgroundResourceId(R.drawable.card_background);
		        card.setObj(item);  
		        card.setCardColor(item.getNumber("color").intValue());

		        //Set resource
		        
		        card.setSwipeable(true);
		        card.setClickable(false);
		        //Add thumbnail to a card
		        
               cards.add(card);
               
               if(!(Boolean) item.get("read")){
	        	   item.put("read", true);
		        	item.saveEventually();
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
           cardThumbnail.setDrawableResource(R.drawable.message);
           addCardThumbnail(cardThumbnail);
           
    	   setOnSwipeListener(new Card.OnSwipeListener() {
               @Override
               public void onSwipe(final Card card) {
            	   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_context);
           		// set title
      			alertDialogBuilder.setTitle("Delete "+ getTitle() + "?");
       
      			// set dialog message
      			alertDialogBuilder
      				.setMessage("Are you sure you want to delete this message?")
      				.setCancelable(false)
      				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
      					public void onClick(DialogInterface dialog,int id) {
      						obj.deleteInBackground();
      					}
      				  })
      				.setNegativeButton("No",new DialogInterface.OnClickListener() {
      					public void onClick(DialogInterface dialog,int id) {
      						cards.add(card);
      						adapter.notifyDataSetChanged();
      						dialog.cancel();
      						
      						
      					}
      				});
      				
      				// create alert dialog
      				AlertDialog alertDialog = alertDialogBuilder.create();
       
      				// show it
      				alertDialog.show();
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

           view.setBackgroundColor(cardColor);
           
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

       public int getCardColor() {
           return cardColor;
       }

       public void setCardColor(int cardColor) {
           this.cardColor = cardColor;
       }
   }

}
