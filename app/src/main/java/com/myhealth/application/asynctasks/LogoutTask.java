package com.myhealth.application.asynctasks;

import android.os.AsyncTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akatchi on 13-9-2014.
 */
public class LogoutTask extends AsyncTask<String, String, Integer>
{
    private String      token;
    private JSONParser  jsonParser = new JSONParser();

    public LogoutTask(String token)
    {
        this.token = token;
    }

    @Override
    protected Integer doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        //Haal het json object op
        JSONObject json = jsonParser.makeHttpRequest(urls[0],"GET", params);
        int    code    = 0;

        try
        {
            code    = json.getInt("status");
        }
        catch (JSONException e) 	{ e.printStackTrace(); }

        return code;
    }
}
