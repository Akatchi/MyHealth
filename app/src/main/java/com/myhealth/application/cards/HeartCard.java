package com.myhealth.application.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.myhealth.application.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
* Created by Akatchi on 25-9-2014.
*/
public class HeartCard extends Card {

    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    private String heartrate, complaint;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public HeartCard(Context context) {
        this(context, R.layout.card_heartmeasurement);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public HeartCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_heartrate);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.card_klachten);


        if (mTitle!=null){ mTitle.setText(heartrate); }
        if (mSecondaryTitle!=null){ mSecondaryTitle.setText(complaint); }


    }

    public void setHeartrate(String heartrate)
    {
        this.heartrate = heartrate;
    }

    public void setComplaint(String complaint)
    {
        this.complaint = complaint;
    }
}