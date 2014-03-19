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
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import android.R.integer;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class ChildDeviceInfoFragment extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	static List<ParseObject> ob;
	static ArrayList<Card> appCards = new ArrayList<Card>();
	static ArrayList<Card> contactCards = new ArrayList<Card>();
	static ArrayList<Card> recentCards = new ArrayList<Card>();
	CardListView listView;
	PopupWindow pw; 
	Date today;
	Calendar c;
	private int page;
	private static JSONObject infoObj;
	private PopupMenu popupMenu;
	static String _kid;
	CardArrayAdapter adapter; 
	
	 // newInstance constructor for creating fragment with arguments
    public static ChildDeviceInfoFragment newInstance(int page) {
    	ChildDeviceInfoFragment childDeviceFrag = new ChildDeviceInfoFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", page);
        childDeviceFrag.setArguments(args);
        return childDeviceFrag;
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
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_child_device_info, container, false);
	
	
	
	_context = getActivity();
	

	_kid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("child_list_0", "");
	listView = (CardListView) view.findViewById(R.id.listView);
	
	
	
	
	setHasOptionsMenu(true);
	if(ob != null){
		setupList();
	}else{
		new RemoteDataTask().execute();
	}
	
   
	
	return view;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	   case R.id.menu_child:
		   
		   View menuItemView = getActivity().findViewById(R.id.menu_child);
		   PopupMenu menu = new PopupMenu(_context, menuItemView);
		   
		   
		   menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      		 
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                   _kid = item.getTitle().toString();
                   Log.d("KIDNAME",_kid);
                   return true;
               }
           });
		   int size = PreferenceManager.getDefaultSharedPreferences(_context).getInt("child_list_size",0);
		   for(int i = 0; i<size;i++){
	            String childName = PreferenceManager.getDefaultSharedPreferences(_context).getString("child_list_"+i, "");
	            menu.getMenu().add(Menu.NONE, 0, Menu.NONE, childName);
	        }
		   
		   menu.show();
		   
	        return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	void setupList(){
		if (adapter!=null){
			adapter.clear();
			adapter.notifyDataSetChanged();
			}
		
		
		
		ArrayList<Card> cards = new ArrayList<Card>();
		switch (page) {
        case 0:
        	
        	try {
        		
				JSONArray appArray= infoObj.getJSONArray("appList");
				appCards.clear();
				for(int n = 0; n < appArray.length(); n++)
				{
				    JSONObject object = appArray.getJSONObject(n);
				    
				    SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	    		    String dateString = simpleDate.format(Double.parseDouble((object.getString("date_installed"))));
				    MainCard card = new MainCard(_context);
				    Log.i("NAME", object.getString("name"));
			        //Create a CardHeader
			        CardHeader header = new CardHeader(getActivity());
			        header.setTitle(_kid + "'s apps");
			        card.setTitle(object.getString("name"));
			        
			        
			        card.setSecondaryTitle("installed on " + dateString);
			        //Add Header to card
			        card.addCardHeader(header);
			      //Create thumbnail
			        CardThumbnail thumb = new CardThumbnail(getActivity());
			           
			        card.setBackgroundResourceId(R.drawable.card_background);
			        

				   
			        //Set resource
			        thumb.setDrawableResource(R.drawable.android_icon);
			        card.addCardThumbnail(thumb);
			        card.setSwipeable(false);
			        card.setClickable(true);
				    appCards.add(card);
				    
				    
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	cards = appCards;
        	
        	break;
        case 1:
        	
        	
try {
        		
				JSONArray appArray= infoObj.getJSONArray("contacts");
				contactCards.clear();
				for(int n = 0; n < appArray.length(); n++)
				{
				    JSONObject object = appArray.getJSONObject(n);
				    MainCard card = new MainCard(_context);
				    
			        //Create a CardHeader
			        CardHeader header = new CardHeader(getActivity());
			        header.setTitle(_kid + "'s contacts");
			        card.setTitle(object.getString("name"));
			        card.setSecondaryTitle(object.getString("number"));
			        //Add Header to card
			        card.addCardHeader(header);
			      //Create thumbnail
			        CardThumbnail thumb = new CardThumbnail(getActivity());
			           
			        card.setBackgroundResourceId(R.drawable.card_background);
			        
			        //Set resource
			        thumb.setDrawableResource(R.drawable.person);
			        card.addCardThumbnail(thumb);
			        card.setSwipeable(false);
			        card.setClickable(true);
				    contactCards.add(card);
				    
				    
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cards = contactCards;
        	break ;
        case 2:
        	
try {
        		
				JSONArray appArray= infoObj.getJSONArray("callLog");
				recentCards.clear();
				for(int n = 0; n < appArray.length(); n++)
				{
				    JSONObject object = appArray.getJSONObject(n);
				    MainCard card = new MainCard(_context);
				    
			        //Create a CardHeader
			        CardHeader header = new CardHeader(getActivity());
			        header.setTitle(_kid + "'s call log");
			        card.setTitle(object.getString("name"));
			        SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy hh:mm a");

	    		    String dateString = simpleDate.format(Double.parseDouble((object.getString("date"))));
	    		    int i = Integer.parseInt((object.getString("duration")));
	    		    int tempDur = i/60;
	    		    
	    		    String dur = Integer.toString(tempDur); 
	    		    
	    		    
	    		    card.setSecondaryTitle("on " + dateString +" for " + dur + " minutes");
	    		    
			        //Add Header to card
			        card.addCardHeader(header);
			      //Create thumbnail
			        CardThumbnail thumb = new CardThumbnail(getActivity());
			           
			        card.setBackgroundResourceId(R.drawable.card_background);
			        String temp = object.getString("type");
			        
			        if(temp.equals("OUTGOING")){
			        	thumb.setDrawableResource(R.drawable.dialed_calls_icon);	
			        }else if(temp.equals("INCOMING")){
			        	thumb.setDrawableResource(R.drawable.received_calls_icon);
			        }else if(temp.equals("MISSED")){
			        	thumb.setDrawableResource(R.drawable.missed_calls_icon);
			        }

				   
			        //Set resource
			        
			        card.addCardThumbnail(thumb);
			        card.setSwipeable(false);
			        card.setClickable(true);
				    recentCards.add(card);
				    
				    
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	cards = recentCards;
        	
        	break;
    }
		
		adapter = new CardArrayAdapter(_context, cards);
		listView.setAdapter(adapter);
		Log.d("size", "" + cards.size());
		
		
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
                    "kidContent");
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
        	
        	
        	
            JSONObject obj = new JSONObject(); 
        	
        	
        	for (ParseObject item : ob) {
            	obj = item.getJSONObject("content");
        		infoObj = obj;
        		mProgressDialog.dismiss();
        	
        }
        	
        	setupList();
    }
    
    
    }
    }



