package com.myhealth.application.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.myhealth.application.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Akatchi on 29-9-2014.
 */
public class HeartMeasureCard extends Card
{

    protected TextView mHeartrate;
    protected EditText mKlachten;
    private String heartrate;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public HeartMeasureCard(Context context) {
        this(context, R.layout.card_measureheart);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public HeartMeasureCard(Context context, int innerLayout) {
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
        mHeartrate = (TextView) parent.findViewById(R.id.card_heartrate);
        mKlachten = (EditText) parent.findViewById(R.id.card_klachten);

        if( mHeartrate!=null ){ mHeartrate.setText(heartrate); }
//        if( mSecondaryTitle!=null ){ mSecondaryTitle.setText(complaint + "\n"); }
    }

    public void setHeartrate(String heartrate)
    {
        this.heartrate = heartrate;
    }

    public EditText getComplaint()
    {
        return mKlachten;
    }

}