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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.kdragon.other.EmailRetriever;
import com.kdragon.other.WebInterface;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity implements OnClickListener{
	
	private Button _loginButton;
	private Button _regButton;
	private Button _skipButton;
	private EditText _regEmailInput;
	private EditText _usernameInput;
	private EditText _passwordInput;
	private EditText _regUserInput;
	private EditText _regPassInput;
	private EditText _regFNameInput;
	private EditText _regLNameInput;
	private EditText _regWeightInput;
	
	private String _msg;
	
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_login);
			
			
			
	}

	@Override
	public void onClick(View v) {
		
		
		//Login old user, Register new user, or skip and test application. 
		
		
	}
	//go back to main page
	private void loginUser(){
		Intent returnIntent = new Intent();
		 returnIntent.putExtra("msg",_msg);
		 setResult(RESULT_OK, returnIntent);    
		 finish();
	}
	
	 
}
