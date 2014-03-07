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
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChoreFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	List<ParseObject> ob;
	ArrayList<Card> cards;
	ArrayList<Card> completedCards;
	CardListView listView;
	PopupWindow pw; 
	CardArrayAdapter adapter;
	CardArrayAdapter completedAdapter;
	private int page; 
	
	
	
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
	cards = new ArrayList<Card>();
	completedCards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.choir_list);
	TextView titleText = (TextView)view.findViewById(R.id.title);
	ImageButton button = (ImageButton)view.findViewById(R.id.addButton);     
	new RemoteDataTask().execute();

	titleText.setText("Chores");
	changeTextViewFont(titleText);
	
	button.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub

    		addPopUp();

    	}
    	}
    );
	
	return view;
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
            		String s = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", "error");
            		
        			ParseObject obj = new ParseObject("Chores");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(s, true);
        			postACL.setRoleReadAccess(s, true);
        			//obj.setACL(postACL);
        			
        			obj.put("item", popupInput.getText().toString());
        			obj.put("completed", false);
        			
        			obj.saveEventually();
            			
            			
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       card.setTitle(popupInput.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
        	              
        		        card.setChecked(false);
        		        card.setObj(obj);   
        		       
        		       
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
                card.setTitle((String) item.get("item"));
                //Add Header to card
                card.addCardHeader(header);
              //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
	              
		        card.setBackgroundResourceId(R.drawable.card_background);
		        card.setObj(item);   
		        card.setChecked((Boolean) item.get("completed"));
		        Date date = new Date();
		        
		        
        		
 		       

    		    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    		    String dateString = simpleDate.format(date);
    		    

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
		               completedCards.add(card);
		           }else {
		        	   date = item.getCreatedAt();
		        	   dateString = simpleDate.format(date);
		        	   card.setSecondaryTitle(dateString);
		               card.setBackgroundResourceId(R.drawable.card_background);
		               card.setClickable(true);
		               cards.add(card);
		           }
                
            }
        	

        	

        	
        	
        	adapter = new CardArrayAdapter(getActivity(),cards);
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
        protected ImageView mImageView;
        protected CheckBox mCheckbox;
        protected ParseObject mObj;
        protected int resourceIdThumbnail;
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
                	        			
                	        			newObj.saveEventually();
                	        			
                	        			MainCard newCard = new MainCard(getActivity());
                	                       //Create a CardHeader
                	                       CardHeader header = new CardHeader(getActivity());
                	                       newCard.setTitle(getTitle());
                	                       
                	                       Date date = new Date();
                	       		        
                	       		        
                	               		
                	         		       

                	           		    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

                	           		    String dateString = simpleDate.format(date);
                	           		    
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
                        if(mCheckbox.isChecked()){
                        	
                        	obj.put("completed", false);
                            mCheckbox.setChecked(false);
                            card.changeBackgroundResourceId(R.drawable.card_background);
                        }else{
                        	
                        	obj.put("completed", true);
                        	mCheckbox.setChecked(true);
                            card.changeBackgroundResourceId(R.drawable.card_background2);
                        }
                        obj.saveEventually();
                        cards.remove(card);
                        completedCards.add(card);
                        adapter.notifyDataSetChanged();
                        completedAdapter.notifyDataSetChanged();
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
            
            mCheckbox.setChecked(checked);
            if(checked){
         	   mCheckbox.setChecked(true);
         	  mSecondaryTitle.setText("Completed on" + secondaryTitle); 
            }else {
         	   mCheckbox.setChecked(false);
         	  mSecondaryTitle.setText( "Created on "+ secondaryTitle);
            }
            
                mTitle.setText(title);

            
                

            
            mImageView.setImageResource(R.drawable.broom);
          

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

        public int getResourceIdThumbnail() {
            return resourceIdThumbnail;
        }

        public void setResourceIdThumbnail(int resourceIdThumbnail) {
            this.resourceIdThumbnail = resourceIdThumbnail;
        }
    }

}
