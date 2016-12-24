package com.project.rptang.android.component;

/**
 * Created by mathum on 2016/11/24.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.AudioManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.project.rptang.android.utils.MovieUtils;

public class VerticalVolumeSeekBar extends SeekBar {

    private static final String THUMB_PATH = "/data/local/vip/userdata/ui/1280x800/movie/yinliang_slider_btn.png";
    private static final String PROGRESS_NORMAL_PATH = "/data/local/vip/userdata/ui/1280x800/movie/yinliang_slider_nor.png";
    private static final String PROGRESS_ACTIVE_PATH = "/data/local/vip/userdata/ui/1280x800/movie/yinliang_slider_sel.png";
    private static final String TAG = "VerticalVolumeSeekBar";
    private MovieUtils movieUtils = new MovieUtils();
    private Bitmap thumbBitmap;
    private Bitmap progressNormalBitmap;
    private Bitmap progressActiveBitmap;
    private Drawable progressNormalDrawable;
    private Drawable progressActiveDrawable;
    private AudioManager mAudioManager;
    private Context context;


    public VerticalVolumeSeekBar(Context context) {
        super(context);
        this.context = context;
        changeUI();
    }

    public VerticalVolumeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        changeUI();
    }

    public VerticalVolumeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        changeUI();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    private void changeUI(){
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        setPadding(20, 10, 20, 10);
        thumbBitmap = movieUtils.getDiskBitmap(THUMB_PATH);
        progressNormalBitmap = movieUtils.getDiskBitmap(PROGRESS_NORMAL_PATH);
        progressActiveBitmap = movieUtils.getDiskBitmap(PROGRESS_ACTIVE_PATH);


        progressActiveDrawable = new BitmapDrawable(progressActiveBitmap);
        progressNormalDrawable = new BitmapDrawable(progressNormalBitmap);
        ClipDrawable clipDrawable = new ClipDrawable(progressActiveDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{progressNormalDrawable, clipDrawable});

        setThumb(new BitmapDrawable(thumbBitmap));
        setProgressDrawable(layerDrawable);
        setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.e(TAG, "progress : " + progress);
                if (mAudioManager != null) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);

                    Intent intent = new Intent();
                    intent.setAction("com.ewin.vip.seekbar.PROGRESS_CHANGED");
                    intent.putExtra("progress", progress);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        //将旋转后的视图移动回来
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int i = 0;
                //获取滑动的距离
                i = getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(i);
                //每次拖动SeekBar都会调用
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

}
