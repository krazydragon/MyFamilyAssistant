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

import java.util.List;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.rbarnes.other.EmailRetriever;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class LoginActivity extends Activity implements OnClickListener,ColorPickerDialog.OnColorChangedListener{
	
	private Button _loginButton;
	private Button _regButton;
	private Button _checkButton;
	private Button _signupButton;
	private Button _colorButton;
	private EditText _regEmailInput;
	private EditText _usernameInput;
	private EditText _passwordInput;
	private EditText _regUserInput;
	private EditText _regPassInput;
	private EditText _regFNameInput;
	private EditText _ParPasswordInput;
	private EditText _famNameInput;
	private EditText _regParPassInput;
	private EditText _kidPassInput;
	private EditText _reKidPassInput;
	private Form _loginForm;
	private Form _regForm;
	private String _famName;
	private String _famPass;
	private String _kidPass;
	private Boolean _isFamThere=true;
	private Boolean _parent=false;
	List<ParseObject> ob;
	LinearLayout _view;
	Context _context;
    private String _pass;
    private String _passName;
	private ParseUser _user;
	private ColorPickerDialog _colorDialog;
	private int _userColor;
	
	private String _msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login);
			
			_loginForm = new Form();
			_loginButton = (Button)findViewById(R.id.loginButton);
			_regButton = (Button)findViewById(R.id.RegisterScreenButton);
			_checkButton = (Button)findViewById(R.id.CheckButton);
			_loginButton.setOnClickListener(this);
			_regButton.setOnClickListener(this);
			_usernameInput = (EditText)findViewById(R.id.loginUsername);
			_passwordInput = (EditText)findViewById(R.id.loginPassword);
			_context = this;
			TextView titleText = (TextView)findViewById(R.id.title);
			
			
			//check user input
			Validate username = new Validate(_usernameInput);
			Validate password = new Validate(_passwordInput);

			username.addValidator(new NotEmptyValidator(this)); 
			password.addValidator(new NotEmptyValidator(this)); 
			
			_loginForm.addValidates(username);
			_loginForm.addValidates(password);
			
			changeEditTextFont(_usernameInput);
			changeButtonFont(_loginButton);
			changeButtonFont(_passwordInput);
			changeTextViewFont(titleText);
			_colorDialog = new ColorPickerDialog(_context, Color.BLACK);
			_colorDialog.setOnColorChangedListener(this);
			
			
	}

	@Override
	public void onClick(View v) {
		Log.d("VIEW", "" + v);
		//Login old user, Register new user, or skip and test application. 
		final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Logging in.  Please wait.");
        dlg.show();
				if(v == _loginButton){
					dlg.dismiss();
					if(_loginForm.validate()){
						//Toast.makeText(getApplicationContext(), "Valid Form", Toast.LENGTH_LONG).show();
						ParseUser.logInInBackground(_usernameInput.getText().toString(), _passwordInput.getText().toString(), new LogInCallback() {
							  @Override
							public void done(ParseUser user, ParseException e) {
								  
							    if (user != null) {
							    	
							    	_msg = "Welcome back " + _usernameInput.getText().toString() + "!";
							    	if((user.getString("famName")==null)||((user.getString("famName").equals("")))){
							    		famCheck();
							    	}else{
							    		_famName = user.getString("famName");
							    		_parent = user.getBoolean("parent");
							    		_userColor = user.getNumber("userColor").intValue();
							    		_user = user;
							    		loginUser();
							    	}
							    } else {
							    	//show error msg
							    	
							    	Crouton.makeText(LoginActivity.this,  "Username or password is invald. Please try again.", Style.ALERT).show();
							    }
							  }
							});
					}
					
					
					
					
				}else if(v == _regButton){
					dlg.dismiss();
			setContentView(R.layout.fragment_edituser);
			_signupButton = (Button)findViewById(R.id.SignupButton);
			_colorButton = (Button)findViewById(R.id.ColorButton);
			_signupButton.setOnClickListener(this);
			_colorButton.setBackgroundColor(Color.BLACK);
			_colorButton.setOnClickListener(this);
			_regForm = new Form();
			
			_regUserInput = (EditText)findViewById(R.id.regUsername);
			_regPassInput = (EditText)findViewById(R.id.descriptionInput);
			EditText regRePassInput = (EditText)findViewById(R.id.regRePassword);
			_regEmailInput = (EditText)findViewById(R.id.regEmail);
			_regFNameInput = (EditText)findViewById(R.id.regFirstName);
			//_regLNameInput = (EditText)findViewById(R.id.regLastName);
			
			
			_regEmailInput.setText(EmailRetriever.getEmail(this));
			
			Validate regUser = new Validate(_regUserInput);
			Validate regEmail = new Validate(_regEmailInput);
			Validate regFName = new Validate(_regFNameInput);
			
			ConfirmValidate confirmPass = new ConfirmValidate(_regPassInput, regRePassInput);
			
			regUser.addValidator(new NotEmptyValidator(this));
			regEmail.addValidator(new NotEmptyValidator(this));
			regFName.addValidator(new NotEmptyValidator(this));
			
			
			_regForm.addValidates(regUser);
			_regForm.addValidates(regEmail);
			_regForm.addValidates(regFName);
			
			_regForm.addValidates(confirmPass);

	}else if(v == _signupButton){
			ParseUser user = new ParseUser();
			

			if(_regForm.validate()){
				user.setUsername(_regUserInput.getText().toString());
				user.setPassword(_regPassInput.getText().toString());
				user.setEmail(_regEmailInput.getText().toString());
				user.put("firstName", _regFNameInput.getText().toString());
				
				user.signUpInBackground(new SignUpCallback() {
					  @Override
					public void done(ParseException e) {
						  dlg.dismiss();
					    if (e == null) {
					    	famCheck();
					    } else {
					      // Sign up didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    	Crouton.makeText(LoginActivity.this, _regEmailInput.getText().toString() + " is in use. Please pick a different one.", Style.ALERT).show();
					    }
					  }
					});
			}
			
			
			
			
		}
	else if(v == _colorButton){
		dlg.dismiss();	
	_colorDialog.show();	
	
		
		
	}
	}
	public void checkPass(View v){
		_user = ParseUser.getCurrentUser();
		if(_isFamThere){
			_famName = _famNameInput.getText().toString();
			ParseQuery<ParseRole> query = ParseRole.getQuery();
			
			
			query.whereEqualTo("name", _famName);
			query.findInBackground(new FindCallback<ParseRole>() {
			    public void done(List<ParseRole> allRoles, ParseException e) {
			    	if(allRoles.isEmpty()){
			    		_isFamThere = false;
			    		_view.setVisibility(View.VISIBLE);
			    		
			    	}else{
			    		
			    		for(ParseRole role : allRoles) {
			    	        
			    	        
			    	        
					    		if(_user != null){
					    		role.getUsers().add(_user);
					    		role.saveInBackground();
					    		
			    	        }
					    		checkFamPass();
			    	    }       
			    		
			    		
			    		}
				    	
			    		
			    	}
			    	
			    	
			    
			});
			
		}else{
			
			ParseACL roleACL = new ParseACL();
			roleACL.setRoleReadAccess(_famName, true);
			roleACL.setRoleWriteAccess(_famName, true);
			roleACL.setPublicReadAccess(true);
			roleACL.setPublicWriteAccess(true);
			ParseRole role = new ParseRole(_famName, roleACL);
			
	    	role.getUsers().add(_user);
			role.saveInBackground();
			
			ParseObject famPass = new ParseObject("Family");
			roleACL = new ParseACL();
			roleACL.setRoleReadAccess(_famName, true);
			roleACL.setRoleWriteAccess(_famName, true);
			
			famPass.put("password", _ParPasswordInput.getText().toString());
			famPass.put("kidPass", _kidPassInput.getText().toString());
			famPass.setACL(roleACL);
			famPass.saveInBackground();
			_user.add("famName", _famName);
            _user.add("parent", true);
            _user.saveInBackground();
			
			_parent = true;
			
			loginUser();
		}
	}
	private void checkFamPass(){
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
	    builder.setTitle("Enter Family Password");

	    // Set up the input
	    final EditText input = new EditText(_context);
	    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
	    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	    builder.setView(input);

	    // Set up the buttons
	    builder.setPositiveButton("Parent", new DialogInterface.OnClickListener() { 
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            _pass = input.getText().toString();
	            _passName = "password";
	            _parent = true;
	            Log.d("USER", ""+_user);
	            _user.put("famName", _famName);
	            _user.put("parent", true);
	            _user.put("userColor", _userColor);
	            _user.saveInBackground();
	            verFamCheck();
	        }
	    });
	    builder.setNegativeButton("Kid", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	        	
	        	_passName = "kidPass";
	        	 _parent = false;
	        	_pass = input.getText().toString();
	        	_user.put("famName", _famName);
	        	_user.put("parent", false);
	        	_user.put("userColor", _userColor);
	        	_user.saveInBackground();

	        	verFamCheck();
	        }
	    });

	    builder.show();
	}
	
	private void verFamCheck(){
		
		
		
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Family");
		
		query.whereEqualTo(_passName, _pass);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> famPass, ParseException e) {
		    	
				
		    	if(famPass.isEmpty()){
		    		Crouton.makeText(LoginActivity.this, "Wrong password please try again.", Style.ALERT).show();
		    		checkFamPass();
		    	}else{
		    		loginUser();
		    		
		    	}
		    }
		});
	}
		
		private void famCheck(){
			setContentView(R.layout.fragment_fam_vaild); 
	    	_famNameInput = (EditText)findViewById(R.id.famName);
	    	_ParPasswordInput = (EditText)findViewById(R.id.parPass);
	    	_regParPassInput = (EditText)findViewById(R.id.reParPass);
	    	_kidPassInput = (EditText)findViewById(R.id.kidPass);
	    	_reKidPassInput = (EditText)findViewById(R.id.reKidPass);
	    	_view = (LinearLayout)findViewById(R.id.makePass);
		}
	//go back to main page
	private void loginUser(){
		
		
		ParseACL roleACL = new ParseACL();
		roleACL.setRoleReadAccess(_famName, true);
		roleACL.setRoleWriteAccess(_famName, true);
		roleACL.setPublicReadAccess(false);
		roleACL.setPublicWriteAccess(false);
		ParseACL.setDefaultACL(roleACL, true);
		
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("fam_auth", true);
        editor.putBoolean("parent", _parent);
        editor.putString("fam_name", _famName);
        editor.putString("user", _user.getString("firstName"));
        editor.commit();
		Intent returnIntent = new Intent();
		 returnIntent.putExtra("msg",_msg);
		 
		 setResult(RESULT_OK, returnIntent);    
		 
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("name", _user.getString("firstName"));
 		installation.put("parent", _parent);
 		installation.put("family", _famName);
 		installation.saveInBackground();
 		
 		
		 finish();
	}
	void changeButtonFont(TextView v){
		Typeface t=Typeface.createFromAsset(getAssets(),
	            "primer.ttf");
		v.setTypeface(t);
	}
	void changeEditTextFont(TextView v){
		Typeface t=Typeface.createFromAsset(getAssets(),
	            "primer.ttf");
		v.setTypeface(t);
	}
	void changeTextViewFont(TextView v){
		Typeface t=Typeface.createFromAsset(getAssets(),
	            "primer.ttf");
		v.setTypeface(t);
	}
	
	@Override
	public void onColorChanged(int color) {
		// TODO Auto-generated method stub
		_colorButton.setBackgroundColor(color);
		_userColor = color;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

	    View v = getCurrentFocus();
	    boolean ret = super.dispatchTouchEvent(event);

	    if (v instanceof EditText) {
	        View w = getCurrentFocus();
	        int scrcoords[] = new int[2];
	        w.getLocationOnScreen(scrcoords);
	        float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 

	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
	    }
	return ret;
	}
}
