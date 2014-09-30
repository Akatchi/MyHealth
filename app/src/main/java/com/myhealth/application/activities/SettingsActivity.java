package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.myhealth.application.R;
import com.myhealth.application.asynctasks.GetProfileTask;
import com.myhealth.application.asynctasks.SaveProfileTask;
import com.myhealth.application.asynctasks.UserObject;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;

import java.util.Locale;

public class SettingsActivity extends Activity
{
    private EditText        mFirstname, mLastname;
    private SessionManager  session;
    private Spinner         mTaalVoorkeur;
    private String          savedLanguage;
    private int             PICK_IMAGE = 1;
    private String          lang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        mFirstname = (EditText) findViewById(R.id.editText);
        mLastname  = (EditText) findViewById(R.id.editText2);

        try
        {
            //Profiel data ophalen en invullen in de textvelden 
            UserObject userData = new GetProfileTask(session.getToken(), session.getUsername()).execute(new String[]{Variables.GETPROFILEDATA}).get();

            if( userData.getCode() == Variables.CODE_SUCCESS )
            {
                mFirstname.setText(userData.getFirstname());
                mLastname.setText(userData.getLastname());
            }
            else if( userData.getCode() == Variables.CODE_TOKEN_EXPIRED )
            {
                session.logoutUser();
            }
        }
        catch( Exception e ){ e.printStackTrace(); }

        mTaalVoorkeur = (Spinner) findViewById(R.id.spinLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaalVoorkeur.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(session.getLanguage());

        mTaalVoorkeur.setSelection(spinnerPosition);

        mTaalVoorkeur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                savedLanguage = parent.getItemAtPosition(pos).toString();

                lang = "";
                if( savedLanguage.equals(getResources().getString(R.string.Nederlands)) ) { lang = "nl"; }
                else if( savedLanguage.equals(getResources().getString(R.string.Engels)) ){ lang = "en"; }

            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                // Do nothing, just another required interface callback
            }

        });

    }

    public void saveSettings(View v)
    {
        try
        {
            //Code to update the languages
            session.setLanguage(savedLanguage);

            Locale myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            String firstname = mFirstname.getText().toString();
            String lastname = mLastname.getText().toString();

            int code = new SaveProfileTask(session.getToken(), session.getUsername(), firstname, lastname).execute(new String[]{Variables.SAVEPROFILEDATA}).get();

            if (code == Variables.CODE_SUCCESS)
            {
                Toast.makeText(getApplicationContext(), R.string.profile_save_success, Toast.LENGTH_SHORT).show();
                Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMain);
            } else if (code == Variables.CODE_TOKEN_EXPIRED)
            {
                Toast.makeText(getApplicationContext(), R.string.token_expired, Toast.LENGTH_SHORT).show();
                session.logoutUser();
            }
        }
        catch( Exception e ){ e.printStackTrace(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch( item.getItemId() )
        {
            case android.R.id.home:
                //Als er op de bluetooth zoek knop gedruk wordt moet er gezocht worden naar een bluetooth device
                Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
