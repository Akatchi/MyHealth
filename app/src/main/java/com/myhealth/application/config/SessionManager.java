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

    private static final String PREF_NAME = "MyHealth";
    //Keys die in de sessie worden opgeslagen
    private static final String IS_LOGIN  = "IsLoggedIn";
    private static final String USERNAME  = "Username";

    public SessionManager(Context context)
    {
        this._context   = context;
        pref            = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    //Maak een login sessie aan met de ingevulde username
    public void createLoginSession(String name)
    {
        editor = pref.edit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USERNAME, name);
        editor.commit();
    }

    //Kijk of de user ingelogd is, zoniet stuur hem terug naar de login pagina
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

    //Log de user uit, verwijder de username + login boolean zodat er opnieuw ingelogd kan worden
    //Nadat de login gegevens verwijdert zijn, wordt de user teruggestuurd naar de login activity
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

    //Haal de keys op om in de app weer te geven of om te kijken wat er op het huidige moment instaat
    public String getUsername() 	{ return pref.getString(USERNAME, null); }
    public boolean isLoggedIn()		{ return pref.getBoolean(IS_LOGIN, false); 	}


}
