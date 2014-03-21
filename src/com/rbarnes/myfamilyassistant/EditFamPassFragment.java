package com.rbarnes.myfamilyassistant;

import java.util.List;

import it.gmariotti.cardslib.library.internal.CardHeader;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rbarnes.myfamilyassistant.MessageFragment.MainCard;
import com.rbarnes.other.EmailRetriever;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	
	RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_fam_vaild, container, false);
	
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
	regParPassInput.setText("Reenter new or old parent password");
	_kidPassInput.setText("Enter new or old child password");
	reKidPassInput.setText("Renter new or old child password");
	
	
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
