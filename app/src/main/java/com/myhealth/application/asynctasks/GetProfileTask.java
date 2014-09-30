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
 * Created by Akatchi on 30-9-2014.
 */
public class GetProfileTask extends AsyncTask<String, String, UserObject>
{
    private String      token, firstname, lastname;
    private JSONParser  jsonParser = new JSONParser();

    public GetProfileTask(String token)
    {
        this.token      = token;
    }

    @Override
    protected UserObject doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",      token));

        //Haal het json object op
        JSONObject json = jsonParser.makeHttpRequest(urls[0],"GET", params);

        int    code    = 0;
        UserObject toReturn = null;

        try
        {
            Log.d("json", json.getString("status") + ":: " + json.toString());
            //Haal de message en code op van de JSON return page
            code        = json.getInt("status");

            JSONObject result = json.getJSONObject("result");

            firstname   = result.getString("first_name");
            lastname    = result.getString("last_name");

            toReturn = new UserObject(firstname, lastname, code);
        }
        catch (JSONException e) 	{ e.printStackTrace(); }

        return toReturn;
    }
}

