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


import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.rbarnes.myfamilyassistant.ChoreFragment.MainCard;
import com.rbarnes.myfamilyassistant.ChoreFragment.RemoteDataTask;

import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LockFragment extends Fragment{
	
	RelativeLayout view;
	boolean isUnlocked = true;
	Button lockButton;
	private String _famName;
	private String _user;
	private String _kid;
	private Boolean _lock = false;
	private ProgressDialog mProgressDialog;
	private Context _context;
	private List<ParseObject> ob;
	private PopupMenu popupMenu;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	view = (RelativeLayout) inflater.inflate(R.layout.fragment_lock, container, false);
	new RemoteDataTask().execute();
	_context = getActivity();
	TextView tv = (TextView) view.findViewById(R.id.lockView);
	lockButton = (Button)view.findViewById(R.id.lockButton);
	
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	_user = PreferenceManager.getDefaultSharedPreferences(_context).getString("user", _user);
	
	
	Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "primer.ttf");
	tv.setTypeface(custom_font);
	lockButton.setTypeface(custom_font);
	
	lockButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub

    		ImageView lockImage = (ImageView)view.findViewById(R.id.lockImage);
    		JSONObject data = new JSONObject();
    		
    		if(isUnlocked){
    			lockImage.setBackgroundResource(R.drawable.lock);
    			
    			lockButton.setText("Unlock");
    			isUnlocked = false;
    			
    			
        		
        		try {
        			data.put("action", "com.rbarnes.UPDATE_STATUS");
        			data.put("name", _user);
        			data.put("goal", "lock");
        			data.put("family", _famName);
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		
        		
    		}else{
    			lockImage.setBackgroundResource(R.drawable.unlock);
    			lockButton.setText("Lock");
    			isUnlocked = true;
    			
    			try {
        			data.put("action", "com.rbarnes.UPDATE_STATUS");
        			data.put("name", _user);
        			data.put("goal", "unlock");
        			data.put("family", _famName);
        		} catch (JSONException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
    		}


    		ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
    		query.whereEqualTo("parent", false);
    		query.whereEqualTo("family", _famName);
    		query.whereEqualTo("name", _kid);
    		
    		
    		ParsePush push = new ParsePush();
    		push.setQuery(query);
    		push.setData(data);
    		push.sendInBackground();
    	}
    	}
    );
	
	
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
                    "KidName");
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
        	
        	
        	
            
        	popupMenu = new PopupMenu(_context, view.findViewById(R.id.lockView));
        	
        	
        	popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        		 
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    _kid = item.getTitle().toString();
                    return true;
                }
            });
        	
        	int i = 1;
        	for (ParseObject item : ob) {
            	
        		popupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, (String) item.get("name"));
                i++;
                
		           
                
            }
        	

        	
        	mProgressDialog.dismiss();
        	popupMenu.show();
        }
    }

}
