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
		protected void onPreExecute() {
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.pgrProgress);
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected void onPostExecute(JSONObject results) {
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.pgrProgress);
			progressBar.setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected JSONObject doInBackground(Void... voids) {
			publishProgress(true);

			Context context = getApplicationContext();

			String url = "http://"
					+ context.getString(R.string.default_login_hostname) + ":"
					+ context.getString(R.string.default_server_port)
					+ context.getString(R.string.default_get_logins_path);

			// Create the JSON credentials
			try {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("sessionCookie", SessionHelper.getCookie(context));

				// Add your data
				StringEntity entity = new StringEntity(jsonObj.toString(),
						HTTP.UTF_8);
				entity.setContentType("application/json");

				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				Log.i("LoginHistory", "Trying to post to : " + url);

				httppost.setHeader("Content-Type", "application/json");
				httppost.setEntity(entity);

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				// Parse the response
				InputStream is = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				Log.i("LogHistory", "Login list : " +sb.toString());

				
				JSONObject results = new JSONObject(sb.toString());
				return results;
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("LoginHistory", e.getMessage());
			}

			return null;

		}

	}
}
