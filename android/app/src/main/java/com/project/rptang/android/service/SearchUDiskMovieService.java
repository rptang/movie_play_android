package com.project.rptang.android.service;

import android.app.IntentService;
import android.content.Intent;
import com.project.rptang.android.MovieConstants;
import com.project.rptang.android.model.MovieModel;
import com.project.rptang.android.utils.MovieUtils;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchUDiskMovieService extends IntentService {

    private MovieUtils movieUtils;
    private List<MovieModel> movieModels = new ArrayList<>();

    public SearchUDiskMovieService(){
        super(SearchUDiskMovieService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if(action != null && action.equals(MovieConstants.ACTION_SEARCH_MOVIES)){
            movieUtils = new MovieUtils();
            movieUtils.searchAllMovie(new File("/mnt/udisk"));
            movieModels = movieUtils.list;

            Intent sendMovieListIntent = new Intent(MovieConstants.ACTION_SHOW_ALL_MOVIES);
            sendMovieListIntent.putExtra("MovieList", (Serializable) movieModels);
            sendBroadcast(sendMovieListIntent);
        }
    }
}
