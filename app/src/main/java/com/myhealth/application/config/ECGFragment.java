package com.myhealth.application.config;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.myhealth.application.R;
import com.myhealth.application.cards.ECGCard;
import com.myhealth.application.database.DatabaseHelper;
import com.myhealth.application.database.ECGMeasure;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ECGFragment extends Fragment
{
    private DatabaseHelper      dbHelper;
    private List<ECGMeasure>    ECGMeasureList;
    private SessionManager      session;
    private ListView            mMeasurements;
    private View                rootView;
    private LayoutInflater      inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_ecgmeasurement, container, false);

        this.inflater = inflater;

        mMeasurements = (ListView) rootView.findViewById(R.id.measureList);

        session = new SessionManager(rootView.getContext());

        fillECGListView();

        return rootView;
    }

    private void fillECGListView()
    {
        try
        {
            dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
            Dao<ECGMeasure, Integer> ecgDao = dbHelper.getECGMeasure();

            ECGMeasureList = ecgDao.queryForEq("username", session.getUsername());
            Collections.reverse(ECGMeasureList);

            OpenHelperManager.releaseHelper();

            final ArrayList<Card> cards = new ArrayList<Card>();
            final CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

            for (ECGMeasure item : ECGMeasureList)
            {
                final ECGMeasure delItem = item;
                ECGCard card = new ECGCard(rootView.getContext());
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
                            Dao<ECGMeasure, Integer> ecgDao = dbHelper.getECGMeasure();

                            ecgDao.delete(delItem);
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

                card.setGraphData(item.getECGdata());
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