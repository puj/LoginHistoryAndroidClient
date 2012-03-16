package puj.loginhistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import puj.loginhistory.helpers.SessionHelper;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class LoginHistoryLoggedInActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context currentContext = getApplicationContext();

		setContentView(R.layout.loggedin_area);

		ListView loginListView = (ListView) findViewById(R.id.lstLogins);

		JSONObject results = new GetLoginsRequest().doInBackground();

		if(results != null){
			try {
				JSONArray loginsArray = results.getJSONArray("logins");
				String[] logins = new String[loginsArray.length()];
				for (int i = 0; i < loginsArray.length(); i++) {
					int newIndex = loginsArray.length()-1 - i;
					logins[newIndex] = (newIndex+1) + " : " +new Date( Long.parseLong(loginsArray.getString(i))).toString();
				}
				
				

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, logins);
				
				loginListView.setAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onDestroy() {
		SessionHelper.clearCookie(getApplicationContext());
		super.onDestroy();
	}

	protected class GetLoginsRequest extends
			AsyncTask<Void, Boolean, JSONObject> {
		
		@Override
		protected JSONObject doInBackground(Void... voids) {
			Context context = getApplicationContext();

			// Build the path to get the logins
			String url = "http://"
					+ context.getString(R.string.default_login_hostname) + ":"
					+ context.getString(R.string.default_server_port)
					+ context.getString(R.string.default_get_logins_path);

			try {
				// Get a new post object to the url with the current context cookie
				HttpPost httppost = SessionHelper.getNewHttpPost(url,context);
				
				// Create a new HttpClient and Post Header
				HttpClient httpClient = new DefaultHttpClient();

				// Execute HTTP Post Request
				HttpResponse response = httpClient.execute(httppost);

				// Parse the response
				String stringResponse = SessionHelper.parseResponseContent(response);
				
				Log.i("LogHistory", "Login list : " + stringResponse);
				
				// Convert the string response into a JSON object
				//  This should be of the form {logins : [...]?}
				JSONObject results = new JSONObject(stringResponse);
				return results;
				

			} catch (Exception e) {
				Log.e("LoginHistory", e.getMessage());
			}

			return null;

		}

	}
}
