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

    private String username = "staff";
    ProgressDialog pd;
    TextView txtJson;
    ImageView badgeImageView;
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
//        LineChart linechart = (LineChart) findViewById(R.id.linechart);
//        PieChart piechart = (PieChart) findViewById(R.id.piechart);
        HorizontalBarChart horizontalbarchart = (HorizontalBarChart) findViewById(R.id.horizontalbarchart);

        new JsonTask(new JsonTask.AsyncResponse(){
            // BAGEZZZ
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
        }).execute("https://gamma-demo-stage.raccoongang.com/api/v0/badges/?username=staff");

        new JsonTask(new JsonTask.AsyncResponse(){
        // STATUSES
            @Override
            public void processFinish(String output){
                Log.d("MAIN Response: ", "> " + output);
                try {
                    jsonArrayToMap(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://gamma-demo-stage.raccoongang.com/api/v0/statuses/?username=staff");

        new JsonTask(new JsonTask.AsyncResponse(){

            @Override
            public void processFinish(String output){
                Log.d("MAIN Response: ", "> " + output);
                try {
                    jsonArrayToMap(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://gamma-demo-stage.raccoongang.com/api/v0/progress/?username=staff");

        new JsonTask(new JsonTask.AsyncResponse(){

            @Override
            public void processFinish(String output){
                Log.d("MAIN Response: ", "> " + output);
                try {
                    jsonToMap(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute("https://gamma-demo-stage.raccoongang.com/api/v0/chart/?username=staff");

    }

    private void showBadgesBlock(ArrayList<HashMap> badgesMap) {
//        LinearLayout layout = (LinearLayout)findViewById(R.id.linearlayout);
//        ConstraintSet set = new ConstraintSet();

        GridView gridview = (GridView) findViewById(R.id.grid_badges);
        gridview.setAdapter(new ImageAdapter(this));
        ArrayList badges = new ArrayList<>();

        for (int i = 0; i < badgesMap.size(); i++) {

            URL url = null;
            String badgeUrl = (String) badgesMap.get(i).get("url");
            if (badgeUrl != null && !badgeUrl.isEmpty() && !badgeUrl.equals("null")) {

                System.out.println("BAGEZZZ URRRL " + badgeUrl);
//                try {
//                    url = new URL(badgeUrl);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }

                badges.add(new Badge(badgeUrl));

//                Bitmap bmp = null;
//                try {
//                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                ImageView image = new ImageView(this);
//                image.setId(View.generateViewId());
//                layout.addView(image, 0);
//                set.connect(image.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP, 60);
//                image.setImageBitmap(bmp);

//                set.clone(layout);
//                set.applyTo(layout);
            }
        }
//        GridView gvBadges = findViewById( R.id.grid_badges);
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


    public static void jsonToMap(String t) throws JSONException {

        HashMap<String, String> map = new HashMap<>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        System.out.println("json : "+jObject);
        System.out.println("map : "+map);
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

