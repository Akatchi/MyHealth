package com.myhealth.application.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.myhealth.application.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Akatchi on 25-9-2014.
 */
public class ECGCard extends Card
{

    protected TextView mComplaint;
    protected LinearLayout mGraph;
    private String complaint;
    private int[] graphData;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public ECGCard(Context context) {
        this(context, R.layout.card_ecgmeasurement);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public ECGCard(Context context, int innerLayout) {
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
        mComplaint = (TextView) parent.findViewById(R.id.card_klachten);
        mGraph = (LinearLayout) parent.findViewById(R.id.graph);

        if( mComplaint !=null ){ mComplaint.setText(complaint + "\n"); }
        if( mGraph != null ) { setupGraph(graphData, mGraph); }


    }

    public void setGraphData(int[] graphData)
    {
        this.graphData = graphData;
    }

    public void setComplaint(String complaint)
    {
        this.complaint = complaint;
    }

    private void setupGraph(int[] graphData, LinearLayout layout)
    {
        GraphView.GraphViewData[] graphPoints = new GraphView.GraphViewData[graphData.length];

        for( int i = 0; i < graphData.length; i++ )
        {
            graphPoints[i] = new GraphView.GraphViewData(i+1, graphData[i]);
        }

        GraphViewSeries ECGGraph = new GraphViewSeries(graphPoints);

        GraphView graphView = new LineGraphView(getContext(), "ECG");
        graphView.setManualYAxisBounds(100, 0);

        graphView.addSeries(ECGGraph); // data

        layout.addView(graphView);
    }
}