/*
 * project	MyFamilyAssistant
 * 
 * package	com.rbarnes.myfamilyassistant
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Feb 19, 2014
 */
package com.rbarnes.myfamilyassistant;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.cardsui.CardBase;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;
import com.kdragon.other.EmailRetriever;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validate.ConfirmValidate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button _loginButton;
	private Button _regButton;
	private Button _checkButton;
	private Button _signupButton;
	private EditText _regEmailInput;
	private EditText _usernameInput;
	private EditText _passwordInput;
	private EditText _regUserInput;
	private EditText _regPassInput;
	private EditText _regFNameInput;
	private EditText _regLNameInput;
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
	List<ParseObject> ob;
	LinearLayout _view;
	Context _context;
	
	
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
			//check user input
			Validate username = new Validate(_usernameInput);
			Validate password = new Validate(_passwordInput);

			username.addValidator(new NotEmptyValidator(this)); 
			password.addValidator(new NotEmptyValidator(this)); 
			
			_loginForm.addValidates(username);
			_loginForm.addValidates(password);
			
			
	}

	@Override
	public void onClick(View v) {
		//Login old user, Register new user, or skip and test application. 
		final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Logging in.  Please wait.");
        dlg.show();
				if(v == _loginButton){
					
					if(_loginForm.validate()){
						//Toast.makeText(getApplicationContext(), "Valid Form", Toast.LENGTH_LONG).show();
						ParseUser.logInInBackground(_usernameInput.getText().toString(), _passwordInput.getText().toString(), new LogInCallback() {
							  @Override
							public void done(ParseUser user, ParseException e) {
								  dlg.dismiss();
							    if (user != null) {
							    	Log.i("login","worked");
							    	_msg = "Welcome back " + _usernameInput.getText().toString() + "!";
							    	famCheck();
							    } else {
							    	//show error msg
							    	Log.i("login",e.toString());
							    	
							    }
							  }
							});
					}
					
					
					//loginUser();
					
				}else if(v == _regButton){
					dlg.dismiss();
			setContentView(R.layout.fragment_edituser);
			_signupButton = (Button)findViewById(R.id.SignupButton);
			_signupButton.setOnClickListener(this);
_regForm = new Form();
			
			_regUserInput = (EditText)findViewById(R.id.regUsername);
			_regPassInput = (EditText)findViewById(R.id.descriptionInput);
			EditText regRePassInput = (EditText)findViewById(R.id.regRePassword);
			_regEmailInput = (EditText)findViewById(R.id.regEmail);
			_regFNameInput = (EditText)findViewById(R.id.regFirstName);
			_regLNameInput = (EditText)findViewById(R.id.regLastName);
			
			
			_regEmailInput.setText(EmailRetriever.getEmail(this));
			
			Validate regUser = new Validate(_regUserInput);
			Validate regEmail = new Validate(_regEmailInput);
			Validate regFName = new Validate(_regFNameInput);
			Validate regLName = new Validate(_regLNameInput);
			
			ConfirmValidate confirmPass = new ConfirmValidate(_regPassInput, regRePassInput);
			
			regUser.addValidator(new NotEmptyValidator(this));
			regEmail.addValidator(new NotEmptyValidator(this));
			regFName.addValidator(new NotEmptyValidator(this));
			regLName.addValidator(new NotEmptyValidator(this));
			
			
			_regForm.addValidates(regUser);
			_regForm.addValidates(regEmail);
			_regForm.addValidates(regFName);
			_regForm.addValidates(regLName);
			
			_regForm.addValidates(confirmPass);

	}else if(v == _signupButton){
			ParseUser user = new ParseUser();
			

			if(_regForm.validate()){
				user.setUsername(_regUserInput.getText().toString());
				user.setPassword(_regPassInput.getText().toString());
				user.setEmail(_regEmailInput.getText().toString());
				user.put("firstName", _regFNameInput.getText().toString());
				user.put("lastName", _regLNameInput.getText().toString());
				
				user.signUpInBackground(new SignUpCallback() {
					  @Override
					public void done(ParseException e) {
						  dlg.dismiss();
					    if (e == null) {
					    	famCheck();
					    } else {
					      // Sign up didn't succeed. Look at the ParseException
					      // to figure out what went wrong
					    	//Crouton.makeText(LoginActivity.this,  e.toString(), Style.ALERT).show();
					    }
					  }
					});
			}
			
			
			
			
		}
	}
	public void checkPass(View v){
		if(_isFamThere){
			_famName = _famNameInput.getText().toString();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("FamList");
			
			
			query.whereEqualTo("Name", _famName);
			query.findInBackground(new FindCallback<ParseObject>() {
			    public void done(List<ParseObject> famPass, ParseException e) {
			    	if(famPass.isEmpty()){
			    		_isFamThere = false;
			    		
			    		_view.setVisibility(View.VISIBLE);
			    	}else{
			    		loginUser();
			    	}
			    	
			    	
			    }
			});
			
		}else{
			loginUser();	
		}
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
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
       
        editor.putBoolean("fam_auth", true);
        editor.putString("fam_name", _famName);
        editor.commit();
		Intent returnIntent = new Intent();
		 returnIntent.putExtra("msg",_msg);
		 
		 setResult(RESULT_OK, returnIntent);    
		 finish();
	}
	
	
}
