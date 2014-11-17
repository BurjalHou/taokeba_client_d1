package com.taokeba.common;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhaolin on 14-2-25.
 */
public class BitmapManager {

    private  static HashMap<String, SoftReference<Bitmap>> cache;
    private  static ExecutorService pool;
    private static Map<ImageView, String> imageViews;
    private Bitmap defaultBmp;

    static {
        cache = new HashMap<String, SoftReference<Bitmap>>();
        pool = Executors.newFixedThreadPool(5);
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }

    public BitmapManager() {

    }

    public BitmapManager(Bitmap def) {
        this.defaultBmp = def;
    }

    public void setDefaultBmp(Bitmap bmp) {
        defaultBmp = bmp;
    }

    public void loadBitmap(String url, ImageView imageView) {
        loadBitmap(url, imageView, this.defaultBmp, 0, 0);
    }

    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {
        loadBitmap(url, imageView, defaultBmp, 0, 0);
    }

    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp, int width, int height) {
        imageViews.put(imageView, url);
        Bitmap bitmap = getBitmapFromCache(url);

        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //String fileName =
        }
    }

    public Bitmap getBitmapFromCache(String url) {
        Bitmap bitmap = null;
        if(cache.containsKey(url)) {
            bitmap = cache.get(url).get();
        }
        return bitmap;
    }
}
