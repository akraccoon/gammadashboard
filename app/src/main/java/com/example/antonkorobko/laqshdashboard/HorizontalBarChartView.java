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
        horizontalbarchart.setMaxVisibleValueCount(700);
        horizontalbarchart.setDrawGridBackground(false);

        XAxis xl = horizontalbarchart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
//        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = horizontalbarchart.getAxisLeft();
//        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);

        YAxis yr = horizontalbarchart.getAxisRight();
//        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(700f); // this replaces setStartAtZero(true)

        LimitLine ll1 = new LimitLine(325f, "Good student");
        ll1.setLineColor(Color.RED);
        ll1.setLineWidth(4f);
        ll1.setTextColor(Color.BLACK);
        ll1.setTextSize(12f);
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);

        LimitLine ll2 = new LimitLine(600f, "Super student");
        ll2.setLineColor(Color.BLUE);
        ll2.setLineWidth(4f);
        ll2.setTextColor(Color.BLACK);
        ll2.setTextSize(12f);
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);

        yl.addLimitLine(ll1);
        yl.addLimitLine(ll2);

        setData(1, 700);
        horizontalbarchart.setFitBars(true);
        horizontalbarchart.animateY(2500);

        Legend l = horizontalbarchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }


    private void setData(int count, float range) {

        float barWidth = 3f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            yVals1.add(new BarEntry(i * spaceForBar, val));
        }

        BarDataSet set1;

        if (horizontalbarchart.getData() != null &&
                horizontalbarchart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)horizontalbarchart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            horizontalbarchart.getData().notifyDataChanged();
            horizontalbarchart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet 1");

            set1.setDrawIcons(false);

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
