package puj.loginhistory;

import puj.loginhistory.events.LoginClickListener;
import puj.loginhistory.events.RegisterClickListener;
import puj.loginhistory.helpers.SessionHelper;
import android.app.ActivityGroup;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


public class LoginHistoryRegisterActivity extends ActivityGroup {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("LoginHistory","Trying to start activity");
		super.onCreate(savedInstanceState);
		
		Context currentContext = getApplicationContext();
		
		setContentView(R.layout.register_area);
		
		// Setup login button
		Button registerButton = ((Button) findViewById(R.id.btnRegister));
		EditText usernameField = ((EditText) findViewById(R.id.txtUsername));
		EditText passwordField = ((EditText) findViewById(R.id.txtPassword));
		registerButton.setOnClickListener(new RegisterClickListener(currentContext,
				usernameField, passwordField));
	}
	
	@Override 
	public void onStop(){
		finish();
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		SessionHelper.clearCookie(getApplicationContext());
		finish();
		super.onDestroy();
	}
}
