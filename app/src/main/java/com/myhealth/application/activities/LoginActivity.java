package com.myhealth.application.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.myhealth.application.R;
import com.myhealth.application.asynctasks.LoginTask;
import com.myhealth.application.asynctasks.ResponseObject;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;

public class LoginActivity extends Activity
{
    private EditText        mUsername, mPassword;
    private SessionManager  session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session   = new SessionManager(getApplicationContext());

        //Initializeer de EditTexts die gebruikt worden om de login gegevens in te vullen
        mUsername = (EditText) findViewById(R.id.field_emailaddress);
        mPassword = (EditText) findViewById(R.id.field_password);

        mUsername.setText("Test@test.nl");
        mPassword.setText("test");
    }

    //Functie zit in een onClick van de button in de XML layout
    public void loginUser(View v)
    {
        //Haal de ingevulde text op uit de login velden en kijk of ze geldig zijn ( niet 0 )
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        //Kijk of de ingevulde velden geldig zijn ingevuld zoniet geef een error message weer
        String errorMessage = "";
        if( username.equals("") )       { errorMessage += "Please enter your username \n"; }
        if( password.equals("") )       { errorMessage += "Please enter your password \n"; }
        if( !errorMessage.equals("") )  { Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show(); }
        else //Als er dus geen error gevonden is
        {
            try
            {
                //Hier wordt een AsyncTask aangeroepen om het inlog request af te handelen
                //De asynctask returnt een ResponseObject waar een message en een code instaat om te kijken of de login goed was
                ResponseObject response = new LoginTask(username, password).execute(new String[]{Variables.LOGINURL}).get();

                //Tot slot wordt er een message weergegeven wat de response van de server was ( bv. login gegevens ongeldig / login succesvol )
//                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();

                //Als de return code success is start dan een intent die de MainActivity opent
                if( response.getCode() == Variables.CODE_SUCCESS )
                {
                    //Roep de methode login user aan die het opstarten van de mainactivity en het opslaan van de logged in state afhandelt
                    Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                    loginUser(username, response.getToken());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){ e.printStackTrace(); }
        }
    }

    private void loginUser(String username, String token)
    {
        //Sla de ingelogde username op in de sessionmanager en zet de logged in status op true
        session.createLoginSession(username, token);

        //Start een intent naar de main activity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
