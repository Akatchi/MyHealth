package com.myhealth.application.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.myhealth.application.R;
import com.myhealth.application.asynctasks.SaveBloodTask;
import com.myhealth.application.cards.BloodMeasureCard;
import com.myhealth.application.config.HealthObject;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;
import com.myhealth.application.database.BloodMeasure;
import com.myhealth.application.database.DatabaseHelper;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BloedActivity extends Activity
{
    private TextView mBDLaag, mBDHoog, mHartslag;
    private EditText mKlachten;
    private ListView mMeasurements;

    private SessionManager session;
    private DatabaseHelper dbHelper;
    private BloodMeasureCard card;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloed);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        session = new SessionManager(getApplicationContext());

        //Bloeddruk + heartslag weergeven
//        mBDLaag     = (TextView) findViewById(R.id.textView);
//        mBDHoog     = (TextView) findViewById(R.id.textView2);
//        mHartslag   = (TextView) findViewById(R.id.textView3);
//
//        mKlachten   = (EditText) findViewById(R.id.editText);
//
//        Random r = new Random();
//        HealthObject.getInstance().setBloodPressureLow(r.nextInt(50));
//        HealthObject.getInstance().setBloodPressureHigh(r.nextInt(50) + 50);
//        HealthObject.getInstance().setHeartRate(r.nextInt(100));
//
//        mBDLaag.setText(getResources().getString(R.string.field_bdLaag)         + " " + HealthObject.getInstance().getBloodPressureLow());
//        mBDHoog.setText(getResources().getString(R.string.field_bdHoog)         + " " + HealthObject.getInstance().getBloodPressureHigh());
//        mHartslag.setText(getResources().getString(R.string.field_heartrate)    + " " + HealthObject.getInstance().getHeartRate());

        card = new BloodMeasureCard(getApplicationContext());
            CardHeader header = new CardHeader(getApplicationContext());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
            header.setTitle(sdf.format(new Date()));
            card.addCardHeader(header);

            card.setBdLaag(String.valueOf(HealthObject.getInstance().getBloodPressureLow()));
            card.setBdHoog(String.valueOf(HealthObject.getInstance().getBloodPressureHigh()));
            card.setHeartrate(String.valueOf(HealthObject.getInstance().getHeartRate()));

        CardView cardView = (CardView) findViewById(R.id.blood_card);
        cardView.setCard(card);


//        fillListView();
    }

    public void saveOnline(View v)
    {
        //Todo check of de velden niet leeg zijn
        try
        {
            String klachten = "";
            if( !card.getComplaint().getText().toString().equals(getResources().getString(R.string.field_klachten)) )
            {
                klachten = card.getComplaint().getText().toString();
            }

            int code = new SaveBloodTask(HealthObject.getInstance().getBloodPressureLow(), HealthObject.getInstance().getBloodPressureHigh(), HealthObject.getInstance().getHeartRate(), klachten, session.getToken(), session.getUsername()).execute(new String[]{Variables.SENDBLOODTEST}).get();

            if( code == Variables.CODE_SUCCESS )
            {
                Toast.makeText(getApplicationContext(), R.string.bloodpressure_onlinesave_success, Toast.LENGTH_SHORT).show();
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
        //Todo check of de velden niet leeg zijn
        String klachten = "";
        if( !card.getComplaint().getText().toString().equals(getResources().getString(R.string.field_klachten)) )
        {
            klachten = card.getComplaint().getText().toString();
        }

        try
        {
            dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
            Dao<BloodMeasure, Integer> bloodDao = dbHelper.getBloodMeasureDAO();

            bloodDao.create(new BloodMeasure(HealthObject.getInstance().getBloodPressureHigh(), HealthObject.getInstance().getBloodPressureLow(), HealthObject.getInstance().getHeartRate(), klachten, session.getUsername()));

            OpenHelperManager.releaseHelper();

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.save_measure_success), Toast.LENGTH_SHORT).show();
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startActivity(startMainActivity);

//            fillListView();
        }
        catch( Exception e ){ OpenHelperManager.releaseHelper(); e.printStackTrace(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bloed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch( item.getItemId() )
        {
            case android.R.id.home:
                Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
