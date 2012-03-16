package puj.loginhistory.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import puj.loginhistory.R;

public class SessionHelper {

	/**
	 * Returns the "sessionCookie" string in SharedPreferences for the given
	 * context
	 * 
	 * @param pAppContext
	 *            the current context of the app
	 * @return the value of the "sessionCookie" field in the SharedPreferences
	 *         of the current context
	 */
	public static String getCookie(Context pAppContext) {
		if (pAppContext == null) {
			return null;
		}
		return getSharedPrefsString(pAppContext, R.string.sessionCookie);
	}

	/**
	 * Sets the "sessionCookie" string the SharedPreferences for the given
	 * context to a given value
	 * 
	 * @param pAppContext
	 *            the current context of the app
	 * @param pNewCookie
	 *            the new value to be assigned to the "sessionCookie" SharePref
	 * @return true if successful
	 */
	public static boolean setCookie(Context pAppContext, String pNewCookie) {
		// Fail if either argument is null
		if (pAppContext == null || pNewCookie == null) {
			return false;
		}

		return setSharedPrefsString(pAppContext, R.string.sessionCookie,
				pNewCookie);
	}

	public static boolean clearCookie(Context pAppContext) {
		if (pAppContext == null) {
			return false;
		}

		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext,
				R.string.shared_preferences_filename);

		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(
				sharedPrefString, Context.MODE_PRIVATE);

		// Start editing
		Editor editor = sharedPrefs.edit();

		// Get the name of the sessionCookie in the SharedPrefs
		String sessionCookieStringName = getResourceString(pAppContext,
				R.string.sessionCookie);

		Log.i("LoginHistory", "Trying to remove sessionCookie : "
				+ getSharedPrefsString(pAppContext, R.string.sessionCookie));

		// Remove/Clear the cookie from the SharedPrefs
		editor.remove(sessionCookieStringName);

		// Commit
		editor.commit();

		// Success!!!!
		return true;
	}

	/**
	 * Sets a boolean in the SharedPrefs for the given context
	 * 
	 * @param pAppContext
	 *            pAppContext The current context of the app
	 * @param pBooleanNameResourceId
	 *            The resource ID for the name of the target Boolean in the
	 *            SharedPrefs to change
	 * @param pBooleanValue
	 *            The value to be assigned in the SharedPrefs for the identified
	 *            boolean
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsBoolean(Context pAppContext,
			int pBooleanNameResourceId, boolean pBooleanValue) {
		// Convert the resource identity to a string
		String sharedPrefsStringName = getResourceString(pAppContext,
				pBooleanNameResourceId);

		return setSharedPrefsBoolean(pAppContext, sharedPrefsStringName,
				pBooleanValue);
	}

	/**
	 * Sets a string in the SharedPrefs for the given context
	 * 
	 * @param pAppContext
	 *            pAppContext The current context of the app
	 * @param pBooleanName
	 *            The name of the target boolean in SharedPrefs to change
	 * @param pBooleanValue
	 *            The value to be assigned in the SharedPrefs for the identified
	 *            boolean
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsBoolean(Context pAppContext,
			String pBooleanName, boolean pBooleanValue) {
		if (pAppContext == null || pBooleanName == null) {
			// It is arguable if we will allow pStringValue to be null here or
			// if we
			// force the usage of SharedPrefs.remove()
			return false;
		}

		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext,
				R.string.shared_preferences_filename);

		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(
				sharedPrefString, Context.MODE_PRIVATE);

		// Start editing
		Editor editor = sharedPrefs.edit();

		// Change the value
		editor.putBoolean(pBooleanName, pBooleanValue);

		// Commit the changes to the SharedPrefs
		editor.commit();

		return true;
	}

	/**
	 * Sets a string in the SharedPrefs for the given context
	 * 
	 * @param pAppContext
	 *            pAppContext The current context of the app
	 * @param pStringNameResourceId
	 *            The resource ID for the name of the target String in the
	 *            SharedPrefs to change
	 * @param pStringValue
	 *            The value to be assigned in the SharedPrefs for the identified
	 *            string
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsString(Context pAppContext,
			int pStringNameResourceId, String pStringValue) {
		// Convert the resource identity to a string
		String sharedPrefsStringName = getResourceString(pAppContext,
				pStringNameResourceId);

		return setSharedPrefsString(pAppContext, sharedPrefsStringName,
				pStringValue);
	}

	/**
	 * Sets a string in the SharedPrefs for the given context
	 * 
	 * @param pAppContext
	 *            pAppContext The current context of the app
	 * @param pStringName
	 *            The name of the target string in SharedPrefs to change
	 * @param pStringValue
	 *            The value to be assigned in the SharedPrefs for the identified
	 *            string
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsString(Context pAppContext,
			String pStringName, String pStringValue) {
		if (pAppContext == null || pStringName == null || pStringValue == null) {
			// It is arguable if we will allow pStringValue to be null here or
			// if we
			// force the usage of SharedPrefs.remove()
			return false;
		}

		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext,
				R.string.shared_preferences_filename);

		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(
				sharedPrefString, Context.MODE_PRIVATE);

		// Start editing
		Editor editor = sharedPrefs.edit();

		// Change the value
		editor.putString(pStringName, pStringValue);

		// Commit the changes to the SharedPrefs
		editor.commit();

		return true;
	}

	/**
	 * This method allows passing resource identifiers instead of boolean
	 * literals as names of preferences to retrieve
	 * 
	 * @param pAppContext
	 *            The current context of the app
	 * @param pResourceBooleanNameId
	 *            The resource id of the string name of the target boolean
	 * @return the value of the boolean in the generated resources of the
	 *         application
	 */
	public static boolean getSharedPrefsBoolean(Context pAppContext,
			int pResourceBooleanNameId) {
		// Convert the resource identity to a string
		String sharedPrefsBooleanName = getResourceString(pAppContext,
				pResourceBooleanNameId);

		return getSharedPrefsBoolean(pAppContext, sharedPrefsBooleanName);
	}

	/**
	 * Returns the {@link SharedPreferences} string identified by the
	 * pStringName argument in the given context
	 * 
	 * @param pAppContext
	 *            The current context of the app
	 * @param pStringName
	 *            The string name of the target string
	 * @return the value of the string in SharedPreferences within the current
	 *         Context
	 */
	public static boolean getSharedPrefsBoolean(Context pAppContext,
			String pBooleanName) {
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext,
				R.string.shared_preferences_filename);

		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(
				sharedPrefString, Context.MODE_PRIVATE);

		// Grab the string we are looking for
		return sharedPrefs.getBoolean(pBooleanName, false);
	}

	/**
	 * This method allows passing resource identifiers instead of string
	 * literals as names of preferences to retrieve
	 * 
	 * @param pAppContext
	 *            The current context of the app
	 * @param pResourceStringNameId
	 *            The resource id of the string name of the target string
	 * @return the value of the string in the generated resources of the
	 *         application
	 */
	public static String getSharedPrefsString(Context pAppContext,
			int pResourceStringNameId) {
		// Convert the resource identity to a string
		String sharedPrefsStringName = getResourceString(pAppContext,
				pResourceStringNameId);

		return getSharedPrefsString(pAppContext, sharedPrefsStringName);
	}

	/**
	 * Returns the {@link SharedPreferences} string identified by the
	 * pStringName argument in the given context
	 * 
	 * @param pAppContext
	 *            The current context of the app
	 * @param pStringName
	 *            The string name of the target string
	 * @return the value of the string in SharedPreferences within the current
	 *         Context
	 */
	public static String getSharedPrefsString(Context pAppContext,
			String pStringName) {
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext,
				R.string.shared_preferences_filename);

		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(
				sharedPrefString, Context.MODE_PRIVATE);

		// Grab the string we are looking for
		return sharedPrefs.getString(pStringName, null);
	}

	/**
	 * Returns the value of the resource string identified by the resource ID in
	 * the context the given context
	 * 
	 * @param pAppContext
	 *            The current context of the app
	 * @param pResourceId
	 *            The resource ID of the target string
	 * @return the value of the string defined by the pResourceId
	 */
	public static String getResourceString(Context pAppContext, int pResourceId) {

		return pAppContext.getString(pResourceId);
	}

	/**
	 * This will create a new {@link HttpPost} object and return it in the
	 * current context of the application
	 * 
	 * @param url
	 *            the Target of the POST request
	 * @param context
	 *            the application context to retrieve the cookie from
	 * @return A new HttpPost object with the correct headers and content types
	 * @throws UnsupportedEncodingException
	 *             The JSON object could not use UTF8 encoding
	 * @throws JSONException
	 *             Could not create JSON object from the sessionCookie
	 */
	public static HttpPost getNewHttpPost(String url, Context context)
			throws UnsupportedEncodingException, JSONException {
		// Initialize the JSON object
		JSONObject jsonObj = new JSONObject();

		// Add the sessionCookie from the context
		jsonObj.put("sessionCookie", SessionHelper.getCookie(context));

		// Put the JSON object in the Request object
		StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
		entity.setContentType("application/json");

		// Create JSON POST object with arguments
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setEntity(entity);

		return httpPost;
	}

	/**
	 * This will create a new {@link HttpPost} object with the JSON object
	 * argument as the content
	 * 
	 * @param jsonObject
	 *            A pre-created JSON object to send in the request
	 * @param url
	 *            the Target of the POST request
	 * @param context
	 *            the application context to retrieve the cookie from
	 * @return A new HttpPost object with the correct headers and content types
	 * @throws UnsupportedEncodingException
	 *             The JSON object could not use UTF8 encoding
	 * @throws JSONException
	 *             Could not create JSON object from the sessionCookie
	 */
	public static HttpPost getNewHttpPost(String url, JSONObject jsonObject)
			throws UnsupportedEncodingException, JSONException {
		// Put the JSON object in the Request object
		StringEntity entity = new StringEntity(jsonObject.toString(),
				HTTP.UTF_8);
		entity.setContentType("application/json");

		// Create JSON POST object with arguments
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setEntity(entity);

		return httpPost;
	}

	/**
	 * This will return the String representation of the content of the response
	 * 
	 * @param response
	 *            A completed response from the {@link HttpClient}
	 * @return string representation of response content
	 * @throws IllegalStateException
	 *             Response seems not to be complete
	 * @throws IOException
	 *             Cannot read the stream of the response
	 */
	public static String parseResponseContent(HttpResponse response)
			throws IllegalStateException, IOException {
		// Retrieve the stream from the content of the response
		InputStream is = response.getEntity().getContent();

		// Buffer the stream
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"iso-8859-1"), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;

		// Build the string from the stream
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		is.close();

		return sb.toString();
	}

	/**
	 * Returns the password has using a special salt
	 * 
	 * @param username
	 *            the user specified username
	 * @param password
	 *            the user specified password
	 * @return SHA512 encoded password string to be used in conjunction with the
	 *         server requests
	 */
	public static String getPasswordHash(String username, String password) {
		// Setup the password encoding arguments
		final String salt = "09234j234kj!@#213lk$#$)(*)DFSDFL##$";
		final String passwordAndSalt = username + password + salt;

		// Encode the password with base64 and salt
		String encodedPassword = null;
		try {

			// Choose SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-512");

			// Prepare the digest
			md.update(passwordAndSalt.getBytes("UTF-8"));

			// Retrieve the encoded password string
			encodedPassword = new String(Hex.encodeHex(md.digest()));
		} catch (Exception e) {
			Log.e("LoginHistory", e.getMessage());
		}

		return encodedPassword;
	}

}
