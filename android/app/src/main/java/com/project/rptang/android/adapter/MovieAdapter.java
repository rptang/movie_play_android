package com.project.rptang.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.rptang.android.R;
import com.project.rptang.android.activity.MoviePlayActivity;
import com.project.rptang.android.model.MovieModel;
import java.io.Serializable;
import java.util.List;

public class MovieAdapter extends BaseAdapter {

    public List<MovieModel> list;
    private Context context;
    private ViewHolder viewHolder;


    public MovieAdapter(Context context, List<MovieModel> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.movie_item, null);
            viewHolder.textview = (TextView) view.findViewById(R.id.textview);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageview);
            viewHolder.textview.setAlpha(0.9f);
            viewHolder.textview.setText(list.get(position).getTitle());
            Glide.with(context).load(list.get(position).getUrl()).into(viewHolder.imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MoviePlayActivity.class);
                    intent.putExtra("MovieList", (Serializable) list);
                    intent.putExtra("url", list.get(position).getUrl());
                    intent.putExtra("position", position);
                    intent.putExtra("name", list.get(position).getTitle());
                    context.startActivity(intent);

//                    Intent intent1 = new Intent();
//                    intent1.setAction("com.ewin.vip.CONTROL_MUSIC_PLAY");
//                    intent1.putExtra("control", "playNextMusicFromMovie");
//                    context.sendBroadcast(intent1);
                }
            });
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder{
        private TextView textview;
        private ImageView imageView;
        private RelativeLayout layout;
    }
}

