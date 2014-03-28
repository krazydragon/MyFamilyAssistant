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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.rbarnes.myfamilyassistant.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GroceryFragment extends Fragment{
	

	private ProgressDialog mProgressDialog;
	private Context _context;
	private List<ParseObject> ob;
	private ArrayList<Card> cards;
	private CardListView listView;
	private CardArrayAdapter adapter; 
	private String _famName;
	private String _user;
	
	@SuppressWarnings({ })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_main_list, container, false);
	
	_context = getActivity();
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	cards = new ArrayList<Card>();
	listView = (CardListView) view.findViewById(R.id.choir_list);
	TextView titleText = (TextView)view.findViewById(R.id.title);
	    
	new RemoteDataTask().execute();

	titleText.setText("Groceries");
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
		
		
		final EditText popUpTxt = new EditText(_context);

		
		popUpTxt.setHint("Enter groceries needed");

		new AlertDialog.Builder(_context)
		  .setTitle("Add Groceries")
		  .setView(popUpTxt)
		  .setPositiveButton("Add", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	if(popUpTxt.getText().toString().trim().length() > 0){

		    		// TODO Auto-generated method stub
            		
        			ParseObject obj = new ParseObject("Groceries");
        			ParseACL postACL = new ParseACL();
        			postACL.setRoleWriteAccess(_famName, true);
        			postACL.setRoleReadAccess(_famName, true);
        			obj.setACL(postACL);
        			
        			obj.put("item", popUpTxt.getText().toString());
        			obj.put("completed", false);
        			
        			obj.saveEventually();
            			
            			
        			//Create a Card
                    
               		MainCard card = new MainCard(getActivity());
                       //Create a CardHeader
                       CardHeader header = new CardHeader(getActivity());
                       header.setTitle("Groceries");
                       card.setTitle(popUpTxt.getText().toString());
                       //Add Header to card
                       card.addCardHeader(header);
                     //Create thumbnail
        		        
        	              
        		        card.setChecked(false);
        		        card.setObj(obj);   
        		       
        		       
        		        card.setBackgroundResourceId(R.drawable.card_background);
        		           
        		        
        		        card.setSwipeable(true);
        		        card.setClickable(true);
        		        //Add thumbnail to a card
        		        
                       cards.add(card);
        			adapter.notifyDataSetChanged();
        			
        			JSONObject data = new JSONObject();
        			
        			
        			
        			try {
        				data.put("alert", popUpTxt.getText().toString()+" has been added to the grocery list.");
        			} catch (JSONException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			
        			
        			ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        			query.whereEqualTo("parent", true);
        			query.whereEqualTo("family", _famName);

        			
        			
        			ParsePush push = new ParsePush();
        			push.setQuery(query);
        			push.setData(data);
        			push.sendInBackground();
	        		

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
                
        		MainCard card = new MainCard(getActivity());
                //Create a CardHeader
                CardHeader header = new CardHeader(getActivity());
                card.setTitle((String) item.get("item"));
                //Add Header to card
                header.setTitle("Groceries");
                card.addCardHeader(header);
              //Create thumbnail
		        CardThumbnail thumb = new CardThumbnail(getActivity());
	              
		        card.setBackgroundResourceId(R.drawable.card_background);
			    card.setObj(item);   
			    card.setChecked((Boolean) item.get("completed"));
		        if((Boolean) item.get("completed")){
		        	   
		               card.setBackgroundResourceId(R.drawable.card_background2);
		           }else {
		        	  
		               card.setBackgroundResourceId(R.drawable.card_background);
		           }
		        //Set resource
		        thumb.setDrawableResource(R.drawable.ic_launcher);
		        card.setSwipeable(true);
		        card.setClickable(true);
		        //Add thumbnail to a card
		        
                cards.add(card);
                
                
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
            cardThumbnail.setDrawableResource(R.drawable.shopping_cart_icon);
            addCardThumbnail(cardThumbnail);
            
        	setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(final Card card) {
                	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            				_context);

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
                    	 if(mCheckbox.isChecked()){
                            	Toast.makeText(getContext(), "You need to buy more " + title, Toast.LENGTH_SHORT).show();
                            	obj.put("completed", false);
                                mCheckbox.setChecked(false);
                                card.changeBackgroundResourceId(R.drawable.card_background);
                            }else{
                            	Toast.makeText(getContext(), title + " was purchased", Toast.LENGTH_SHORT).show();
                            	obj.put("completed", true);
                                mCheckbox.setChecked(true);
                                card.changeBackgroundResourceId(R.drawable.card_background2);
                            }
                    	 obj.saveEventually();
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
                mSecondaryTitle.setText("");

            mCheckbox.setChecked(checked);
            if(checked){
         	   mCheckbox.setChecked(true);
                
            }else {
         	   mCheckbox.setChecked(false);
                
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
