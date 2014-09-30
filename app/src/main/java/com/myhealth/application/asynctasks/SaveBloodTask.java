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
public class SaveBloodTask extends AsyncTask<String, String, Integer>
{
    private int         bdLaag, bdHoog, hartslag;
    private String      token, email, klachten;
    private JSONParser  jsonParser = new JSONParser();

    public SaveBloodTask(int bdLaag, int bdHoog, int hartslag, String klachten, String token, String email)
    {
        this.bdLaag     = bdLaag;
        this.bdHoog     = bdHoog;
        this.hartslag   = hartslag;
        this.token      = token;
        this.email      = email;
        this.klachten   = klachten;
    }

    @Override
    protected Integer doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",      token));
        params.add(new BasicNameValuePair("email",      email));
        params.add(new BasicNameValuePair("bdLaag",     String.valueOf(bdLaag)));
        params.add(new BasicNameValuePair("bdHoog",     String.valueOf(bdHoog)));
        params.add(new BasicNameValuePair("hartslag",   String.valueOf(hartslag)));
        params.add(new BasicNameValuePair("klachten",   klachten));

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
