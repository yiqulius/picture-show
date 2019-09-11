package com.wuxiaolong.androidmvpsample.testcard;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * 获取屏幕的宽高像素和密度等工具类
 */

public class DisplayUtil {
    private static final String TAG = "DisplayUtil";

    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //4.2开始有虚拟导航栏，增加了该方法才能准确获取屏幕高度
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            //displayMetrics = activity.getResources().getDisplayMetrics();//或者该方法也行
        }
        return displayMetrics;
    }

    public static DisplayMetrics getDisplayMetrics(Context context){
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        }else{
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics;
    }

    public static void printDisplayMetrics(Activity activity){
        DisplayMetrics displayMetrics = getDisplayMetrics(activity);
        Log.v(TAG,"---printDisplayMetrics---" +
                "widthPixels=" + displayMetrics.widthPixels
                + ", heightPixels=" + displayMetrics.heightPixels
                + ", density=" + displayMetrics.density
                + ", densityDpi="+displayMetrics.densityDpi);
    }

    public static void printDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        Log.v(TAG,"---printDisplayMetrics---" +
                "widthPixels=" + displayMetrics.widthPixels
                + ", heightPixels=" + displayMetrics.heightPixels
                + ", density=" + displayMetrics.density
                + ", densityDpi="+displayMetrics.densityDpi);
    }

}