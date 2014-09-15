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
public class LoginTask extends AsyncTask<String, String, ResponseObject>
{
    private String      username, password;
    private JSONParser  jsonParser = new JSONParser();

    public LoginTask(String username, String password)
    {
        //Sla de login gegevens op om door te sturen naar de JSON page
        this.username = username;
        this.password = password;
    }

    @Override
    protected ResponseObject doInBackground(String... urls)
    {
        //Vul de parameters die doorgestuurd moeten worden naar de json pagina
        List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

        //Haal het json object op
        JSONObject json = jsonParser.makeHttpRequest(urls[0],"POST", params);

        String message = "";
        int    code    = 0;

        try
        {
            //Haal de message en code op van de JSON return page
            message = json.getString("message");
            code    = json.getInt("code");

            //Todo: user gegevens nog ophalen in JSON en die opslaan zodat je weet wie er ingelogd is
        }
        catch (JSONException e) 	{ e.printStackTrace(); }

        //Zet de opgehaalde message en code in een ResponseObject om die te returnen
        ResponseObject response = new ResponseObject(message, code);

        return response;
    }
}
