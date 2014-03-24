package com.rbarnes.myfamilyassistant;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditFamPassFragment extends Fragment{
	
	private EditText _parPassInput;
	private EditText _kidPassInput;
	private Form _regForm;
	private Context _context;
	String _famName;
	PopupWindow pw;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	final RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_fam_vaild, container, false);
	
	LinearLayout passView = (LinearLayout)view.findViewById(R.id.makePass);
	passView.setVisibility(View.VISIBLE);
	
	_context = getActivity();
	
	_famName = PreferenceManager.getDefaultSharedPreferences(_context).getString("fam_name", _famName);
	
	Button signupButton = (Button)view.findViewById(R.id.CheckButton);

	signupButton.setText("Update User");
	
	
	_regForm = new Form();
	
	 EditText famNameInput = (EditText)view.findViewById(R.id.famName);
	_parPassInput = (EditText)view.findViewById(R.id.parPass);
	EditText regParPassInput = (EditText)view.findViewById(R.id.reParPass);
	_kidPassInput = (EditText)view.findViewById(R.id.kidPass);
	EditText reKidPassInput = (EditText)view.findViewById(R.id.reKidPass);
	
	
	famNameInput.setEnabled(false);
	famNameInput.setText(_famName);
	_parPassInput.setHint("Enter new or old parent password");
	regParPassInput.setHint("Reenter new or old parent password");
	_kidPassInput.setHint("Enter new or old child password");
	reKidPassInput.setHint("Renter new or old child password");
	
	
	ConfirmValidate confirmParPass = new ConfirmValidate(_parPassInput, regParPassInput);
	ConfirmValidate confirmKidPass = new ConfirmValidate(_kidPassInput, reKidPassInput);
	
	

	
	_regForm.addValidates(confirmParPass);
	_regForm.addValidates(confirmKidPass);
	
	signupButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		if(_regForm.validate()){
    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
    		query.whereEqualTo("famName", _famName);
    		query.findInBackground(new FindCallback<ParseObject>() {
    		    public void done(List<ParseObject> list, ParseException e) {
    		    	if(!list.isEmpty()){
    		    		for(ParseObject famPass : list){
    		    			famPass.put("password", _parPassInput.getText().toString());
    		    			famPass.put("kidPass", _kidPassInput.getText().toString());
    		    			famPass.saveInBackground();
    		    			getActivity().getSupportFragmentManager().popBackStack();
    		    		}
    		    	}else{
    		    		
    		    		Log.d("Something went wrong","This should never happen");
    		    	}
    		    }
    		});
			}
    		
    	}
    	
    	}
    );
	
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


}
