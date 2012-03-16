package puj.loginhistory.events;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import puj.loginhistory.LoginHistoryActivity;
import puj.loginhistory.LoginHistoryLoggedInActivity;
import puj.loginhistory.LoginHistoryRegisterActivity;
import puj.loginhistory.R;
import puj.loginhistory.helpers.SessionHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginClickListener implements android.view.View.OnClickListener {
	/**
	 * A reference to the text field containing the username
	 */
	private EditText mUsernameField;

	/**
	 * A reference to the text field containing the password
	 */
	private EditText mPasswordField;

	/**
	 * The request target, will be used for register later
	 */
	protected String mUrl;

	/**
	 * Reference to the parent activity
	 */
	protected Activity mActivity;
	
	public LoginClickListener(Activity activity, Context context, EditText usernameField,
			EditText passwordField) {
		mUsernameField = usernameField;
		mPasswordField = passwordField;
		mActivity = activity;
		
		
		// Construct the target URL
		mUrl = "http://" + context.getString(R.string.default_login_hostname)
				+ ":" + context.getString(R.string.default_server_port)
				+ context.getString(R.string.default_login_path);
	}

	public void onClick(View view) {
		new LoginRequest().doInBackground(view);
	}
	
	protected class LoginRequest extends AsyncTask<View,Boolean,Void>{
		@Override
		protected void onPreExecute(){
			ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.pgrProgress);
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}
		
		@Override
		protected void onPostExecute(Void nothing){
			ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.pgrProgress);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}
		
		
		@Override
		protected Void doInBackground(View... views) {
			View view = views[0];
			publishProgress(true);
			
			// Retrieve the encoded password 
			final String username = mUsernameField.getText().toString();
			final String password = mPasswordField.getText().toString();
			String encodedPassword = SessionHelper.getPasswordHash(username,password);

			

			// Create the JSON credentials
			try {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("username", username);
				jsonObj.put("pwHash", encodedPassword);

				// Get the POST request object from the url and the JSON object 
				HttpPost httppost = SessionHelper.getNewHttpPost(mUrl,jsonObj);
				
				// Execute HTTP Post Request
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(httppost);

				// Get the results of the POST Request
				String resultString = SessionHelper.parseResponseContent(response);

				Log.i("LogHistory", resultString);
				
				// Parse the results into JSON
				JSONObject results = new JSONObject(resultString);
				
				// Store the session cookie, will only happen for logins, not register
				if(results.optString("sessionCookie").compareTo("") != 0){
					
					// Get the share preferences for the application
					SharedPreferences prefs = view.getContext().getSharedPreferences(
						view.getContext().getString(
								R.string.shared_preferences_filename),
						Context.MODE_PRIVATE);
				
					// Overwrite the session cookie in SharePreferences
					Editor prefsEditor = prefs.edit();
					prefsEditor.putString("sessionCookie", results.getString("sessionCookie"));
					prefsEditor.commit();
				}
				
				// Do the class-specific follow-up action
				if(results.getString("result").compareTo("success") == 0){
					actionSuccessful();
				}else{
					actionFailed();
				}
				
			} catch (ClientProtocolException e) {
				Log.e("LoginHistory", e.getMessage());
			} catch (IOException e) {
				// Find and change the label on the layout
				TextView statusLabel = (TextView)mActivity.findViewById(R.id.lblStatus);
				statusLabel.setText("Connection to server failed.  Do you have internet?");
				
				Log.e("LoginHistory", e.getMessage());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("LoginHistory", e.getMessage());
			}
			
			return null;
		}
		
	}
	
	/**
	 * We have successfully logged in, we should navigate to our LoginHistory page
	 */
	protected void actionSuccessful(){
		// Start the LoginHistory activity
		Intent intent = new Intent(mActivity.getApplicationContext(), LoginHistoryLoggedInActivity.class);
		mActivity.startActivity(intent);
		
		// Close down the login activity
		mActivity.finish();
	}
	
	/**
	 * Logged was failed, show a message for the user to indicate this.
	 */
	protected void actionFailed(){
		// Find and change the label on the layout
		TextView statusLabel = (TextView)mActivity.findViewById(R.id.lblStatus);
		statusLabel.setText("Login failed, please try again...");
	}

}
