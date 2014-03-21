package com.rbarnes.myfamilyassistant;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import it.gmariotti.cardslib.library.internal.CardHeader;

import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rbarnes.myfamilyassistant.MessageFragment.MainCard;
import com.rbarnes.other.EmailRetriever;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import android.content.Context;
import android.graphics.Color;
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

public class EditUserFragment extends Fragment implements ColorPickerDialog.OnColorChangedListener{
	
	private EditText _regUserInput;
	private EditText _regPassInput;
	private EditText _regFNameInput;
	private EditText _regEmailInput;
	private Form _regForm;
	private Context _context;
	int _userColor;
	ParseUser _currentUser;
	PopupWindow pw;
	ColorPickerDialog dialog;
	Button colorButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
	super.onCreate(savedInstanceState); 
	
	RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_edituser, container, false);
	_context = getActivity();
	
	_currentUser = ParseUser.getCurrentUser();
	_userColor =  _currentUser.getNumber("userColor").intValue();
	Log.d("color", ""+_userColor);
	dialog = new ColorPickerDialog(_context, _userColor);
	Button signupButton = (Button)view.findViewById(R.id.SignupButton);
	colorButton = (Button)view.findViewById(R.id.ColorButton);
	signupButton.setText("Update User");
	
	
	_regForm = new Form();
	
	_regUserInput = (EditText)view.findViewById(R.id.regUsername);
	_regPassInput = (EditText)view.findViewById(R.id.descriptionInput);
	EditText regRePassInput = (EditText)view.findViewById(R.id.regRePassword);
	_regEmailInput = (EditText)view.findViewById(R.id.regEmail);
	_regFNameInput = (EditText)view.findViewById(R.id.regFirstName);

	
	
	_regUserInput.setEnabled(false);
	_regUserInput.setText(_currentUser.getUsername());
	_regEmailInput.setText(_currentUser.getEmail());
	_regFNameInput.setText(_currentUser.getString("firstName"));
	_regEmailInput.setText(EmailRetriever.getEmail(_context));
	
	Validate regUser = new Validate(_regUserInput);
	Validate regEmail = new Validate(_regEmailInput);
	Validate regFName = new Validate(_regFNameInput);
	ConfirmValidate confirmPass = new ConfirmValidate(_regPassInput, regRePassInput);
	
	regUser.addValidator(new NotEmptyValidator(_context));
	regEmail.addValidator(new NotEmptyValidator(_context));
	regFName.addValidator(new NotEmptyValidator(_context));


	
	_regForm.addValidates(regUser);
	_regForm.addValidates(regEmail);
	_regForm.addValidates(regFName);
	_regForm.addValidates(confirmPass);
	colorButton.setBackgroundColor(_userColor);
	signupButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		if(_regForm.validate()){
    			_currentUser.setUsername(_regUserInput.getText().toString());
    			_currentUser.setPassword(_regPassInput.getText().toString());
    			_currentUser.setEmail(_regEmailInput.getText().toString());
    			_currentUser.put("firstName", _regFNameInput.getText().toString());
				_currentUser.put("userColor", _userColor);
    			addPopUp();
			}
    		

    	}
    	}
    );
	
	colorButton.setOnClickListener(new Button.OnClickListener(){

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    					
    		dialog.show();

    	}
    	}
    );
	dialog.setOnColorChangedListener(this);
	
	
	return view;
	
	
	
	
	}

public void addPopUp(){
		
		
		
		
		//We need to get the instance of the LayoutInflater, use the context of this activity
        LayoutInflater inflater = (LayoutInflater) EditUserFragment.this
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
        
		addTitleText.setText("Enter current password");
		popupInput.setHint("Current Pasword");
		
        submit.setOnClickListener(new Button.OnClickListener(){

        	@Override
        	public void onClick(View v) {
        		
        		if(popupInput.getText().toString().trim().length() > 0){

        			
        			ParseUser.logInInBackground(_regUserInput.getText().toString(), popupInput.getText().toString(), new LogInCallback() {
  					  public void done(ParseUser user, ParseException e) {
  					    if (e == null) {
  					    	_currentUser.saveEventually();
  					    	getActivity().getSupportFragmentManager().popBackStack();
  					    } else {
  					      // Sign up didn't succeed. Look at the ParseException
  					      // to figure out what went wrong
  					    	
  					    }
  					  }
  					});
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

@Override
public void onColorChanged(int color) {
	// TODO Auto-generated method stub
	colorButton.setBackgroundColor(color);
	Log.d("my color", "" + color);
	_userColor = color;
}
}
