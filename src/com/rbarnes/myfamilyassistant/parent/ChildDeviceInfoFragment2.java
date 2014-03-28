/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 1, 2014
 */
package com.rbarnes.myfamilyassistant.parent;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rbarnes.myfamilyassistant.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;


public class ChildDeviceInfoFragment2 extends Fragment{
	
	private ProgressDialog mProgressDialog;
	private Context _context;
	private CardListView listView;
	public static JSONObject infoObj;
	private static String _kid;
	private static CardArrayAdapter adapter; 
	private static ArrayList<Card> cards;
	


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
	
	final LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_child_device_info, container, false);
	
	
	
	_context = getActivity();
	listView = (CardListView) view.findViewById(R.id.listView);
	 // Create a progressdialog
    mProgressDialog = new ProgressDialog(getActivity());
    // Set progressdialog title
    mProgressDialog.setTitle("");
    // Set progressdialog message
    mProgressDialog.setMessage("Loading...");
    mProgressDialog.setIndeterminate(false);
	
	if(infoObj != null){
		setupList();
	}else{
		_kid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("child_list_0", "");
		cards = new ArrayList<Card>();
		adapter = new CardArrayAdapter(_context, cards);
		
	}
	
	
	
	setHasOptionsMenu(true);
	if(infoObj != null){
		setupList();
	}else{
		_kid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("child_list_0", "");
		
	}
	
	listView.setAdapter(adapter);
	
	return view;
	}
	
	public void updateKidName(JSONObject obj, String kid, Context context){
		infoObj = obj;
		_kid = kid;
		if(_context == null)
			_context = context;
		if(cards == null)
			cards = new ArrayList<Card>();
		if(adapter == null)
			adapter = new CardArrayAdapter(_context, cards);
			
		setupList();
		
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu,inflater);
		MenuItem childItem = menu.findItem(R.id.menu_child);
		childItem.setVisible(true);
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
                  
          			ParseQuery<ParseObject> query = ParseQuery.getQuery("kidContent");
          			query.whereEqualTo("name", _kid);
          			mProgressDialog.show();
          			query.findInBackground(new FindCallback<ParseObject>() {
          		    public void done(List<ParseObject> list, ParseException e) {
          		    	JSONObject obj = new JSONObject(); 
          	        	
          	        	
          	        	for (ParseObject item : list) {
          	            	obj = item.getJSONObject("content");
          	        		infoObj = obj;
          	        		ChildDeviceInfoFragment cdi = new ChildDeviceInfoFragment();
                           ChildDeviceInfoFragment3 cdi3 = new ChildDeviceInfoFragment3();
                           cdi.updateKidName(infoObj, _kid,_context);
                           cdi3.updateKidName(infoObj, _kid,_context);
                           
                           setupList();
                           mProgressDialog.dismiss();
          	        	
          	        }
          	        	
          	        	
          		    }
          		});
                   
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
		
		
		
		
        	
try {
        		
				JSONArray appArray= infoObj.getJSONArray("contacts");
				cards.clear();
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
				    cards.add(card);
				    adapter.notifyDataSetChanged();
				    
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		
		
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
            mSecondaryTitle = (TextView) view.findViewById(R.id.inner_title2);
            mCheckbox = (CheckBox)view.findViewById(R.id.checkBox1);
            
            
            
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



