package com.myhealth.application.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akatchi on 13-9-2014.
 */
public class SaveProfileTask extends AsyncTask<String, String, Integer>
{
    private String      token, email, firstname, lastname;
    private JSONParser  jsonParser = new JSONParser();

    public SaveProfileTask(String token, String email, String firstname, String lastname)
    {
        this.token      = token;
        this.email      = email;
        this.firstname  = firstname;
        this.lastname   = lastname;
    }

    @Override
    protected Integer doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",      token));
        params.add(new BasicNameValuePair("email",      email));
        params.add(new BasicNameValuePair("first_name",  firstname));
        params.add(new BasicNameValuePair("last_name",   lastname));

        //Haal het json object op
        JSONObject json = jsonParser.makeHttpRequest(urls[0],"POST", params);

        int    code    = 0;

        try
        {
            Log.d("json", json.getString("status") + ":: " + json.toString());
            //Haal de message en code op van de JSON return page
            code    = json.getInt("status");

        }
        catch (JSONException e) 	{ e.printStackTrace(); }

        return code;
    }
}
