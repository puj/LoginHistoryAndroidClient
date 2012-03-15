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

import puj.loginhistory.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

	public LoginClickListener(Context context, EditText usernameField,
			EditText passwordField) {
		mUsernameField = usernameField;
		mPasswordField = passwordField;

		// Construct the target URL
		mUrl = "http://" + context.getString(R.string.default_login_hostname)
				+ ":" + context.getString(R.string.default_server_port)
				+ context.getString(R.string.default_login_path);
	}

	public void onClick(View view) {

		// Get the username
		final String username = mUsernameField.getText().toString();

		// Setup the password encoding arguments
		final String salt = "09234j234kj!@#213lk$#$)(*)DFSDFL##$";
		final String passwordAndSalt = username
				+ mPasswordField.getText().toString() + salt;

		// Encode the password with base64 and salt
		String encodedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(passwordAndSalt.getBytes("UTF-8"));
			encodedPassword = new String(Hex.encodeHex(md.digest()));
		} catch (NoSuchAlgorithmException e) {
			Log.e("LoginHistory", e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Log.e("LoginHistory", e.getMessage());
		}

		// Create the JSON credentials
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("username", username);
			jsonObj.put("pwHash", encodedPassword);

			// Add your data
			StringEntity entity = new StringEntity(jsonObj.toString(),
					HTTP.UTF_8);
			entity.setContentType("application/json");

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(mUrl);

			Log.i("LoginHistory", "Trying to post to : " + mUrl);

			httppost.setHeader("Content-Type", "application/json");
			httppost.setEntity(entity);

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			// Parse the response
			InputStream is = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			// Store the session cookie
			String sessionCookie = sb.toString();
			SharedPreferences prefs = view.getContext().getSharedPreferences(
					view.getContext().getString(
							R.string.shared_preferences_filename),
					Context.MODE_PRIVATE);

			Editor prefsEditor = prefs.edit();
			prefsEditor.putString("sessionCookie", sessionCookie);
			prefsEditor.commit();

			Log.i("LogHistory", sb.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e("LoginHistory", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("LoginHistory", e.getMessage());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("LoginHistory", e.getMessage());
		}

	}

}
