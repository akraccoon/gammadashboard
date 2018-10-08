package com.example.antonkorobko.laqshdashboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;


import java.util.ArrayList;
import java.util.HashMap;

public class LineChartView extends AppCompatActivity {

    private Context context;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private LineChart linechart;
    private int mFillColor = Color.argb(255, 206, 243, 105);
    private int mLineFillColor = Color.argb(255, 111, 144, 29);

    public LineChartView(Context context){
        this.context = context;
//        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    public void showHorizontalBarChart(ArrayList<HashMap> progressMap) {
//        setContentView(R.layout.activity_main);
        linechart = ((Activity)context).findViewById(R.id.linechart);

        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
//
//        LimitLine ll1 = new LimitLine(10f, "Good student");
//        ll1.setLineColor(Color.RED);
//        ll1.setLineWidth(4f);
//        ll1.setTextColor(Color.BLACK);
//        ll1.setTextSize(12f);
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//
//
//        LimitLine ll2 = new LimitLine(30f, "Super student");
//        ll2.setLineColor(Color.BLUE);
//        ll2.setLineWidth(4f);
//        ll2.setTextColor(Color.BLACK);
//        ll2.setTextSize(12f);
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//

        XAxis xAxis = linechart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line
//
//        xAxis.addLimitLine(ll1);
//        xAxis.addLimitLine(ll2);

//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

//        LimitLine ll1 = new LimitLine(200f, "Upper Limit");
//        ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

//        LimitLine ll2 = new LimitLine(30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = linechart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
//        leftAxis.setAxisMaximum(400f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);
        linechart.getAxisRight().setEnabled(false);

        //linechart.getViewPortHandler().setMaximumScaleY(2f);
        //linechart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(progressMap);

//        linechart.setVisibleXRange(20);
//        linechart.setVisibleYRange(20f, AxisDependency.LEFT);
//        linechart.centerViewTo(20, 50, AxisDependency.LEFT);

        linechart.animateX(2500);
        //linechart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = linechart.getLegend();
        l.setEnabled(false);

        // modify the legend ...
//        l.setForm(LegendForm.CIRCLE);

        // // dont forget to refresh the drawing
        // linechart.invalidate();
    }

    private void setData(ArrayList<HashMap> progressMap) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        int arraySize = progressMap.size();
        float totalValue = 0f;
        for (int i = 0; i < arraySize; i++) {

            float val = Float.valueOf((String) progressMap.get(i).get("points"));
            totalValue = totalValue+val;
            values.add(new Entry(i, totalValue));
        }

        LineDataSet set1;

        if (linechart.getData() != null &&
                linechart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)linechart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            linechart.getData().notifyDataChanged();
            linechart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Progress");

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(mLineFillColor);
            set1.setCircleColor(mLineFillColor);
            set1.setLineWidth(4f);
//            set1.setCircleRadius(3f);
//            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            }
//            else {
            set1.setFillColor(mFillColor);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            linechart.setData(data);
        }
    }

}
