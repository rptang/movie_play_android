package com.project.rptang.android.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.SeekBar;

import com.project.rptang.android.utils.MovieUtils;

/**
 * Created by mathum on 2016/11/25.
 */

public class MovieSeekBar extends SeekBar {

    private static final String THUMB_PATH = "/data/local/vip/userdata/ui/1280x800/movie/bofang_slider_btn.png";
    private static final String PROGRESS_NORMAL_PATH = "/data/local/vip/userdata/ui/1280x800/movie/bofang_slider_nor.png";
    private static final String PROGRESS_ACTIVE_PATH = "/data/local/vip/userdata/ui/1280x800/movie/bofang_slider_sel.png";
    private static final String TAG = "VolumeSeekBar";

    private MovieUtils movieUtils = new MovieUtils();

    private Bitmap thumbBitmap;
    private Bitmap progressNormalBitmap;
    private Bitmap progressActiveBitmap;
    private Drawable progressNormalDrawable;
    private Drawable progressActiveDrawable;

    public MovieSeekBar(Context context) {
        super(context);
        changeUI();
    }

    public MovieSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeUI();
    }

    public MovieSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeUI();
    }

    private void changeUI(){
        setPadding(25, 0, 25, 0);
        thumbBitmap = movieUtils.getDiskBitmap(THUMB_PATH);
        progressNormalBitmap = movieUtils.getDiskBitmap(PROGRESS_NORMAL_PATH);
        progressActiveBitmap = movieUtils.getDiskBitmap(PROGRESS_ACTIVE_PATH);


        progressActiveDrawable = new BitmapDrawable(progressActiveBitmap);
        progressNormalDrawable = new BitmapDrawable(progressNormalBitmap);
        ClipDrawable clipDrawable = new ClipDrawable(progressActiveDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{progressNormalDrawable, clipDrawable});

        setThumb(new BitmapDrawable(thumbBitmap));
        setProgressDrawable(layerDrawable);
        setMax(100);
    }

}
