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
import com.myhealth.application.asynctasks.SaveHeartRateTask;
import com.myhealth.application.cards.HeartMeasureCard;
import com.myhealth.application.config.HealthObject;
import com.myhealth.application.config.SessionManager;
import com.myhealth.application.config.Variables;
import com.myhealth.application.database.DatabaseHelper;
import com.myhealth.application.database.HeartMeasure;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HeartActivity extends Activity
{
    private TextView mHartslag;
    private EditText mKlachten;
    private ListView mMeasurements;
    private SessionManager session;
    private DatabaseHelper dbHelper;
    private List<HeartMeasure> heartMeasureList;
    private HeartMeasureCard card;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

        session = new SessionManager(getApplicationContext());

        //hartslag weergeven
//        mHartslag = (TextView) findViewById(R.id.field_hartslag);
//        mKlachten = (EditText) findViewById(R.id.field_klachten);
//        mMeasurements = (ListView) findViewById(R.id.measureList);

        Random r = new Random();
        HealthObject.getInstance().setHeartRate(r.nextInt(100));

//        mHartslag.setText(getResources().getString(R.string.field_heartrate)    + " " + HealthObject.getInstance().getHeartRate());

        card = new HeartMeasureCard(getApplicationContext());
            CardHeader header = new CardHeader(getApplicationContext());
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");
            header.setTitle(sdf.format(new Date()));
            card.addCardHeader(header);

            card.setHeartrate(String.valueOf(HealthObject.getInstance().getHeartRate()));

        CardView cardView = (CardView) findViewById(R.id.heart_card);
        cardView.setCard(card);

//        fillListView();
    }

    public void saveOnline(View v)
    {
        //Todo: Klachten nog meesturen
        try
        {
            String klachten = "";
            if( !card.getComplaint().getText().toString().equals(getResources().getString(R.string.field_klachten)) )
            {
                klachten = card.getComplaint().getText().toString();
            }

            int code = new SaveHeartRateTask(HealthObject.getInstance().getHeartRate(), session.getToken(), session.getUsername(), klachten).execute(new String[]{Variables.SENDHEARTRATE}).get();

            if( code == Variables.CODE_SUCCESS )
            {
                Toast.makeText(getApplicationContext(), R.string.heartrate_onlinesave_success, Toast.LENGTH_SHORT).show();
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
            Dao<HeartMeasure, Integer> heartDao = dbHelper.getHeartMeasure();

            heartDao.create(new HeartMeasure(HealthObject.getInstance().getHeartRate(), session.getUsername(), klachten));

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
