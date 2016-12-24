package com.project.rptang.android.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.project.rptang.android.model.MovieModel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoviePlayer implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener{

    private static final String TAG = "MoviePlayer";
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean isPlaying = false;
    private List<MovieModel> movieModelList;
    private Timer timer = new Timer();
    private MovieUpdateListener movieUpdateListener;
    private int position;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null) {
                return;
            } else if (isPlaying) {
                handler.sendEmptyMessage(0x123);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123 && mediaPlayer != null) {
                try {
                    if (mediaPlayer.getDuration() > 0) {
                        movieUpdateListener.updateTime(
                                MovieUtils.milliSecondConvertToTimeStyle(getCurrentProgress()),
                                getCurrentProgress());
                    }
                } catch (Exception e) {
                }
            }
        }
    };

    public MoviePlayer(SurfaceView surfaceView, final int position) {
        try {
            this.position = position;
            this.surfaceView = surfaceView;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            // surfaceView的设置
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.setKeepScreenOn(true);
            //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    playMovieByUrl(movieModelList.get(position).getUrl());
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        if (timer != null) {
                            timer.cancel();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        timer.schedule(timerTask,0,1000);
    }

    public List<MovieModel> getMovieModelList() {
        return movieModelList;
    }

    public void setMovieModelList(List<MovieModel> movieModelList) {
        this.movieModelList = movieModelList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void playMovieByUrl(String url) {
        try {
            isPlaying = true;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNextMovie(){
        playMovieByUrl(movieModelList.get(++position).getUrl());
    }

    public void playPreviousMovie(){
        playMovieByUrl(movieModelList.get(--position).getUrl());
    }

    public void continuePlay(){
        if (mediaPlayer != null) {
            isPlaying = true;
            mediaPlayer.start();
        }
    }

    public void pause(){
        if (mediaPlayer != null) {
            isPlaying = false;
            mediaPlayer.pause();
        }
    }

    public void stop(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    public boolean isMoviePlaying(){
        return isPlaying;
    }

    public MediaPlayer getMediaPlayer(){
        if (mediaPlayer != null) {
            return mediaPlayer;
        }
        return null;
    }

    //获得当前位置
    public int getCurrentProgress(){
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        movieUpdateListener.setPlayCompletion();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        movieUpdateListener.setDuration(mediaPlayer.getDuration());
    }


    public interface MovieUpdateListener{
        void setPlayCompletion();
        void setDuration(int duration);
        void updateTime(String time, int progress);
    }

    public void setMovieUpdateListener(MovieUpdateListener movieUpdateListener){
        this.movieUpdateListener = movieUpdateListener;
    }

}
