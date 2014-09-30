package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.myhealth.application.R;
import com.myhealth.application.asynctasks.SaveECGTask;
import com.myhealth.application.cards.ECGMeasureCard;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;
import com.myhealth.application.database.DatabaseHelper;
import com.myhealth.application.database.ECGMeasure;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class ECGActivity extends Activity
{
    private EditText mKlachten;

    private int[] ECGdata;
    private SessionManager session;
    private DatabaseHelper dbHelper;
    private ECGMeasureCard card;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        session = new SessionManager(getApplicationContext());

//        mKlachten = (EditText) findViewById(R.id.field_klachten);

//        ECGdata = HealthObject.getInstance().getECGData();

        ECGdata = new int[20];
        Random r = new Random();
        for( int j = 0; j < ECGdata.length; j++ )
        {
            ECGdata[j] = r.nextInt(100);
        }

        card = new ECGMeasureCard(getApplicationContext());
            CardHeader header = new CardHeader(getApplicationContext());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
            header.setTitle(sdf.format(new Date()));
            card.addCardHeader(header);


            card.setGraphData(ECGdata);
        CardView cardView = (CardView) findViewById(R.id.ecg_card);
        cardView.setCard(card);
    }

    public void saveOnline(View v)
    {
        try
        {
            String klachten = "";
            if( !card.getComplaint().getText().toString().equals(getResources().getString(R.string.field_klachten)) )
            {
                klachten = card.getComplaint().getText().toString();
            }

            int code = new SaveECGTask(klachten, session.getToken(), session.getUsername(), Arrays.toString(ECGdata)).execute(new String[]{Variables.SENDECGDATA}).get();

            if( code == Variables.CODE_SUCCESS )
            {
                Toast.makeText(getApplicationContext(), R.string.ecg_onlinesave_success, Toast.LENGTH_SHORT).show();
                Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startMain);
            }
            else if( code == Variables.CODE_TOKEN_EXPIRED )
            {
                Toast.makeText(getApplicationContext(), R.string.token_expired, Toast.LENGTH_SHORT).show();
                session.logoutUser();
            }
        }
        catch( Exception e ) { e.printStackTrace(); }
    }

    public void saveLocal(View v)
    {
        String klachten = "";
        if( !card.getComplaint().getText().toString().equals(getResources().getString(R.string.field_klachten)) )
        {
            klachten = card.getComplaint().getText().toString();
        }

        try
        {
            dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            Dao<ECGMeasure, Integer> ecgDao = dbHelper.getECGMeasure();

            ecgDao.create(new ECGMeasure(ECGdata, session.getUsername(), klachten));

            OpenHelperManager.releaseHelper();

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.save_measure_success), Toast.LENGTH_SHORT).show();
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startActivity(startMainActivity);

        }
        catch( Exception e ){ OpenHelperManager.releaseHelper(); e.printStackTrace(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ecg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
