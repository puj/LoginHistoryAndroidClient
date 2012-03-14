package puj.loginhistory;

import java.net.URL;
import java.util.Properties;

import org.apache.http.HttpConnection;

import puj.loginhistory.events.LoginClickListener;
import puj.loginhistory.helpers.SessionHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginHistoryActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onStart() {
		/*
		 * Choose the correct layout based on three different cases 1. A user
		 * has never logged in 2. A user does not have a session active 3. A
		 * user has logged in and has an active session
		 */
		
		Context currentContext = getApplicationContext();
		
		String currentSessionCookie = SessionHelper.getCookie(currentContext);
		boolean isReturningUser = SessionHelper.getSharedPrefsBoolean(currentContext, R.string.isReturningUser);

		if (currentSessionCookie == null) {
			setContentView(R.layout.login_area);

			Button submitButton = ((Button) findViewById(R.id.btnSubmit));
			EditText usernameField = ((EditText) findViewById(R.id.txtUsername));
			EditText passwordField = ((EditText) findViewById(R.id.txtPassword));
			submitButton.setOnClickListener(new LoginClickListener(
					usernameField, passwordField));
		} else {
			setContentView(R.layout.main);
		}

		super.onStart();
	}

	@Override
	public void onDestroy() {
		SessionHelper.clearCookie(getApplicationContext());
		super.onDestroy();
	}

}