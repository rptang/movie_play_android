package com.project.rptang.android.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.project.rptang.android.R;
import com.project.rptang.android.component.VerticalVolumeSeekBar;
import com.project.rptang.android.model.MovieModel;
import com.project.rptang.android.utils.MoviePlayer;
import com.project.rptang.android.utils.MoviePresenter;
import com.project.rptang.android.utils.MovieUtils;
import java.util.List;

public class MoviePlayActivity extends Activity implements View.OnClickListener, MoviePlayer.MovieUpdateListener, MoviePresenter.MovieControlView {

    private static final String TAG = "MoviePlayActivity";
    private static final String FILE_PATH = "/data/local/vip/userdata/ui/1280x800/movie/";

    private TextView movie_name;
    private TextView tv_current_time;
    private TextView tv_total_time;
    private ImageView back;
    private ImageView iv_pause_movie;
    private ImageView iv_volume;
    private SeekBar seekbar;
    private VerticalVolumeSeekBar sk_volume;
    private MovieUtils movieUtils;
    private SurfaceView surfaceview;
    private MoviePlayer moviePlayer;
    private RelativeLayout player_top_layout;
    private RelativeLayout player_bottom_layout;
    private MoviePresenter moviePresenter;
    private List<MovieModel> movieModelList;

    private boolean isLayoutShowing = false;   //判断上下部的控制界面的显示状态
    private boolean isVolumeSeekBarShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_play);
        initView();
        initListener();
        initDrawable();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        movie_name = (TextView) findViewById(R.id.movie_name);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);
        back = (ImageView) findViewById(R.id.back);
        iv_pause_movie = (ImageView) findViewById(R.id.iv_pause_movie);
        iv_volume = (ImageView) findViewById(R.id.iv_volume);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        sk_volume = (VerticalVolumeSeekBar) findViewById(R.id.sk_volume);
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
        player_top_layout = (RelativeLayout) findViewById(R.id.player_top_layout);
        player_bottom_layout = (RelativeLayout) findViewById(R.id.player_bottom_layout);
        player_top_layout.setAlpha(0.75f);
        player_bottom_layout.setAlpha(0.75f);
        sk_volume.setAlpha(0.75f);

        player_top_layout.setVisibility(View.GONE);
        player_bottom_layout.setVisibility(View.GONE);
        sk_volume.setVisibility(View.GONE);
    }

    private void initListener() {

        back.setOnClickListener(this);
        iv_pause_movie.setOnClickListener(this);
        iv_volume.setOnClickListener(this);

        surfaceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLayoutShowing) {
                    movieControlViewInVisiable();
                } else {
                    movieControlViewVisiable();
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (moviePlayer != null && moviePlayer.isMoviePlaying()) {
                    // 设置当前播放的位置
                    moviePlayer.getMediaPlayer().seekTo(progress);
                }
            }
        });
    }

    private StateListDrawable play_drawable;
    private StateListDrawable pause_drawable;

    private void initDrawable() {

        movieUtils = new MovieUtils();

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "guanbi_btn_nor.png")));
        drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "guanbi_btn_pre.png")));
        back.setImageDrawable(drawable);

        play_drawable = new StateListDrawable();
        play_drawable.addState(new int[]{-android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "kaishi_btn_nor.png")));
        play_drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "kaishi_btn_pre.png")));

        pause_drawable = new StateListDrawable();
        pause_drawable.addState(new int[]{-android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "zanting_btn_nor.png")));
        pause_drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "zanting_btn_pre.png")));
        iv_pause_movie.setImageDrawable(pause_drawable);

        StateListDrawable volume_drawable = new StateListDrawable();
        volume_drawable.addState(new int[]{-android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "yinliang_btn_nor.png")));
        volume_drawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(movieUtils.getDiskBitmap(FILE_PATH + "yinliang_btn_pre.png")));
        iv_volume.setImageDrawable(volume_drawable);
    }

    private void initData() {

        /**
         * 获得视频集合以及用户选中的视频位置信息
         */
        int position = getIntent().getIntExtra("position",0);
        movieModelList = (List<MovieModel>) getIntent().getSerializableExtra("MovieList");

        movie_name.setText(movieModelList.get(position).getTitle());

        /**
         * 控制播放视频的实现类
         */
        moviePresenter = new MoviePresenter(this);
        moviePlayer = new MoviePlayer(surfaceview, position);
        moviePlayer.setMovieModelList(movieModelList);
        moviePlayer.setMovieUpdateListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                MoviePlayActivity.this.finish();
                break;
            case R.id.iv_pause_movie:
                if (moviePlayer.isMoviePlaying()) {
                    moviePlayer.pause();
                    iv_pause_movie.setImageDrawable(play_drawable);
                }else{
                    moviePlayer.continuePlay();
                    iv_pause_movie.setImageDrawable(pause_drawable);
                }
                moviePresenter.resetMovieControlViewTimer();
                break;
            case R.id.iv_volume:
                if (!isVolumeSeekBarShowing) {
                    sk_volume.setVisibility(View.VISIBLE);
                    isVolumeSeekBarShowing = true;
                }else{
                    sk_volume.setVisibility(View.GONE);
                    isVolumeSeekBarShowing = false;
                }
                moviePresenter.resetMovieControlViewTimer();
                break;
        }
    }

    @Override
    public void setPlayCompletion() {
        if(moviePlayer.getPosition() >= movieModelList.size()-1){
            this.finish();
        }else {
            moviePlayer.playNextMovie();
        }
    }

    @Override
    public void setDuration(int duration) {
        tv_total_time.setText(MovieUtils.milliSecondConvertToTimeStyle(duration));
        seekbar.setMax(duration);
    }

    @Override
    public void updateTime(String time, int progress) {
        seekbar.setProgress(progress);
        tv_current_time.setText(time);
    }

    @Override
    public void dismissMovieControlView() {
        movieControlViewInVisiable();
    }

    private void movieControlViewInVisiable(){
        player_top_layout.setVisibility(View.GONE);
        player_bottom_layout.setVisibility(View.GONE);
        sk_volume.setVisibility(View.GONE);
        isLayoutShowing = false;
        isVolumeSeekBarShowing = false;
        moviePresenter.endMovieControlViewTimer();
    }

    private void movieControlViewVisiable(){
        player_top_layout.setVisibility(View.VISIBLE);
        player_bottom_layout.setVisibility(View.VISIBLE);
        isLayoutShowing = true;
        moviePresenter.startMovieControlViewTimer();
    }
}
