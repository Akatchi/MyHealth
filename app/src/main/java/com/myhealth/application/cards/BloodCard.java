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
public class BloodCard extends Card
{

    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected TextView mBdLaag, mBdHoog;
    private String heartrate, complaint, bdHoog, bdLaag;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public BloodCard(Context context) {
        this(context, R.layout.card_bloodmeasurement);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public BloodCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_heartrate);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.card_klachten);
        mBdHoog = (TextView) parent.findViewById(R.id.card_bloodHigh);
        mBdLaag = (TextView) parent.findViewById(R.id.card_bloodLow);


        if( mTitle!=null ){ mTitle.setText(heartrate); }
        if( mSecondaryTitle!=null ){ mSecondaryTitle.setText(complaint + "\n"); }
        if( mBdHoog != null ) { mBdHoog.setText(bdHoog); }
        if( mBdLaag != null ) { mBdLaag.setText(bdLaag); }


    }

    public void setHeartrate(String heartrate)
    {
        this.heartrate = heartrate;
    }

    public void setComplaint(String complaint)
    {
        this.complaint = complaint;
    }

    public void setBdHoog(String bdHoog)
    {
        this.bdHoog = bdHoog;
    }

    public void setBdLaag(String bdLaag)
    {
        this.bdLaag = bdLaag;
    }
}