package com.project.rptang.android.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Stiven on 2016/12/21.
 */

public class MoviePresenter {

    private static final int MSG_MOVIE_CONTROL_VIEW_DISMISS = 0x11;
    private MovieControlView movieControlView;
    private MovieControlViewHandler movieControlViewHandler;

    public MoviePresenter(MovieControlView movieControlView) {
        this.movieControlView = movieControlView;
        movieControlViewHandler = new MovieControlViewHandler();
    }

    private Runnable movieControlViewRunnable = new Runnable() {
        @Override
        public void run() {
            movieControlViewHandler.obtainMessage(
                    MSG_MOVIE_CONTROL_VIEW_DISMISS).sendToTarget();
        }
    };

    public void startMovieControlViewTimer(){
        movieControlViewHandler.postDelayed(movieControlViewRunnable,5000);
    }

    public void endMovieControlViewTimer(){
        movieControlViewHandler.removeMessages(MSG_MOVIE_CONTROL_VIEW_DISMISS);
        movieControlViewHandler.removeCallbacks(movieControlViewRunnable);
    }

    public void resetMovieControlViewTimer(){
        movieControlViewHandler.removeCallbacks(movieControlViewRunnable);
        movieControlViewHandler.postDelayed(movieControlViewRunnable,5000);
    }

    public class MovieControlViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_MOVIE_CONTROL_VIEW_DISMISS:
                    movieControlView.dismissMovieControlView();
                    break;
            }
        }
    }

    public interface MovieControlView {

        public void dismissMovieControlView();
    }
}
