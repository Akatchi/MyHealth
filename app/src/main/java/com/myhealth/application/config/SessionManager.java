package com.myhealth.application.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.myhealth.application.activities.LoginActivity;

/**
 * Created by Akatchi on 13-9-2014.
 */
public class SessionManager
{
    private SharedPreferences pref;
    private Editor editor;
    private Context _context;

    private int PRIVATE_MODE = 0;

    /**
     * The keys for the shared preferences array
     */
    private static final String PREF_NAME = "MyHealth";
    private static final String IS_LOGIN  = "IsLoggedIn";
    private static final String USERNAME  = "Username";

    /**
     * The constructor for the session manager,
     * Initializes the shared preferences.
     * @param context
     */
    public SessionManager(Context context)
    {
        this._context   = context;
        pref            = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    /**
     * createLoginSession, Sets the proper boolean for the logged in user.
     * Also puts the username into the SharedPreferences. And commits it
     *
     * @param name
     * @return void
     */
    public void createLoginSession(String name)
    {
        editor = pref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USERNAME, name);
        editor.commit();
    }

    /**
     * See if the user is logged in,
     * if not redirect back to login.
     * @return void
     */
    public void checkLogin()
    {
        if(!this.isLoggedIn())
        {
            Intent i = new Intent(_context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    /**
     * logoutUser, logs the user out, and deletes the username and the login boolean
     * so the user may log in again.
     * When username and boolean is deleted, redirect back to the login activity
     */
    public void logoutUser()
    {
        editor = pref.edit();
        editor.remove(USERNAME);
        editor.remove(IS_LOGIN);
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    /**
     * getName, gets the username from the shared preferences.
     * @return String username
     */
    public String getName() 		{ return pref.getString(USERNAME, null); }

    /**
     * isLoggedIn, gets the log in boolean for the user.
     * @return boolean is_login
     */
    public boolean isLoggedIn()		{ return pref.getBoolean(IS_LOGIN, false); 	}


}
