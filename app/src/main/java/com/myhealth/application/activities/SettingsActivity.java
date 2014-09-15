package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.myhealth.application.R;
import com.myhealth.application.config.SessionManager;

public class SettingsActivity extends Activity
{
    private EditText        mUsername;
    private SessionManager  session;
    private Spinner         mTaalVoorkeur;
    private String          savedLanguage, avatarUri;
    private ImageView       mAvatar;
    private int             PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new SessionManager(getApplicationContext());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        mUsername = (EditText) findViewById(R.id.inputUsername);
        mUsername.setText(session.getUsername());

        mTaalVoorkeur = (Spinner) findViewById(R.id.spinLanguage);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaalVoorkeur.setAdapter(adapter);

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
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
                // Do nothing, just another required interface callback
            }

        });

        mAvatar = (ImageView) findViewById(R.id.avatar);
        mAvatar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PICK_IMAGE && data != null && data.getData() != null)
        {
            Uri _uri = data.getData();

            avatarUri = _uri.toString();
            mAvatar.setImageURI(_uri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveSettings(View v)
    {
        String username = mUsername.getText().toString();
        Toast.makeText(getApplicationContext(), "Username: " + username + "\n" + "Language: " + savedLanguage + "\n" + "Uri: " + avatarUri, Toast.LENGTH_SHORT).show();
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
