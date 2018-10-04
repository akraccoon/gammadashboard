package com.example.antonkorobko.laqshdashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BadgesListAdapterWithCache  extends ArrayAdapter<Badge> {
    private Context mContext;
    List<Badge> mylist;

    public BadgesListAdapterWithCache(Context _context, List<Badge> _mylist) {
        super(_context, R.layout.grid_item, _mylist);

        mContext = _context;
        this.mylist = _mylist;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Badge badge = getItem(position);

        BadgeViewHolder holder;

        if (convertView == null) {
            convertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
            convertView = vi.inflate(R.layout.grid_item, parent, false);

            //
            holder = new BadgeViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.image);
            holder.title = (TextView)convertView.findViewById(R.id.title);

            //
            convertView.setTag(holder);
        }
        else{
            holder = (BadgeViewHolder) convertView.getTag();
        }


        //
        holder.populate(badge, ((MainActivity)mContext).isLvBusy());

        //
        return convertView;
    }


    static class BadgeViewHolder {
        public ImageView img;
        public TextView title;

        void populate(Badge p) {
            title.setText(p.title);

            //
            ImageDownloader imageDownloader = new ImageDownloader();
            imageDownloader.download(p.img_url, img);
        }

        void populate(Badge p, boolean isBusy) {
//            title.setText(p.title);

            if (!isBusy){
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.download(p.img_url, img);
            }
            else{
                img.setImageResource(R.color.colorPrimary);
            }
        }
    }

}

