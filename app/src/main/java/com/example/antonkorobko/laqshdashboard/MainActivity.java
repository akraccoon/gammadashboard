package com.example.antonkorobko.laqshdashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity{

    ProgressDialog pd;
    TextView txtJson;
    ImageView badgeImageView;
    public static String points;

    private boolean lvBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);
//        txtJson = (TextView) findViewById(R.id.txtJson);

        String username = "staff";
//        String username = "rimikt4";
        String lmsUrl = "gamma-demo-stage.raccoongang.com";
//        String lmsUrl = "gamma-laqsh-dev.raccoongang.com";
        points = "0";

        new JsonTask(new JsonTask.AsyncResponse(){
            // GET POINTS
            @Override
            public void processFinish(String output){
                Log.d("MAIN Response: ", "> " + output);
                try {
                    HashMap progressMap = jsonToMap(output);
                    System.out.println("POINTS map : "+progressMap);
                    System.out.println("POINTS: "+progressMap.get("points"));
                    points = (String) progressMap.get("points");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://" + lmsUrl + "/api/v0/points/?username=" + username);


        new JsonTask(new JsonTask.AsyncResponse(){
            // STATUS HORIZONTAL BAR
            @Override
            public void processFinish(String output){
                // Need to get overall points from https://gamma-demo-stage.raccoongang.com/api/v0/points/?username=staff
                Log.d("MAIN Response: ", "> " + output);
                try {
                    ArrayList<HashMap> progressMap = jsonArrayToMap(output);
//                    System.out.println("HORIZONTAL map : "+progressMap);
                    // call HorizontalBarChart

                    HorizontalBarChartView horizontalbarchartview = new HorizontalBarChartView(MainActivity.this);
                    horizontalbarchartview.showHorizontalBarChart(progressMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://" + lmsUrl + "/api/v0/statuses/?username=" + username);


        new JsonTask(new JsonTask.AsyncResponse(){
            // PIE CHART
            @Override
            public void processFinish(String output){
                Log.d("MAIN Response: ", "> " + output);
                try {
                    HashMap pieChart = jsonToMap(output);
                    PieChartView pieChartView = new PieChartView(MainActivity.this);
                    pieChartView.showPieChart(pieChart);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://" + lmsUrl + "/api/v0/chart/?username=" + username);


        new JsonTask(new JsonTask.AsyncResponse(){
            // FILLED LINE
            @Override
            public void processFinish(String output){
                // Need to get status limits from https://gamma-demo-stage.raccoongang.com/api/v0/statuses/?username=staff

                Log.d("MAIN Response: ", "> " + output);
                try {
                    ArrayList<HashMap> filledLineMap = jsonArrayToMap(output);
                    System.out.println("PROGREZZ map : "+filledLineMap);
                    // call FilledLineChart

                    LineChartView linechartview = new LineChartView(MainActivity.this);
                    linechartview.showHorizontalBarChart(filledLineMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://" + lmsUrl + "/api/v0/progress/?username=" + username);


        new JsonTask(new JsonTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                try {
                    ArrayList<HashMap> badgesMap = jsonArrayToMap(output);
                    System.out.println("BAGEZZ map : "+badgesMap);
                    showBadgesBlock(badgesMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://" + lmsUrl + "/api/v0/badges/?username=" + username);

    }

    private void showBadgesBlock(ArrayList<HashMap> badgesMap) {
        ExpandableHeightGridView gridview = (ExpandableHeightGridView) findViewById(R.id.grid_badges);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setExpanded(true);
        ArrayList badges = new ArrayList<>();

        for (int i = 0; i < badgesMap.size(); i++) {
            URL url = null;
            String badgeUrl = (String) badgesMap.get(i).get("url");
            if (badgeUrl != null && !badgeUrl.isEmpty() && !badgeUrl.equals("null")) {
                System.out.println("BAGEZZZ URRRL " + badgeUrl);
                badges.add(new Badge(badgeUrl));
            }
        }

        BadgesListAdapterWithCache adapterBadges = new BadgesListAdapterWithCache(this, badges);
        gridview.setAdapter(adapterBadges);
    }

    public static ArrayList<HashMap> jsonArrayToMap(String t) throws JSONException {

        try {
            JSONArray JsonArray = new JSONArray(t);
            ArrayList<HashMap> hashMapArrayList = new ArrayList<>();
            System.out.println("jsonARRAY LENGTH " + JsonArray.length());
            for (int i = 0; i < JsonArray.length(); i++) {
                JSONObject obj = JsonArray.getJSONObject(i);
                try {
                    HashMap<String, String> map = new HashMap<>();
                    Iterator<?> keys = obj.keys();

                    while( keys.hasNext() ){
                        String key = (String)keys.next();
                        String value = obj.getString(key);
                        map.put(key, value);
                    }
                    hashMapArrayList.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return hashMapArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static HashMap jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }
        return map;
    }



    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
//                adapterBadges.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                lvBusy = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                lvBusy = true;
                break;
        }
    }

    public boolean isLvBusy(){
        return lvBusy;
    }




public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    public Integer[] mThumbIds = {};
}
}

