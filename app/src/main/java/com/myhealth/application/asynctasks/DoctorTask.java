package com.myhealth.application.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akatchi on 13-9-2014.
 */
public class DoctorTask extends AsyncTask<String, String, DoctorObject>
{
    private String      token;
    private JSONParser  jsonParser = new JSONParser();

    public DoctorTask(String token)
    {
        this.token = token;
    }

    @Override
    protected DoctorObject doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));

        //Haal het json object op
        JSONObject json = jsonParser.makeHttpRequest(urls[0],"GET", params);

        String naam         = "";
        String email        = "";
        String telnr        = "";
        String klinieknaam  = "";
        int    code         = 0;

        Log.d("json", json.toString());
        try
        {
            //Haal de message en code op van de JSON return page
            JSONArray result    = json.getJSONArray("result");
            JSONObject temp     = result.getJSONObject(0);
            naam                = temp.getString("first_name") + " " + temp.getString("last_name");
            email               = temp.getString("email");
            telnr               = temp.getString("telephone_number");

            JSONArray kliniek   = temp.getJSONArray("clinic");
            temp                = kliniek.getJSONObject(0);
            klinieknaam         = temp.getString("name");

            code    = json.getInt("status");

            //Todo: user gegevens nog ophalen in JSON en die opslaan zodat je weet wie er ingelogd is
        }
        catch (JSONException e) 	{ e.printStackTrace(); }

        //Zet de opgehaalde message en code in een ResponseObject om die te returnen
        DoctorObject doctor = new DoctorObject(email, telnr, naam, klinieknaam, code);

        return doctor;
    }
}
