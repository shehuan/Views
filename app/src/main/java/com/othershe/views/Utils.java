package com.othershe.views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/4/22.
 */
public class Utils {
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    public static float getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi / 320;
    }
}
