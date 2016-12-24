package com.project.rptang.android.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.project.rptang.android.MovieConstants;
import com.project.rptang.android.R;
import com.project.rptang.android.adapter.MovieAdapter;
import com.project.rptang.android.model.MovieModel;
import com.project.rptang.android.service.SearchUDiskMovieService;
import com.project.rptang.android.utils.MovieUtils;

import java.util.List;

public class MovieActivity extends Activity {

    private static final String TAG = "MovieActivity";

    private GridView movie_grid;
    private TextView tv_tips;
    private SearchAllMoviesReceiver mSearchAllMoviesReceiver;
    private ShowAllMoviesReceiver mShowAllMoviesReceiver;
    private List<MovieModel> movieModels;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initView();
        initData();
    }

    private void initData() {

        if(MovieUtils.getUsbStatus()){
            startSearchMovieService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        regSearchReceiver();
        regShowReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregSearchReceiver();
        unregShowReceiver();
    }

    private void initView() {

        movie_grid = (GridView) findViewById(R.id.movie_grid);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
    }

    private void regSearchReceiver() {

        mSearchAllMoviesReceiver = new SearchAllMoviesReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_MEDIA_CHECKING);
        mIntentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        mIntentFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        mIntentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        mIntentFilter.addAction(MovieConstants.ACTION_SHOW_ALL_MOVIES);
        mIntentFilter.addDataScheme("file");
        registerReceiver(mSearchAllMoviesReceiver,mIntentFilter);
    }

    private void unregSearchReceiver() {

        unregisterReceiver(mSearchAllMoviesReceiver);
    }

    private void regShowReceiver(){
        mShowAllMoviesReceiver = new ShowAllMoviesReceiver();
        IntentFilter showMovieFilter = new IntentFilter();
        showMovieFilter.addAction(MovieConstants.ACTION_SHOW_ALL_MOVIES);
        showMovieFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        registerReceiver(mShowAllMoviesReceiver, showMovieFilter);
    }

    private void unregShowReceiver(){
        unregisterReceiver(mShowAllMoviesReceiver);
    }

    public class SearchAllMoviesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: "+action);
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                startSearchMovieService();
            }else if(action.equals(Intent.ACTION_MEDIA_EJECT)) {
                tv_tips.setText("U盘已被拔出");
                tv_tips.setVisibility(View.VISIBLE);
                movie_grid.setVisibility(View.GONE);
            }
        }
    }

    public class ShowAllMoviesReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                movieModels = (List<MovieModel>) intent.getSerializableExtra("MovieList");
                if (movieModels.size() == 0) {
                    tv_tips.setText("U盘里面没有视频资源");
                }else{
                    tv_tips.setVisibility(View.GONE);
                    movieAdapter = new MovieAdapter(context, movieModels);
                    movie_grid.setVisibility(View.VISIBLE);
                    movie_grid.setAdapter(movieAdapter);
                }
            }
        }
    }

    private void startSearchMovieService(){
        Intent mSearchMovieService = new Intent(MovieActivity.this,SearchUDiskMovieService.class);
        mSearchMovieService.setAction(MovieConstants.ACTION_SEARCH_MOVIES);
        startService(mSearchMovieService);
    }
}
