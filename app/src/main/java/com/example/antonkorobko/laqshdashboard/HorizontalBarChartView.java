package com.example.antonkorobko.laqshdashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HorizontalBarChartView extends AppCompatActivity {

    private Context context;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private HorizontalBarChart horizontalbarchart;
    private int mFillColor = Color.argb(255, 43, 94, 117);

    public HorizontalBarChartView(Context context){
//        HorizontalBarChart horizontalbarchart;
        this.context = context;
//        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    public void showHorizontalBarChart(ArrayList<HashMap> progressMap) {
//        setContentView(R.layout.activity_main);
        horizontalbarchart = ((Activity)context).findViewById(R.id.horizontalbarchart);

//        tvX = findViewById(R.id.tvXMax);
//        tvY = findViewById(R.id.tvYMax);
        horizontalbarchart.setMaxVisibleValueCount(800);
        horizontalbarchart.setDrawGridBackground(false);

        XAxis xl = horizontalbarchart.getXAxis();
        xl.setPosition(XAxisPosition.TOP);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = horizontalbarchart.getAxisLeft();
//        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(800f);

        YAxis yr = horizontalbarchart.getAxisRight();
//        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
//        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        int arraySize = progressMap.size();
        for (int i = 0; i < arraySize; i++) {

            Float limitLinePosition =  Float.valueOf((String) progressMap.get(i).get("status_points"));
            String limitLineLabel = (String) progressMap.get(i).get("title");
//            String limitLineColor = (String) progressMap.get(i).get("status_color");

            LimitLine ll = new LimitLine(limitLinePosition, limitLineLabel);
            ll.setLineColor(Color.BLUE);
            ll.setLineWidth(4f);
            ll.setTextColor(Color.BLACK);
            ll.setTextSize(12f);
            ll.setLineWidth(4f);
            ll.enableDashedLine(10f, 10f, 3f);
            yl.addLimitLine(ll);
        }


        setData(1, 700);
        horizontalbarchart.setFitBars(false);
        horizontalbarchart.animateY(2500);
//
        Legend l = horizontalbarchart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);
    }


    private void setData(int count, float range) {

        float barWidth = 3f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        int pointsInt = Integer.parseInt(MainActivity.points);
        yVals1.add(new BarEntry(1, pointsInt));

        BarDataSet set1;

        if (horizontalbarchart.getData() != null &&
                horizontalbarchart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)horizontalbarchart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            horizontalbarchart.getData().notifyDataChanged();
            horizontalbarchart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Total points");

            set1.setDrawIcons(false);
            set1.setColor(mFillColor);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(barWidth);
            horizontalbarchart.setData(data);
        }
    }
}
