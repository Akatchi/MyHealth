package com.myhealth.application.config;

/**
 * Created by Akatchi on 23-9-2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.myhealth.application.R;
import com.myhealth.application.cards.HeartCard;
import com.myhealth.application.database.DatabaseHelper;
import com.myhealth.application.database.HeartMeasure;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeartFragment extends Fragment
{
    private DatabaseHelper      dbHelper;
    private List<HeartMeasure>  heartMeasureList;
    private SessionManager      session;
    private ListView            mMeasurements;
    private View                rootView;
    private LayoutInflater      inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_heartmeasurement, container, false);

        this.inflater = inflater;

        mMeasurements = (ListView) rootView.findViewById(R.id.measureList);

        session = new SessionManager(rootView.getContext());

        fillHeartListView();

        return rootView;
    }

    private void fillHeartListView()
    {
        try
        {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
            Dao<HeartMeasure, Integer> heartDao = dbHelper.getHeartMeasure();

            heartMeasureList = heartDao.queryForEq("username", session.getUsername());
            Collections.reverse(heartMeasureList);

            OpenHelperManager.releaseHelper();

            final ArrayList<Card> cards = new ArrayList<Card>();
            final CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            for (HeartMeasure item : heartMeasureList)
            {
                final HeartMeasure delItem = item;
                HeartCard card = new HeartCard(rootView.getContext());
                CardHeader header = new CardHeader(rootView.getContext());

                long timeago = System.currentTimeMillis() - item.getMeasureDate();

                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm");

                header.setTitle(sdf.format(new Date(item.getMeasureDate())));

                header.setButtonExpandVisible(false);
                header.setButtonOverflowVisible(false);

                header.setOtherButtonDrawable(R.drawable.ic_action_cancel);
                header.setOtherButtonVisible(true);

                //Add a callback
                header.setOtherButtonClickListener(new CardHeader.OnClickCardHeaderOtherButtonListener()
                {
                    @Override
                    public void onButtonItemClick(Card card, View view)
                    {
                        try
                        {
                            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
                            Dao<HeartMeasure, Integer> heartDao = dbHelper.getHeartMeasure();

                            heartDao.delete(delItem);
                            OpenHelperManager.releaseHelper();

                            cards.remove(card);
                            mCardArrayAdapter.notifyDataSetChanged();

                            Toast.makeText(getActivity(), getResources().getString(R.string.delete_measure_succes), Toast.LENGTH_LONG).show();
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                card.addCardHeader(header);

                card.setHeartrate(String.valueOf(item.getHeartrate()));
                card.setComplaint(getResources().getString(R.string.field_complaint) + "\n" + item.getKlachten());

//                card.setSwipeable(true);
                cards.add(card);
            }

            CardListView listView = (CardListView) rootView.findViewById(R.id.measureList);
            listView.setAdapter(mCardArrayAdapter);

        } catch (Exception e)
        {
            OpenHelperManager.releaseHelper();
            e.printStackTrace();
        }
    }
}