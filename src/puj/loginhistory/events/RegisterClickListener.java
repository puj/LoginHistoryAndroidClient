package puj.loginhistory.events;

import puj.loginhistory.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;

public class RegisterClickListener extends LoginClickListener{
	
	public RegisterClickListener(Activity activity, Context context, EditText usernameField,
			EditText passwordField) {
		super(activity, context, usernameField, passwordField);
		
		this.mActivity = activity;
		
		// Construct the target URL
		mUrl = "http://"
				+ context.getString(
						R.string.default_login_hostname) + ":"
				+ context.getString(R.string.default_server_port)
				+ context.getString(R.string.default_register_path);
	}
	
	@Override
	protected void actionSuccessful(){
		AlertDialog registrationAlert = new AlertDialog.Builder(mActivity).create();
		registrationAlert.setTitle("Registration Successful");
		registrationAlert.setMessage("Press OK to return to login screen.");
		registrationAlert.setButton(AlertDialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				mActivity.finish();
			}
		});
		registrationAlert.show();
	}
	
	@Override
	protected void actionFailed(){
		AlertDialog registrationAlert = new AlertDialog.Builder(mActivity).create();
		registrationAlert.setTitle("Registration Failed");
		registrationAlert.setMessage("Press OK to try again.");
		registrationAlert.setButton(AlertDialog.BUTTON_NEGATIVE,"OK", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		registrationAlert.show();
	}
}
