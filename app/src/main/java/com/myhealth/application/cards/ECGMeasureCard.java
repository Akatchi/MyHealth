package com.myhealth.application.cards;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.myhealth.application.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Akatchi on 29-9-2014.
 */
public class ECGMeasureCard extends Card
{

    protected EditText mComplaint;
    protected LinearLayout mGraph;
    private String complaint;
    private int[] graphData;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public ECGMeasureCard(Context context) {
        this(context, R.layout.card_measureecg);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public ECGMeasureCard(Context context, int innerLayout) {
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
        mComplaint = (EditText) parent.findViewById(R.id.card_klachten);
        mGraph = (LinearLayout) parent.findViewById(R.id.graph);

        if( mGraph != null ) { setupGraph(graphData, mGraph); }


    }

    public void setGraphData(int[] graphData)
    {
        this.graphData = graphData;
    }

    public EditText getComplaint()
    {
        return mComplaint;
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

        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);

        graphView.addSeries(ECGGraph); // data

        layout.addView(graphView);
    }
}