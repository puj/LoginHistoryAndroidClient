package puj.loginhistory.events;

import puj.loginhistory.R;
import android.content.Context;
import android.widget.EditText;

public class RegisterClickListener extends LoginClickListener{

	public RegisterClickListener(Context context, EditText usernameField,
			EditText passwordField) {
		super(context, usernameField, passwordField);
		
		// Construct the target URL
		mUrl = "http://"
				+ context.getString(
						R.string.default_login_hostname) + ":"
				+ context.getString(R.string.default_server_port)
				+ context.getString(R.string.default_register_path);
	}
	
}
