package puj.loginhistory.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import puj.loginhistory.R;

public class SessionHelper {
	
	/**
	 * Returns the "sessionCookie" string in SharedPreferences for the given context 
	 * @param pAppContext the current context of the app
	 * @return the value of the  "sessionCookie" field in the SharedPreferences of the current context 
	 */
	public static String getCookie(Context pAppContext){
		if(pAppContext == null){
			return null;
		}
		return getSharedPrefsString(pAppContext, R.string.sessionCookie);
	}
	
	/**
	 * Sets the "sessionCookie" string the SharedPreferences for the given context to a given value
	 * @param pAppContext the current context of the app
	 * @param pNewCookie the new value to be assigned to the "sessionCookie" SharePref
	 * @return true if successful
	 */
	public static boolean setCookie(Context pAppContext, String pNewCookie){
		// Fail if either argument is null
		if(pAppContext == null || pNewCookie == null){
			return false;
		}
		
		return setSharedPrefsString(pAppContext, R.string.sessionCookie, pNewCookie);
	}
	
	public static boolean clearCookie(Context pAppContext){
		if(pAppContext == null ){
			return false;
		}
		
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext, R.string.shared_preferences_filename);
		
		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(sharedPrefString,Context.MODE_PRIVATE);
		
		// Start editing
		Editor editor = sharedPrefs.edit();
		
		// Get the name of the sessionCookie in the SharedPrefs
		String sessionCookieStringName = getResourceString(pAppContext, R.string.sessionCookie);
		
		
		Log.i("LoginHistory", "Trying to remove sessionCookie : " + getSharedPrefsString(pAppContext, R.string.sessionCookie));
		
		// Remove/Clear the cookie from the SharedPrefs
		editor.remove(sessionCookieStringName);
		
		// Commit
		editor.commit();
		
		// Success!!!!
		return true;
	}
	
	
	/**
	 * Sets a boolean in the SharedPrefs for the given context
	 * @param pAppContext pAppContext The current context of the app
	 * @param pBooleanNameResourceId The resource ID for the name of the target Boolean in the SharedPrefs to change
	 * @param pBooleanValue The value to be assigned in the SharedPrefs for the identified boolean
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsBoolean(Context pAppContext, int pBooleanNameResourceId, boolean pBooleanValue ){
		// Convert the resource identity to a string 
		String sharedPrefsStringName = getResourceString(pAppContext, pBooleanNameResourceId);
	
		return setSharedPrefsBoolean(pAppContext, sharedPrefsStringName, pBooleanValue);
	}
	
	/**
	 * Sets a string in the SharedPrefs for the given context
	 * @param pAppContext pAppContext The current context of the app
	 * @param pBooleanName The name of the target boolean in SharedPrefs to change
	 * @param pBooleanValue The value to be assigned in the SharedPrefs for the identified boolean
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsBoolean(Context pAppContext, String pBooleanName, boolean pBooleanValue ){
		if(pAppContext == null || pBooleanName == null  ){
			// It is arguable if we will allow pStringValue to be null here or if we
			//  force the usage of SharedPrefs.remove()
			return false;
		}
		
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext, R.string.shared_preferences_filename);
		
		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(sharedPrefString,Context.MODE_PRIVATE);
		
		// Start editing
		Editor editor = sharedPrefs.edit();
		
		// Change the value
		editor.putBoolean(pBooleanName, pBooleanValue);
		
		//Commit the changes to the SharedPrefs
		editor.commit();
		
		return true;
	}
	
	/**
	 * Sets a string in the SharedPrefs for the given context
	 * @param pAppContext pAppContext The current context of the app
	 * @param pStringNameResourceId The resource ID for the name of the target String in the SharedPrefs to change
	 * @param pStringValue The value to be assigned in the SharedPrefs for the identified string
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsString(Context pAppContext, int pStringNameResourceId, String pStringValue ){
		// Convert the resource identity to a string 
		String sharedPrefsStringName = getResourceString(pAppContext, pStringNameResourceId);
	
		return setSharedPrefsString(pAppContext, sharedPrefsStringName, pStringValue);
	}
	
	/**
	 * Sets a string in the SharedPrefs for the given context
	 * @param pAppContext pAppContext The current context of the app
	 * @param pStringName The name of the target string in SharedPrefs to change
	 * @param pStringValue The value to be assigned in the SharedPrefs for the identified string
	 * @return true if the commit succeeds, false if any argument is null
	 */
	public static boolean setSharedPrefsString(Context pAppContext, String pStringName, String pStringValue ){
		if(pAppContext == null || pStringName == null  || pStringValue == null){
			// It is arguable if we will allow pStringValue to be null here or if we
			//  force the usage of SharedPrefs.remove()
			return false;
		}
		
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext, R.string.shared_preferences_filename);
		
		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(sharedPrefString,Context.MODE_PRIVATE);
		
		// Start editing
		Editor editor = sharedPrefs.edit();
		
		// Change the value
		editor.putString(pStringName, pStringValue);
		
		//Commit the changes to the SharedPrefs
		editor.commit();
		
		return true;
	}
	
	
	
	/**
	 * This method allows passing resource identifiers instead of boolean literals as names of preferences to retrieve
	 * @param pAppContext The current context of the app
	 * @param pResourceBooleanNameId The resource id of the string name of the target boolean
	 * @return the value of the boolean in the generated resources of the application
	 */
	public static boolean getSharedPrefsBoolean(Context pAppContext, int pResourceBooleanNameId ){
		// Convert the resource identity to a string 
		String sharedPrefsBooleanName = getResourceString(pAppContext, pResourceBooleanNameId);
		
		return  getSharedPrefsBoolean(pAppContext, sharedPrefsBooleanName);
	}
	
	/**
	 * Returns the {@link SharedPreferences} string identified by the pStringName argument in the given context
	 * @param pAppContext The current context of the app
	 * @param pStringName The string name of the target string
	 * @return the value of the string in SharedPreferences within the current Context
	 */
	public static boolean getSharedPrefsBoolean(Context pAppContext, String pBooleanName){
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext, R.string.shared_preferences_filename);
		
		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(sharedPrefString,Context.MODE_PRIVATE);
		
		// Grab the string we are looking for
		return sharedPrefs.getBoolean(pBooleanName, false);
	}
	
	
	
	/**
	 * This method allows passing resource identifiers instead of string literals as names of preferences to retrieve
	 * @param pAppContext The current context of the app
	 * @param pResourceStringNameId The resource id of the string name of the target string
	 * @return the value of the string in the generated resources of the application
	 */
	public static String getSharedPrefsString(Context pAppContext, int pResourceStringNameId ){
		// Convert the resource identity to a string 
		String sharedPrefsStringName = getResourceString(pAppContext, pResourceStringNameId);
		
		return  getSharedPrefsString(pAppContext, sharedPrefsStringName);
	}
	
	/**
	 * Returns the {@link SharedPreferences} string identified by the pStringName argument in the given context
	 * @param pAppContext The current context of the app
	 * @param pStringName The string name of the target string
	 * @return the value of the string in SharedPreferences within the current Context
	 */
	public static String getSharedPrefsString(Context pAppContext, String pStringName){
		// Get the shared preferences instance for our app
		String sharedPrefString = getResourceString(pAppContext, R.string.shared_preferences_filename);
		
		// Get the actual preferences
		SharedPreferences sharedPrefs = pAppContext.getSharedPreferences(sharedPrefString,Context.MODE_PRIVATE);
		
		// Grab the string we are looking for
		return sharedPrefs.getString(pStringName, null);
	}
	
	/**
	 * Returns the value of the resource string identified by the resource ID in the context the given context
	 * @param pAppContext The current context of the app
	 * @param pResourceId The resource ID of the target string
	 * @return the value of the string defined by the pResourceId
	 */
	public static String getResourceString(Context pAppContext, int pResourceId){
		
		return pAppContext.getString(pResourceId);
	}
	
	
}
