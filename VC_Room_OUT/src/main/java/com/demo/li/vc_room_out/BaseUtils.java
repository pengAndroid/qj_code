package com.demo.li.vc_room_out;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

public class BaseUtils {
    public static void printResolution(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        int sw = context.getResources().getConfiguration().smallestScreenWidthDp;
        Log.v("screen_size", "屏幕分辨率:" + width + "*" + height + ",dpi:" + dm.densityDpi + ",sw:" + sw);
    }


    public static boolean isLandscape(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        return width < height;
    }
}
