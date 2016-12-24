package com.project.rptang.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.project.rptang.android.model.MovieModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stiven on 2016/12/20.
 */

public class MovieUtils {

    public List<MovieModel> list = new ArrayList<>();

    //遍历某目录下面那的所有视频文件
    public List<MovieModel> scanMoviesFromDir(File file) {
        List<MovieModel> list = new ArrayList<>();
        File[] files = file.listFiles();
        if (files == null) {
            return list;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".MP4")
                    || files[i].getName().endsWith(".AVI")
                    || files[i].getName().endsWith(".RM")
                    || files[i].getName().endsWith(".ASF")
                    || files[i].getName().endsWith(".mp4")
                    || files[i].getName().endsWith(".avi")
                    || files[i].getName().endsWith(".rm")
                    || files[i].getName().endsWith(".asf")) {
                MovieModel localMovie = new MovieModel(files[i].getAbsolutePath(), files[i].getName());
                list.add(localMovie);
            }
        }
        return list;
    }

    //遍历文件查询视频文件
    public void searchAllMovie(File dir){
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {
            try {
                if (fs[i].getName().endsWith(".MP4")
                        || fs[i].getName().endsWith(".AVI")
                        || fs[i].getName().endsWith(".RM")
                        || fs[i].getName().endsWith(".ASF")
                        || fs[i].getName().endsWith(".mp4")
                        || fs[i].getName().endsWith(".avi")
                        || fs[i].getName().endsWith(".rm")
                        || fs[i].getName().endsWith(".asf")) {
                    MovieModel localMovie = new MovieModel(fs[i].getAbsolutePath(), fs[i].getName());
                    list.add(localMovie);
                }
                if (fs[i].isDirectory()) {
                    searchAllMovie(fs[i]);
                }
            } catch (Exception e) {
                continue;
            }

        }
    }

    public static boolean getUsbStatus() {
        boolean bRet = false;

        try {
            FileReader usbf = new FileReader("/proc/partitions");
            BufferedReader buf = new BufferedReader(usbf);
            String fstr;

            while ((fstr = buf.readLine()) != null) {
                if (fstr.indexOf("sda1") >= 0) {
                    bRet = true; // have usb
                    break;
                }
            }
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bRet;
    }

    //将毫秒转换为分钟
    public static String milliSecondConvertToTimeStyle(int milliSecond) {
        String time = "";
        int index = milliSecond / 1000;
        if (index / (60 * 60) != 0) {
            if (index % (60 * 60) / 60 >= 10) {
                if (index % 60 >= 10) {
                    time = "" + index / (60 * 60) + ":" + index % (60 * 60) / 60 + ":" + index % 60;
                } else {
                    time = "" + index / (60 * 60) + ":" + index % (60 * 60) / 60 + ":0" + index % 60;
                }
            } else {
                if (index % 60 >= 10) {
                    time = "" + index / (60 * 60) + ":0" + index % (60 * 60) / 60 + ":" + index % 60;
                } else {
                    time = "" + index / (60 * 60) + ":0" + index % (60 * 60) / 60 + ":0" + index % 60;
                }
            }
        } else if (index / 60 != 0) {
            if (index / 60 >= 10) {
                if (index % 60 >= 10) {
                    time = "" + index / 60 + ":" + index % 60;
                } else {
                    time = "" + index / 60 + ":0" + index % 60;
                }
            } else {
                if (index % 60 >= 10) {
                    time = "0" + index / 60 + ":" + index % 60;
                } else {
                    time = "0" + index / 60 + ":0" + index % 60;
                }
            }
        } else {
            if (index >= 10) {
                time = "00:" + index;
            } else {
                time = "00:0" + index;
            }
        }
        return time;
    }

    //从磁盘获取bitmap
    public Bitmap getDiskBitmap(String filePath) {
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //使用Bitmap加Matrix来缩放
    public Drawable resizeImage(Bitmap bitmap, int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return new BitmapDrawable(resizedBitmap);
    }
}
